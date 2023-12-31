package carnerero.agustin.cuentaappandroid.utils



import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import carnerero.agustin.cuentaappandroid.AppConst
import carnerero.agustin.cuentaappandroid.interfaces.OnResolveListener
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Locale

class Utils {

    companion object {

        private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var isDarkTheme=false

        fun calcularImporteMes(
            month: Int,
            year: Int,
            importes: ArrayList<MovimientoBancario>
        ): Float {
            var importeTotal = 0.0

            for (mov in importes) {
                val fechaImporteDate = LocalDate.parse(mov.fechaImporte, formatter)
                if (fechaImporteDate.monthValue == month && fechaImporteDate.year == year) {
                    importeTotal += mov.importe
                }
            }
            return importeTotal.toFloat()
        }
        fun calcularImporteSemanal(
            week: Int,
            year: Int,
            importes: ArrayList<MovimientoBancario>
        ): Float {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
            var importeTotal = 0.0
            for (mov in importes) {
                try {
                    val fechaImporteDate = LocalDate.parse(mov.fechaImporte, formatter)
                    val weekNumber = fechaImporteDate.get(WeekFields.ISO.weekOfWeekBasedYear())
                    if (weekNumber == week && fechaImporteDate.year == year) {
                        importeTotal += mov.importe
                    }
                } catch (e: Exception) {
                    // Manejar cualquier error al analizar la fecha
                    e.printStackTrace()
                }
            }
            return importeTotal.toFloat()
        }

        fun applyTheme(enableDarkTheme: Boolean) {
            isDarkTheme = if (enableDarkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                false
            }

        }
        fun applyLanguage(enableEnLang: Boolean) {

            if (enableEnLang) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
            } else {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("es"))
            }
        }
        fun tryResolve(operationRef: String, isResolve: Boolean,listener : OnResolveListener) {
            if (operationRef.isEmpty()) return

            var operation = operationRef

            if (operation.endsWith(AppConst.COMA)) {
                operation = operation.substring(0, operation.length - 1)
            }

            val operator = getOperator(operationRef)
            var values: List<String> = emptyList()

            if (operator != AppConst.NULL) {
                values = when (operator) {
                    AppConst.RESTAR -> {
                        val index = operation.lastIndexOf(AppConst.RESTAR)
                        if (index < operationRef.length - 1) {
                            listOf(operation.substring(0, index), operation.substring(index + 1))
                        } else {
                            listOf(operation.substring(0, index))
                        }
                    }
                    else -> operation.split(operator)
                }
            }

            if (values.size > 1) {
                try {
                    // Replace comma with dot in the input numbers
                    val (number1, number2) = values.map {  it.replace(".", "").replace(",", ".").toDouble()}
                    listener.showResult(result(number1, number2, operator))


                } catch (e: NumberFormatException) {
                    if (isResolve) {
                        listener.showMessageError(R.string.formaterror)

                    }
                }
            } else {
                if (isResolve && operator != AppConst.NULL) {
                    listener.showMessageError(R.string.incorrectExpresion)
                }
            }
        }

        fun getOperator(operation: String): String {
            return when {
                operation.contains(AppConst.MULTIPLICAR) -> AppConst.MULTIPLICAR
                operation.contains(AppConst.DIVIDIR) -> AppConst.DIVIDIR
                operation.contains(AppConst.SUMAR) -> AppConst.SUMAR
                operation.contains(AppConst.PORCENTAJE) -> AppConst.PORCENTAJE
                operation.contains(AppConst.RESTAR) -> AppConst.RESTAR
                else -> AppConst.NULL
            }
        }
        fun replaceOperator(charSequence: CharSequence): Boolean {

            if (charSequence.length < 2) return false
            val lastElement = charSequence[charSequence.length - 1].toString()
            val penultElement = charSequence[charSequence.length - 2].toString()
            return (lastElement == AppConst.MULTIPLICAR || lastElement == AppConst.DIVIDIR || lastElement == AppConst.SUMAR) && (penultElement == AppConst.MULTIPLICAR || penultElement == AppConst.DIVIDIR || penultElement == AppConst.SUMAR || penultElement==AppConst.RESTAR)
        }

        fun getMonth(): Int {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.MONTH) + 1
        }

        fun getWeek():Int
        {
            val calendar=Calendar.getInstance()
            return calendar.get(Calendar.WEEK_OF_YEAR)+1
        }
        fun getYear():Int{
            val calendar=Calendar.getInstance()
            return calendar.get(Calendar.YEAR)
        }

        fun saveImageToExternalStorage(context: Context, uri: Uri): String? {
            return try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val file = File(context.externalCacheDir, "image.jpg")
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()
                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()

                null
            }
        }

        private fun result(number1: Double, number2: Double, operator: String): Double {

            return when(operator) {
                AppConst.SUMAR -> number1 + number2
                AppConst.MULTIPLICAR -> number1 * number2
                AppConst.DIVIDIR ->  number1 / number2
                else-> number1 - number2
            }
        }

    }




}

