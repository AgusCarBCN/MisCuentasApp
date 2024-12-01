package carnerero.agustin.cuentaappandroid.utils

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Entry
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.currencyLocales
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.EntryCSV
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class Utils {

    companion object {

        fun isValidDecimal(text: String): Boolean {

            return text.isEmpty() || text.matches(Regex("^([1-9]\\d*|0)?(\\.\\d*)?\$"))

        }

        fun convertMillisToDate(millis: Long): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return formatter.format(Date(millis))
        }

        fun numberFormat(amount: Double, currencyCode: String): String {

            val locale = currencyLocales[currencyCode] ?: Locale.GERMAN
            // Formatear la cantidad en la moneda especificada
            val numberFormat = NumberFormat.getCurrencyInstance(locale)
            // Iniciar la carga de cuentas solo cuando el Composable se inicia
            return numberFormat.format(
                abs(amount)
            )
        }


        fun toDateFormatDayMonth(date: String): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateEntry = formatter.parse(date)
            date.let { formatter.parse(it) }
            return dateEntry?.dateFormatDayMonth() ?: ""

        }
        fun toDateShortFormatLocale(date:String): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateEntry = formatter.parse(date)
            date.let { formatter.parse(it) }
            return dateEntry?.dateFormatByLocale() ?: ""
        }


        fun convertStringToLocalDate(date: String): LocalDate {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
            return LocalDate.parse(date, formatter)
        }

        fun writeCsvFile(
            entries: MutableList<EntryCSV>,
            context: Context,
            fileName: String,
            directory: DocumentFile
        ) {
            val file = directory.createFile("text/csv", fileName)
            val outputStream = context.contentResolver.openOutputStream(file?.uri!!)

            BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
                // Escribir el encabezado del CSV
                writer.write("Description,CategoryName,Amount,Date,AccountName,CategoryId,accountId\n")

                //Escribir las rutas en fichero
                for (entry in entries) {
                    // Asegúrate de que los datos sean válidos antes de escribirlos
                    val csvLine =
                                "${entry.description}," +
                                "${entry.category}," +
                                "${entry.amount}," +
                                "${entry.date}," +
                                "${entry.accountName}," +
                                "${entry.categoryId}," +
                                "${entry.accountId}\n"
                    writer.write(csvLine)
                }
            }

        }

        fun readCsvFile(
            context: Context,
            fileUri: Uri
        ): MutableList<Entry> {
            val entries = mutableListOf<Entry>()

            context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val csvParser = CSVParser.parse(bufferedReader, CSVFormat.DEFAULT)

                for (record in csvParser) {
                    try {
                        val description = record.get(0)
                        val category = record.get(1)
                        val amount = record.get(2).toDoubleOrNull() ?: 0.0
                        val date = record.get(3)
                        val accountName = record.get(4)
                        val categoryId = record.get(5).toInt()
                        val accountId = record.get(6).toInt()

                        // Crear objeto route y agregarlo a lista

                        val entry = Entry(
                            description = description,
                            amount = amount,
                            date = date,
                            categoryId = categoryId,
                            accountId = accountId
                        )
                        entries.add(entry)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            return entries
        }

        fun getMapOfEntriesByCategory(listOfEntries: List<EntryDTO>): Map<Int, Pair<Int?, Double?>> {
            val groupedEntriesByCategoryName = listOfEntries.groupBy { it.nameResource }

            val categoryIcons = groupedEntriesByCategoryName.mapValues { (_, entries) ->
                entries.firstOrNull()?.iconResource
            }
            val categoryTotals = groupedEntriesByCategoryName.mapValues { (_, entries) ->
                entries.sumOf { it.amount }
            }
            return categoryIcons.map { (categoryName, icon) ->
                categoryName to Pair(icon, categoryTotals[categoryName])
            }.toMap()
        }


    }
}