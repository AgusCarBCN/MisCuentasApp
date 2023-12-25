package carnerero.agustin.cuentaappandroid
import android.text.Editable
import android.text.TextWatcher


class SeparateThousands(val groupingSeparator: String, val decimalSeparator: String) : TextWatcher {

    // Variable para controlar si la lógica de cambio de texto está ocupada
    private var busy = false
    private var decimalSeparatorFound=false
    // Método llamado después de que el texto ha cambiado
    override fun afterTextChanged(s: Editable?) {
        if (s != null && !busy) {
            busy = true
            var placeBefore = 0 // Contador de dígitos antes del operador
            var placeAfter = 0 // Contador de dígitos después del operador
            var operatorFound = false // Indica si se encontró un operador en la cadena

            // Índice del punto decimal en el texto
            val decimalPointIndex = s.indexOf(decimalSeparator)
            var i = if (decimalPointIndex == -1) {
                s.length - 1
            } else {
                decimalPointIndex - 1
            }


            // Recorriendo el texto desde el final hasta el inicio
            while (i >= 0) {
                val c = s[i]
                if (c == '+' || c == '-' || c == '÷' || c == '×') {
                    operatorFound = true
                    placeBefore = 0
                    placeAfter = 0

                } else if (c == groupingSeparator[0]) {
                    s.delete(i, i + 1)
                } else if (c == decimalSeparator[0]) {
                    if (operatorFound) {
                        // Si encontramos un punto decimal después del operador,
                        // formatear solo los dígitos después del operador
                        if (placeAfter % 3 == 0 && placeAfter != 0) {
                            s.insert(i + 1, groupingSeparator)
                        }
                        placeAfter++
                    } else {
                        // Si encontramos un punto decimal antes del operador,
                        // formatear solo los dígitos antes del operador
                        if (placeBefore % 3 == 0 && placeBefore != 0) {
                            s.insert(i + 1, groupingSeparator)
                        }
                        placeBefore++
                    }
                } else {
                    if (!operatorFound && placeBefore % 3 == 0 && placeBefore != 0) {
                        s.insert(i + 1, groupingSeparator)
                    } else if (operatorFound && placeAfter % 3 == 0 && placeAfter != 0) {
                        s.insert(i + 1, groupingSeparator)
                    }

                    if (!operatorFound) {
                        placeBefore++
                    } else {
                        placeAfter++
                    }
                }
                i--
            }
            busy = false
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // No se realiza ninguna acción antes de que el texto cambie
    }

    // Método llamado cuando el texto está cambiando
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // No se realiza ninguna acción mientras el texto está cambiando
    }
    private fun formatNumberPart(input: CharSequence): CharSequence {
        var placeBefore = 0
        var placeAfter = 0
        var decimalSeparatorFound = false

        val result = StringBuilder()

        // Índice del punto decimal en el texto
        val decimalPointIndex = input.indexOf(decimalSeparator)
        var i = if (decimalPointIndex == -1) {
            input.length - 1
        } else {
            decimalPointIndex - 1
        }

        while (i >= 0) {
            val c = input[i]

            if (c == groupingSeparator[0]) {
                // Se elimina el separador de millares
                // No se añade al resultado ya que se formateará nuevamente
            } else if (c == decimalSeparator[0]) {
                // Se añade el separador decimal solo si hay dígitos después de él
                if (placeAfter % 3 == 0 && placeAfter != 0) {
                    result.insert(0, groupingSeparator)
                }
                decimalSeparatorFound = true
            } else {
                // Formatear números antes y después del operador
                if (!decimalSeparatorFound && placeBefore % 3 == 0 && placeBefore != 0) {
                    result.insert(0, groupingSeparator)
                } else if (decimalSeparatorFound && placeAfter % 3 == 0 && placeAfter != 0) {
                    result.insert(0, groupingSeparator)
                }

                // Añadir el dígito al resultado
                result.insert(0, c)

                if (!decimalSeparatorFound) {
                    placeBefore++
                } else {
                    placeAfter++
                }
            }
            i--
        }

        return result
    }
}



