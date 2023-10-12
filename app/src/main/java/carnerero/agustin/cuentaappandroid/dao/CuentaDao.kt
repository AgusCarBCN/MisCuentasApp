package carnerero.agustin.cuentaappandroid.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import carnerero.agustin.cuentaappandroid.DataBaseApp
import carnerero.agustin.cuentaappandroid.model.Cuenta

class CuentaDao(private val admin: DataBaseApp) {


    // Método para insertar una cuenta en la base de datos
    fun insertarCuenta(cuenta: Cuenta) {
        val db = admin.writableDatabase
        val values = ContentValues()
        values.put("iban", cuenta.iban)
        values.put("saldo", cuenta.saldo)
        values.put("dni", cuenta.dni)

        try {
            db.insert("CUENTA", null, values)
        } catch (e: SQLException) {
            // Manejo de errores al insertar la cuenta
        } finally {
            db.close()
        }
    }

    // Método para obtener una cuenta por IBAN
    fun obtenerCuentaPorIban(iban: String): Cuenta? {
        val db = admin.readableDatabase
        val query = "SELECT * FROM CUENTA WHERE iban = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(iban))

        var cuenta: Cuenta? = null

        if (cursor.moveToFirst()) {
            cuenta = Cuenta(
                cursor.getString(cursor.getColumnIndexOrThrow("iban")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("saldo")),
                cursor.getString(cursor.getColumnIndexOrThrow("dni"))
            )
        }

        cursor.close()
        db.close()
        return cuenta
    }
    fun listarTodasLasCuentas(): List<Cuenta> {
        val cuentas = mutableListOf<Cuenta>()

        admin.readableDatabase.use { db ->
            val query = "SELECT * FROM CUENTA"
            val cursor = db.rawQuery(query, null)

            while (cursor.moveToNext()) {
                val iban = cursor.getString(cursor.getColumnIndexOrThrow("iban"))
                val saldo = cursor.getDouble(cursor.getColumnIndexOrThrow("saldo"))
                val dni = cursor.getString(cursor.getColumnIndexOrThrow("dni"))

                val cuenta = Cuenta(iban, saldo, dni)
                cuentas.add(cuenta)
            }
            cursor.close()
        }
        return cuentas
    }
    fun actualizarSaldo(importe: Double, iban: String) {
        val db = admin.writableDatabase
        val query = "UPDATE cuenta SET saldo = saldo + '${importe}' WHERE iban ='${iban}'"
        db.execSQL(query)
        db.close()
    }

}
