package carnerero.agustin.cuentaappandroid.dao


import android.database.SQLException
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario


class MovimientoBancarioDAOProxy(private val movimientoBancarioDAO: MovimientoBancarioDAO) {

    fun nuevoImporte(movimientoBancario: MovimientoBancario) {
        try {
            movimientoBancarioDAO.nuevoImporte(movimientoBancario)
        } catch (e: SQLException) {
            // Manejar la excepción
        }
    }
    fun listarMovimientos(): ArrayList<MovimientoBancario> {
        var movimientos = ArrayList<MovimientoBancario>()
        try {
            movimientos = movimientoBancarioDAO.getAll()
        } catch (e: SQLException) {

        }
        return movimientos
    }
    fun listarMovimientos(iban:String): ArrayList<MovimientoBancario> {
        var movimientos = ArrayList<MovimientoBancario>()
        try {
         movimientos = movimientoBancarioDAO.getIncomeandBills(iban)
        } catch (e: SQLException) {

        }
        return movimientos
    }
    fun listarIngresos(iban:String): ArrayList<MovimientoBancario> {
        var movimientos = ArrayList<MovimientoBancario>()
        try {
             movimientos = movimientoBancarioDAO.getIncome(iban)
        } catch (e: SQLException) {

        }
        return movimientos
    }
    fun listarGastos(iban:String): ArrayList<MovimientoBancario> {
        var movimientos = ArrayList<MovimientoBancario>()
        try {
            movimientos = movimientoBancarioDAO.getBills(iban)
        } catch (e: SQLException) {

        }
        return movimientos
    }

    fun getByAmountRange(importeDesde: String, importeHasta: String, movimientos: ArrayList<MovimientoBancario>): ArrayList<MovimientoBancario> {
        try {
            if (isValidNumbers(importeDesde, importeHasta)) {
                val desde = importeDesde.toDouble()
                val hasta = importeHasta.toDouble()
                val filtroImporte: (MovimientoBancario) -> Boolean = { mov -> Math.abs(mov.importe) >= desde && Math.abs(mov.importe) <= hasta }
                movimientos.removeIf { !filtroImporte(it) }
            }
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Formato de números inválidos")
        }
        return movimientos
    }


    private fun isValidNumbers(desde: String, hasta: String): Boolean {
        return try {
            val importeDesde = desde.toDouble()
            val importeHasta = hasta.toDouble()
            true
        } catch (e: NumberFormatException) {

            false
        }
    }
    fun getByDescription(textSearch: String, movimientos: List<MovimientoBancario>): List<MovimientoBancario> {
        return movimientos.filter { it.descripcion.equals(textSearch) }
    }
    fun getByDateRange(movimientos: ArrayList<MovimientoBancario>, fechaDesde: String, fechaHasta: String): List<MovimientoBancario> {
        return movimientos.filter { it.fechaImporte.toString() in fechaDesde..fechaHasta }
    }

}
