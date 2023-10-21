package carnerero.agustin.cuentaappandroid.utils

import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Calculos {

    companion object {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        fun calcularIngresoMes(month: Int, year: Int, ingresos: List<MovimientoBancario>): Double {
            var ingresoTotal = 0.0

            for (ingreso in ingresos) {
                var fechaImporteDate = LocalDate.parse(ingreso.fechaImporte, formatter)
                if (fechaImporteDate.monthValue == month && fechaImporteDate.year == year) {
                    ingresoTotal += ingreso.importe
                }
            }
            return ingresoTotal
        }

        fun calcularGastoMes(month: Int, year: Int, gastos: List<MovimientoBancario>): Double {
            var totalGastos = 0.0
            for (gasto in gastos) {
                var fechaImporteDate = LocalDate.parse(gasto.fechaImporte, formatter)
                if (fechaImporteDate.monthValue == month && fechaImporteDate.year == year) {
                    totalGastos += gasto.importe
                }
            }
            return totalGastos
        }
    }

}