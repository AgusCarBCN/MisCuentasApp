package carnerero.agustin.cuentaappandroid
import android.text.Editable
import android.text.TextWatcher

class SeparateThousands(val groupingSeparator: String, val decimalSeparator: String) : TextWatcher {

    private var busy = false

    override fun afterTextChanged(s: Editable?) {
        // Verifica que el texto no sea nulo y que no estemos en medio de un procesamiento anterior
        if (s != null && !busy) {
            // Establece la bandera busy para indicar que estamos procesando el texto
            busy = true

            // Inicializa una variable para llevar la cuenta de la posición del dígito actual
            var place = 0

            // Obtiene la posición del separador decimal en el texto
            val decimalPointIndex = s.indexOf(decimalSeparator)

            // Inicializa i con la posición del dígito más a la derecha antes del separador decimal
            // o el último dígito si no hay separador decimal
            var i = if (decimalPointIndex == -1) {
                s.length - 1
            } else {
                decimalPointIndex - 1
            }

            // Inicia un bucle que recorre los dígitos del número desde la posición i hasta el inicio del texto
            while (i >= 0) {
                // Obtiene el carácter en la posición actual
                val c = s[i]

                // Elimina el carácter si es el separador de millares
                if (c == groupingSeparator[0]) {
                    s.delete(i, i + 1)
                } else {
                    // Inserta el separador de millares (punto) a la izquierda de cada tercer dígito,
                    // siempre que no sea el dígito más a la izquierda
                    if (place % 3 == 0 && place != 0) {
                        s.insert(i + 1, groupingSeparator)
                    }

                    // Incrementa la posición del dígito actual
                    place++
                }

                // Mueve la posición i al siguiente dígito hacia la izquierda
                i--
            }

            // Restablece la bandera busy para indicar que el procesamiento ha terminado
            busy = false
        }
    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}