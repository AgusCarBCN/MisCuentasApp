package carnerero.agustin.cuentaappandroid.dao

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.database.Cursor
import android.database.SQLException
import android.util.Log
import carnerero.agustin.cuentaappandroid.DataBaseApp
import carnerero.agustin.cuentaappandroid.model.Usuario

class UsuarioDao(private val admin: DataBaseApp) {

    // Método para insertar un usuario en la base de datos
    fun insertarUsuario(usuario: Usuario) {
        admin.writableDatabase.use { db ->
            val values = ContentValues().apply {
                put("dni", usuario.dni)
                put("nombre", usuario.nombre)
                put("domicilio", usuario.domicilio)
                put("ciudad", usuario.ciudad)
                put("codigopostal", usuario.codigoPostal)
                put("email", usuario.email)
                put("password", usuario.password)
            }

            try {
                db.insert("USUARIO", null, values)
            } catch (_: SQLException) {
                // Manejo de errores al insertar el usuario
            }
        }
    }

    /* Método para obtener un usuario por DNI y contraseña
    * Estos cambios mejoran la seguridad de la consulta y hacen que el código sea
    * más limpio y legible. Además, protege contra la inyección SQL al usar consultas
    * parametrizadas en lugar de concatenar directamente los valores en la consulta. */
    fun obtenerUsuarioPorDniYPassword(dni: String, password: String): Usuario? {
        admin.readableDatabase.use { db ->
            val query = "SELECT dni, nombre, domicilio, ciudad, codigopostal, email, password FROM USUARIO WHERE dni = ? AND password = ?"
            val cursor = db.rawQuery(query, arrayOf(dni, password))

            var usuario: Usuario? = null

            try {
                if (cursor.moveToFirst()) {
                    usuario = Usuario(
                        cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        cursor.getString(cursor.getColumnIndexOrThrow("domicilio")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ciudad")),
                        cursor.getString(cursor.getColumnIndexOrThrow("codigopostal")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password"))
                    )
                }
            } finally {
                cursor.close()
            }

            return usuario
        }
    }

    // Función para verificar si existe algún usuario en la base de datos
    fun existeAlgunUsuario(): Boolean {
        admin.readableDatabase.use { db ->
            val query = "SELECT COUNT(*) FROM USUARIO"
            var cursor: Cursor? = null

            try {
                cursor = db.rawQuery(query, null)
                if (cursor.moveToFirst()) {
                    val count = cursor.getInt(0)
                    return count > 0
                }
            } catch (e: SQLException) {
                // Manejar cualquier excepción que pueda ocurrir al ejecutar la consulta.
            } finally {
                cursor?.close()
            }

            return false
        }
    }

    // Método para actualizar un campo de usuario por DNI
    fun updateUserField(dni: String, columnName: String, newValue: String): Boolean {
        admin.writableDatabase.use { db ->
            /* Las restricciones de clave externa están desactivadas por defecto
            (por compatibilidad con versiones anteriores), por lo que deben habilitarse
            por separado para cada conexión a la base de datos */
            db.execSQL("PRAGMA foreign_keys = ON;")
            val contentValues = ContentValues()

            try {
                contentValues.put(columnName, newValue)
                val affectedRows = db.update("USUARIO", contentValues, "dni=?", arrayOf(dni))

                return affectedRows > 0
            } catch (e: Exception) {
                Log.e(TAG, "Error updating user field: ${e.message}")
            }
        }

        return false
    }

    // Método para actualizar la contraseña de un usuario por DNI
    fun actualizarPassword(dni: String, newPassword: String) {
        admin.writableDatabase.use { db ->
            db.execSQL("PRAGMA foreign_keys = ON;")
            val values = ContentValues()
            values.put("password", newPassword)

            try {
                db.update("USUARIO", values, "dni = ?", arrayOf(dni))
            } catch (_: SQLException) {
                // Manejar cualquier excepción que pueda ocurrir al ejecutar la actualización.
            }
        }
    }

    // Método para borrar un usuario por DNI
    fun borrarUsuarioPorDni(dni: String) {
        admin.writableDatabase.use { db ->
            db.delete("USUARIO", "dni = ?", arrayOf(dni))
        }
    }
}
