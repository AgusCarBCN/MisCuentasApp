package carnerero.agustin.cuentaappandroid.dao

import android.content.ContentValues
import android.database.SQLException
import carnerero.agustin.cuentaappandroid.DataBaseApp
import carnerero.agustin.cuentaappandroid.model.Cuenta

class CuentaDao(private val admin: DataBaseApp) {

    // Método para insertar una cuenta en la base de datos
    fun insertarCuenta(cuenta: Cuenta) {
        admin.writableDatabase.use { db ->

            db.execSQL("PRAGMA foreign_keys = ON;")
            val values = ContentValues().apply {
                put("iban", cuenta.iban)
                put("saldo", cuenta.saldo)
            }

            try {
                db.insert("CUENTA", null, values)
            } catch (e: SQLException) {
                // Manejo de errores al insertar la cuenta
            }
        }
    }


    fun actualizarSaldo(importe: Double, iban: String) {
        admin.writableDatabase.use { db ->
            db.execSQL("PRAGMA foreign_keys = ON;")
            val query = "UPDATE CUENTA SET saldo = saldo + '$importe' WHERE iban = '$iban'"
            db.execSQL(query)
        }
    }

    fun borrarCuentaPorIBAN(iban: String) {
        admin.writableDatabase.use { db ->
            db.execSQL("PRAGMA foreign_keys = ON;")
            val whereClause = "iban = ?"
            val whereArgs = arrayOf(iban)
            db.delete("CUENTA", whereClause, whereArgs)
        }
    }

    fun cambiarIbanCuenta(ibanActual: String, nuevoIban: String) {
        admin.writableDatabase.use { db ->
            db.execSQL("PRAGMA foreign_keys = ON;")

            // Actualizar el IBAN en la tabla CUENTA
            val values = ContentValues().apply {
                put("iban", nuevoIban)
            }
            val whereClause = "iban = ?"
            val whereArgs = arrayOf(ibanActual)
            try {
                db.update("CUENTA", values, whereClause, whereArgs)
            } catch (e: SQLException) {
                // Manejo de errores al cambiar el IBAN de la cuenta
            }

            // Actualizar el IBAN en otras tablas que hacen referencia a la cuenta
            val tablasReferenciadas = listOf("MOVIMIENTO")  // Agrega más tablas según sea necesario

            for (tabla in tablasReferenciadas) {
                val updateQuery = "UPDATE $tabla SET iban = ? WHERE iban = ?"
                val updateArgs = arrayOf(nuevoIban, ibanActual)

                try {
                    db.execSQL(updateQuery, updateArgs)
                } catch (e: SQLException) {
                    // Manejo de errores al cambiar el IBAN en las tablas referenciadas
                }
            }
        }
    }
    fun borrarTodasLasCuentas() {
        admin.writableDatabase.use { db ->
            db.execSQL("PRAGMA foreign_keys = ON;")
            db.delete("CUENTA", null, null)
        }
    }
    fun listarTodasLasCuentas(): MutableList<Cuenta> {
        val cuentas = mutableListOf<Cuenta>()

        admin.readableDatabase.use { db ->
            val query = "SELECT iban, saldo FROM CUENTA"
            val cursor = db.rawQuery(query, null)
            while (cursor.moveToNext()) {
                val iban = cursor.getString(cursor.getColumnIndexOrThrow("iban"))
                val saldo = cursor.getDouble(cursor.getColumnIndexOrThrow("saldo"))
                val cuenta = Cuenta(iban, saldo)
                cuentas.add(cuenta)
            }
            cursor.close()
        }
        return cuentas
    }
}
