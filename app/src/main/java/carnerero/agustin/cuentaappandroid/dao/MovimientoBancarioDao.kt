package carnerero.agustin.cuentaappandroid.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.SQLException
import carnerero.agustin.cuentaappandroid.DataBaseApp
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario

class MovimientoBancarioDAO(private val admin: DataBaseApp) {

    fun nuevoImporte(movimientoBancario: MovimientoBancario) {
        val db = admin.writableDatabase
        val values = ContentValues()
        values.put("importe", movimientoBancario.importe)
        values.put("descripcion", movimientoBancario.descripcion)
        values.put("iban", movimientoBancario.iban)
        values.put("fechaImporte",movimientoBancario.fechaImporte)
        try {
            db.insert("MOVIMIENTO", null, values)
        } catch (e: SQLException) {

        }
        db.close()
    }
    fun listarMovimientos(): ArrayList<MovimientoBancario> {
        val movimientos = ArrayList<MovimientoBancario>()
        val db = admin.readableDatabase
        val selectQuery = "SELECT * FROM MOVIMIENTO"

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)

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

