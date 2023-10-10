package carnerero.agustin.cuentaappandroid

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import carnerero.agustin.cuentaappandroid.dao.UsuarioDao
import carnerero.agustin.cuentaappandroid.model.Usuario


class LoginActivity : AppCompatActivity() {

    private val admin=DataBaseApp(this,"cuentaApp",null,1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {
        val et_user: EditText = findViewById(R.id.et_text_user)
        val et_pass: EditText = findViewById(R.id.et_password)
        val userDni:String=et_user.text.toString()
        val userPassword:String=et_pass.text.toString()

        // Crear una instancia de la base de datos
        //val admin = DataBaseApp(this, "cuentaApp", null, 1)
               val db = admin.writableDatabase

        // Corregir la consulta SQL para usar un solo WHERE y un solo parámetro
        val rowUser = db.rawQuery(
            "SELECT dni, password FROM USUARIO WHERE dni='${userDni}' AND password='${userPassword}'",
            null
        )

        var user = ""
        var pass = ""
        if (rowUser.moveToFirst()) {
            user = rowUser.getString(0)
            pass = rowUser.getString(1)
        }

        // Cerrar la base de datos después de usarla
        db.close()
// Obtén los valores de los campos de usuario y contraseña
        val username = et_user.text.toString()
        val password = et_pass.text.toString()

// Verifica si los campos están vacíos
        if (userDni.isEmpty() || userPassword.isEmpty()) {
            if (userDni.isEmpty() && userPassword.isEmpty()) {
                Toast.makeText(this, "Campos vacíos", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Has dejado un campo vacío", Toast.LENGTH_LONG).show()
            }
        } else {
            // Verifica las credenciales
            if (username == user && userPassword == pass) {
                // Inicio de sesión exitoso
                // Guardar el DNI en SharedPreferences después del inicio de sesión
                val dni = username
                val sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("dni", dni)
                editor.apply()

                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_LONG).show()

                val intent = Intent(this, NavActivity::class.java)

                startActivity(intent)
            } else {
                Toast.makeText(this, "Inicio de sesion incorrecto", Toast.LENGTH_LONG).show()
            }
        }
    }

        fun createUser(view: View) {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }
    }
