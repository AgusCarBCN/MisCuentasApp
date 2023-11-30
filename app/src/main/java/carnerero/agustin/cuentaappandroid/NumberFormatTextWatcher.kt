package carnerero.agustin.cuentaappandroid
import android.text.Editable
import android.text.TextWatcher
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class NumberFormatTextWatcher : TextWatcher {

    private val decimalFormat: DecimalFormat

    constructor() {
        val symbols = DecimalFormatSymbols(Locale.getDefault())
        symbols.groupingSeparator = ','

        decimalFormat = DecimalFormat("#,###.###", symbols)  // Permite hasta tres decimales
        decimalFormat.isGroupingUsed = true
    }

    override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
        // No es necesario hacer nada aquí
    }

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        // No es necesario hacer nada aquí
    }

    override fun afterTextChanged(editable: Editable?) {
        if (editable != null && editable.isNotEmpty()) {
            val originalText = editable.toString().replace(",", "") // Eliminar separadores existentes

            // Formatear el texto y aplicar al EditText
            val formattedText = decimalFormat.format(originalText.toDoubleOrNull() ?: 0.0)
            editable.replace(0, editable.length, formattedText)
        }
    }
}

