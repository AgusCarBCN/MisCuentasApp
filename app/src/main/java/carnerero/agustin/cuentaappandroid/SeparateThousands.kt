package carnerero.agustin.cuentaappandroid
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import java.util.StringTokenizer

class SeparateThousands(val groupingSeparator: String, val decimalSeparator: String) : TextWatcher {

    private var busy = false

    override fun afterTextChanged(s: Editable?) {
        if (s != null && !busy) {
            busy = true

            var placeBefore = 0
            var placeAfter = 0
            var operatorFound = false

            val decimalPointIndex = s.indexOf(decimalSeparator)
            var i = if (decimalPointIndex == -1) {
                s.length - 1
            } else {
                decimalPointIndex - 1
            }

            while (i >= 0) {
                val c = s[i]

                if (c == '+' || c == '-' || c == '÷' || c == '×') {
                    // Si se encuentra un operador, se indica y se reinicia la cuenta de dígitos
                    operatorFound = true
                    placeBefore = 0
                } else if (c == groupingSeparator[0]) {
                    s.delete(i, i + 1) // Se elimina el separador de millares
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

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // No se realiza ninguna acción mientras el texto está cambiando
    }
}


