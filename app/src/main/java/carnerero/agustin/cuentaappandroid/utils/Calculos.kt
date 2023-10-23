package carnerero.agustin.cuentaappandroid.utils

import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Calculos {

    companion object {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        fun calcularImporteMes(month: Int, year: Int, importes: ArrayList<MovimientoBancario>): Float {
            var importeTotal = 0.0

            for (mov in importes) {
                var fechaImporteDate = LocalDate.parse(mov.fechaImporte, formatter)
                if (fechaImporteDate.monthValue == month && fechaImporteDate.year == year) {
                    importeTotal += mov.importe
                }
            }
            return importeTotal.toFloat()
        }

    }

}