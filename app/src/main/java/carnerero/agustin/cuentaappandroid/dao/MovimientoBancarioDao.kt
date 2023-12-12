package carnerero.agustin.cuentaappandroid.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.SQLException
import carnerero.agustin.cuentaappandroid.DataBaseApp
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario

class MovimientoBancarioDAO(private val admin: DataBaseApp) {
    // Consultas SQL predefinidas
    private val selectAll = "SELECT * FROM MOVIMIENTO"
    private val selectAllByIban = "SELECT * FROM MOVIMIENTO WHERE iban=?"
    private val selectIncomes="SELECT * FROM MOVIMIENTO WHERE iban=? AND importe>=0"
    //private val selectIncomes = "SELECT * FROM MOVIMIENTO m JOIN INGRESO i ON m.id=i.id WHERE m.iban = ?"
    //private val selectBills = "SELECT * FROM MOVIMIENTO m JOIN GASTO g ON m.id=g.id WHERE m.iban = ?"
    private val selectBills="SELECT * FROM MOVIMIENTO WHERE iban=? AND importe<0"
    // Método para insertar un nuevo movimiento bancario en la base de datos
    fun nuevoImporte(movimientoBancario: MovimientoBancario) {
        admin.writableDatabase.use { db ->
            db.execSQL("PRAGMA foreign_keys = ON;")
            val values = ContentValues().apply {
                put("importe", movimientoBancario.importe)
                put("descripcion", movimientoBancario.descripcion)
                put("iban", movimientoBancario.iban)
                put("fechaImporte", movimientoBancario.fechaImporte)
            }

            try {
                db.insert("MOVIMIENTO", null, values)
            } catch (_: SQLException) {
                // Manejo de errores al insertar el movimiento bancario
            }
        }
    }

    // Métodos para obtener diferentes tipos de movimientos bancarios
    fun getAll(): ArrayList<MovimientoBancario> {
        return listarMovimientos(selectAll, null)
    }

    fun getIncome(iban: String): ArrayList<MovimientoBancario> {
        return listarMovimientos(selectIncomes, iban)
    }

    fun getIncomeandBills(iban: String): ArrayList<MovimientoBancario> {
        return listarMovimientos(selectAllByIban, iban)
    }

    fun getBills(iban: String): ArrayList<MovimientoBancario> {
        return listarMovimientos(selectBills, iban)
    }

    // Método privado para listar movimientos según una consulta y un IBAN opcional
    private fun listarMovimientos(query: String, iban: String?): ArrayList<MovimientoBancario> {
        val movimientos = ArrayList<MovimientoBancario>()
        admin.readableDatabase.use { db ->
            try {
                val cursor: Cursor = db.rawQuery(query, if (iban != null) arrayOf(iban) else null)

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
                // Manejo de errores al listar los movimientos
            }
        }

        return movimientos
    }
}
