package carnerero.agustin.cuentaappandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.dao.UsuarioDao
import java.io.BufferedReader
import java.io.InputStreamReader


class LoginActivity : AppCompatActivity() {

    private val admin=DataBaseAppSingleton.getInstance(this)
    private val movDAO=MovimientoBancarioDAO(admin)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    fun login(view: View) {
        val et_user: EditText = findViewById(R.id.et_text_user)
        val et_pass: EditText = findViewById(R.id.et_password)
        val userDni:String=et_user.text.toString()
        val userPassword:String=et_pass.text.toString()
        val userDao=UsuarioDao(admin)
        val user=userDao.obtenerUsuarioPorDniYPassword(userDni,userPassword)
        var usuarioLogin=user?.dni
        var passwordLogin=user?.password
        var usuarioName=user?.nombre

// Verifica si los campos están vacíos
        if (userDni.isEmpty() || userPassword.isEmpty()) {
            if (userDni.isEmpty() && userPassword.isEmpty()) {
                Toast.makeText(this, "Campos vacíos", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Has dejado un campo vacío", Toast.LENGTH_LONG).show()
            }
        } else {
            // Verifica las credenciales
            if (userDni == usuarioLogin && userPassword == passwordLogin) {
                // Inicio de sesión exitoso
                // Guardar el DNI en SharedPreferences después del inicio de sesión
                //Las SharedPreferences son una forma de almacenar datos clave-valor en Android
                //de manera sencilla y eficiente
                val dni = usuarioLogin
                val nombre=usuarioName
                //Guarda dni y nombre en sharedpreferences de Android
                val sharedPreferences = getSharedPreferences("dataLogin", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("dni", dni)
                editor.putString("nombre",nombre)
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
