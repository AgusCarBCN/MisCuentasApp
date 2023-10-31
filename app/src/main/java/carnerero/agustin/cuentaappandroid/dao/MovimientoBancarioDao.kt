package carnerero.agustin.cuentaappandroid.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.SQLException
import carnerero.agustin.cuentaappandroid.DataBaseApp
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.StringTokenizer


class MovimientoBancarioDAO(private val admin: DataBaseApp) {
    private val selectAll = "SELECT * FROM MOVIMIENTO"
    private val selectAllByIban = "SELECT * FROM MOVIMIENTO WHERE iban=?"
    private val selectIncomes =
        "SELECT * FROM MOVIMIENTO m JOIN INGRESO i ON m.id=i.id WHERE m.iban = ?"
    private val selectBills =
        "SELECT * FROM MOVIMIENTO m JOIN GASTO g ON m.id=g.id WHERE m.iban = ?"

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

    private fun listarMovimientos(query: String, iban: String?): ArrayList<MovimientoBancario> {
        val movimientos = ArrayList<MovimientoBancario>()
        val db = admin.readableDatabase

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
            // Manejo de errores
        } finally {
            db.close()
        }

        return movimientos
    }








}