package carnerero.agustin.cuentaappandroid.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.SQLException
import carnerero.agustin.cuentaappandroid.DataBaseApp
import carnerero.agustin.cuentaappandroid.model.Usuario

class UsuarioDao(private val admin: DataBaseApp) {


    // Método para insertar un usuario en la base de datos
    fun insertarUsuario(usuario: Usuario) {
        val db = admin.writableDatabase
        val values = ContentValues()
        values.put("dni", usuario.dni)
        values.put("nombre", usuario.nombre)
        values.put("domicilio", usuario.domicilio)
        values.put("ciudad", usuario.ciudad)
        values.put("codigopostal", usuario.codigoPostal)
        values.put("email", usuario.email)
        values.put("password", usuario.password)
        try {
            db.insert("USUARIO", null, values)
        } catch (_: SQLException) {

        }
        db.close()
    }

    /* Método para obtener un usuario por DNI y contraseña
    *Estos cambios mejoran la seguridad de la consulta y hacen que el código sea
    * más limpio y legible. Además, protege contra la inyección SQL al usar consultas
    *  parametrizadas en lugar de concatenar directamente los valores en la consulta. */
    fun obtenerUsuarioPorDniYPassword(dni: String, password: String): Usuario? {
        val db = admin.readableDatabase
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
            db.close()
        }

        return usuario
    }
    // Función para verificar si existe algún usuario en la base de datos
    fun existeAlgunUsuario(): Boolean {
        val db = admin.readableDatabase
        val query = "SELECT COUNT(*) FROM USUARIO"
        var cursor: Cursor?=null

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
            db.close()
        }

        return false
    }

    // Método para borrar un usuario por DNI y contraseña
    /*fun borrarUsuarioPorDniYPassword(dni: String, password: String) {
        val db = admin.writableDatabase
        db.delete("USUARIO", "$dni = ? AND $password = ?", arrayOf(dni, password))
        db.close()
    }*/
    }

