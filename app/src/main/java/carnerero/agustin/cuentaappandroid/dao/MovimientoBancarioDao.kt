package carnerero.agustin.cuentaappandroid.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.SQLException
import carnerero.agustin.cuentaappandroid.DataBaseApp
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario

class MovimientoBancarioDAO(private val admin: DataBaseApp) {
    private val SELECT_ALL = "SELECT * FROM MOVIMIENTO"
    private val SELECT_ALL2 = "SELECT * FROM MOVIMIENTO WHERE iban=?"
    private val SELECT_INCOME = "SELECT * FROM MOVIMIENTO m JOIN INGRESO i ON m.id=i.id WHERE m.iban = ?"
    private val SELECT_BILLS = "SELECT * FROM MOVIMIENTO m JOIN GASTO g ON m.id=g.id WHERE m.iban = ?"

    fun nuevoImporte(movimientoBancario: MovimientoBancario) {
        val db = admin.writableDatabase
        val values = ContentValues()
        values.put("importe", movimientoBancario.importe)
        values.put("descripcion", movimientoBancario.descripcion)
        values.put("iban", movimientoBancario.iban)
        values.put("fechaImporte", movimientoBancario.fechaImporte)
        try {
            db.insert("MOVIMIENTO", null, values)
        } catch (e: SQLException) {
            // Manejo de errores
        } finally {
            db.close()
        }
    }

    fun getAll(): ArrayList<MovimientoBancario> {
        return listarMovimientos(SELECT_ALL, null)
    }

    fun getIncome(iban: String): ArrayList<MovimientoBancario> {
        return listarMovimientos(SELECT_INCOME, iban)
    }
    fun getIncomeandBills(iban: String): ArrayList<MovimientoBancario> {
        return listarMovimientos(SELECT_ALL2, iban)
    }
    fun getBills(iban: String): ArrayList<MovimientoBancario> {
        return listarMovimientos(SELECT_BILLS, iban)
    }

    private fun listarMovimientos(query: String, iban: String?): ArrayList<MovimientoBancario> {
        val movimientos = ArrayList<MovimientoBancario>()
        val db = admin.readableDatabase
        val selectQuery = query

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, if (iban != null) arrayOf(iban) else null)

            if (cursor.moveToFirst()) {
                do {
                    val movimiento = MovimientoBancario(
                        cursor.getDouble(cursor.getColumnIndexOrThrow("importe")),
                        cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                        cursor.getString(cursor.getColumnIndexOrThrow("iban")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fechaImporte"))
                    )
                    movimientos.add(movimiento)
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLException) {
            // Manejo de errores
        } finally {
            db.close()
        }

        return movimientos
    }
}
