package carnerero.agustin.cuentaappandroid.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.SQLException
import carnerero.agustin.cuentaappandroid.DataBaseApp
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario

class MovimientoBancarioDAO(private val admin: DataBaseApp) {
    // Consultas SQL predefinidas
    private val selectAll = "SELECT * FROM MOVIMIENTO"
    private val selectAllByIban = "SELECT * FROM MOVIMIENTO WHERE nombreCuenta=?"
    private val selectIncomes = "SELECT * FROM MOVIMIENTO WHERE nombreCuenta=? AND importe>=0"
    private val selectBills = "SELECT * FROM MOVIMIENTO WHERE nombreCuenta=? AND importe<0"

    // Método para insertar un nuevo movimiento bancario en la base de datos
    fun nuevoImporte(movimientoBancario: MovimientoBancario) {
        admin.writableDatabase.use { db ->
            db.execSQL("PRAGMA foreign_keys = ON;")
            val values = ContentValues().apply {
                put("importe", movimientoBancario.importe)
                put("descripcion", movimientoBancario.descripcion)
                put("nombreCuenta", movimientoBancario.nombreDeCuenta)
                put("fechaImporte", movimientoBancario.fechaImporte)
            }

            try {
                db.insert("MOVIMIENTO", null, values)
            } catch (_: SQLException) {

            }
        }
    }

    // Métodos para obtener diferentes tipos de movimientos bancarios
    fun getAll(): ArrayList<MovimientoBancario> {
        return listarMovimientos(selectAll, null)
    }

    fun getIncome(nombreCuenta: String): ArrayList<MovimientoBancario> {
        return listarMovimientos(selectIncomes, nombreCuenta)
    }

    fun getIncomeandBills(nombreCuenta: String): ArrayList<MovimientoBancario> {
        return listarMovimientos(selectAllByIban, nombreCuenta)
    }

    fun getBills(nombre: String): ArrayList<MovimientoBancario> {
        return listarMovimientos(selectBills, nombre)
    }

    // Método privado para listar movimientos según una consulta y un IBAN opcional
    private fun listarMovimientos(query: String, nombreCuenta: String?): ArrayList<MovimientoBancario> {
        val movimientos = ArrayList<MovimientoBancario>()
        admin.readableDatabase.use { db ->
            try {
                val cursor: Cursor = db.rawQuery(query, if (nombreCuenta != null) arrayOf(nombreCuenta) else null)

                if (cursor.moveToFirst()) {
                    do {
                        val movimiento = MovimientoBancario(
                            cursor.getDouble(cursor.getColumnIndexOrThrow("importe")),
                            cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                            cursor.getString(cursor.getColumnIndexOrThrow("nombreCuenta")),
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

    fun borrarMovimientosPorNombre(nombre: String) {
        admin.writableDatabase.use { db ->
            db.execSQL("PRAGMA foreign_keys = ON;")

            val whereClause = "nombreCuenta = ?"
            val whereArgs = arrayOf(nombre)

            try {
                db.delete("MOVIMIENTO", whereClause, whereArgs)
            } catch (_: SQLException) {

            }
        }
    }

}
