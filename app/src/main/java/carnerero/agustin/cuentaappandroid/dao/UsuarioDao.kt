package carnerero.agustin.cuentaappandroid.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import carnerero.agustin.cuentaappandroid.DataBaseApp
import carnerero.agustin.cuentaappandroid.model.Usuario

class UsuarioDao(context: Context) {
    private val admin: DataBaseApp = DataBaseApp(context,"cuentaApp",null,1)

    // Método para insertar un usuario en la base de datos
    fun insertarUsuario(usuario: Usuario) {
        val db = admin.writableDatabase
        val values = ContentValues()
        values.put("dni", usuario.dni)
        values.put("nombre", usuario.nombre)
        values.put("domicilio", usuario.domicilio)
        values.put("ciudad", usuario.ciudad)
        values.put("codigopostal", usuario.codigoPostal)
        values.put("telefono", usuario.telefono)
        values.put("password", usuario.password)

        try {
            db.insert("USUARIO", null, values)
        } catch (e: SQLException) {

        }

        db.close()
    }

    // Método para obtener un usuario por DNI y contraseña
    fun obtenerUsuarioPorDniYPassword(dni: String, password: String): Usuario? {
        val db = admin.readableDatabase
        val query = "SELECT dni, password FROM USUARIO WHERE dni='${dni}' AND password='${password}'"
        val cursor: Cursor = db.rawQuery(query, arrayOf(dni, password))

        var usuario: Usuario? = null

        if (cursor.moveToFirst()) {
            usuario = Usuario(
                cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                cursor.getString(cursor.getColumnIndexOrThrow("domicilio")),
                cursor.getString(cursor.getColumnIndexOrThrow("ciudad")),
                cursor.getString(cursor.getColumnIndexOrThrow("codigopostal")),
                cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                cursor.getString(cursor.getColumnIndexOrThrow("password"))
            )
        }
        cursor.close()
        db.close()
        return usuario
    }

    // Método para borrar un usuario por DNI y contraseña
    fun borrarUsuarioPorDniYPassword(dni: String, password: String) {
        val db = admin.writableDatabase
        db.delete("USUARIO", "$dni = ? AND $password = ?", arrayOf(dni, password))
        db.close()
    }
    }

