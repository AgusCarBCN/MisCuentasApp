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
                put("nombreCuenta", cuenta.nombre)
                put("saldo", cuenta.saldo)
            }

            try {
                db.insert("CUENTA", null, values)
            } catch (e: SQLException) {
                // Manejo de errores al insertar la cuenta
            }
        }
    }


    fun actualizarSaldo(importe: Double, nombre: String) {
        admin.writableDatabase.use { db ->
            db.execSQL("PRAGMA foreign_keys = ON;")
            val query = "UPDATE CUENTA SET saldo = saldo + '$importe' WHERE nombreCuenta = '$nombre'"
            db.execSQL(query)
        }
    }

    fun borrarCuentaPorIBAN(nombre: String) {
        admin.writableDatabase.use { db ->
            db.execSQL("PRAGMA foreign_keys = ON;")
            val whereClause = "nombreCuenta = ?"
            val whereArgs = arrayOf(nombre)
            db.delete("CUENTA", whereClause, whereArgs)
        }
    }

    fun cambiarIbanCuenta(nombre: String, nuevoNombre: String) {
        admin.writableDatabase.use { db ->
            db.execSQL("PRAGMA foreign_keys = ON;")

            // Actualizar el IBAN en la tabla CUENTA
            val values = ContentValues().apply {
                put("nombreCuenta", nuevoNombre)
            }
            val whereClause = "nombreCuenta = ?"
            val whereArgs = arrayOf(nombre)
            try {
                db.update("CUENTA", values, whereClause, whereArgs)
            } catch (e: SQLException) {
                // Manejo de errores al cambiar el nombre de la cuenta
            }

            // Actualizar el IBAN en otras tablas que hacen referencia a la cuenta
            val tablasReferenciadas = listOf("MOVIMIENTO")  // Agrega más tablas según sea necesario

            for (tabla in tablasReferenciadas) {
                val updateQuery = "UPDATE $tabla SET nombreCuenta = ? WHERE nombreCuenta= ?"
                val updateArgs = arrayOf(nuevoNombre, nombre)

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
            val query = "SELECT nombreCuenta, saldo FROM CUENTA"
            val cursor = db.rawQuery(query, null)
            while (cursor.moveToNext()) {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombreCuenta"))
                val saldo = cursor.getDouble(cursor.getColumnIndexOrThrow("saldo"))
                val cuenta = Cuenta(nombre, saldo)
                cuentas.add(cuenta)
            }
            cursor.close()
        }
        return cuentas
    }
}
