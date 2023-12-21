package carnerero.agustin.cuentaappandroid
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import java.util.StringTokenizer

class SeparateThousands(val groupingSeparator: String, val decimalSeparator: String) : TextWatcher {

    // Variable para controlar si la lógica de cambio de texto está ocupada
    private var busy = false

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

                // Si se encuentra un operador, se indica y se reinicia la cuenta de dígitos
                if (c == '+' || c == '-' || c == '÷' || c == '×') {
                    operatorFound = true
                    placeBefore = 0
                } else if (c == groupingSeparator[0]) {
                    s.delete(i, i + 1) // Se elimina el separador de millares
                } else {
                    // Si no es un operador, se formatean los dígitos según su posición y el operador
                    if (!operatorFound && placeBefore % 3 == 0 && placeBefore != 0) {
                        // Formatear números antes del operador
                        s.insert(i + 1, groupingSeparator)
                    } else if (operatorFound && placeAfter % 3 == 0 && placeAfter != 0) {
                        // Formatear números después del operador
                        s.insert(i + 1, groupingSeparator)
                    }

                    // Se incrementa el contador de dígitos según si están antes o después del operador
                    if (!operatorFound) {
                        placeBefore++
                    } else {
                        placeAfter++
                    }
                }

                i--
            }

            busy = false // Se marca como no ocupada para futuras modificaciones
        }
    }

    // Método llamado antes de que el texto cambie
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // No se realiza ninguna acción antes de que el texto cambie
    }

    // Método llamado cuando el texto está cambiando
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // No se realiza ninguna acción mientras el texto está cambiando
    }
}
