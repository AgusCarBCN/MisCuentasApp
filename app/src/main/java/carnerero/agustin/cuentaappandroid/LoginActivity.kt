package carnerero.agustin.cuentaappandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.dao.UsuarioDao
import carnerero.agustin.cuentaappandroid.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private val admin=DataBaseAppSingleton.getInstance(this)
    private val movDAO=MovimientoBancarioDAO(admin)
    private val userDao=UsuarioDao(admin)
    private lateinit var binding: ActivityLoginBinding
    private lateinit var dni:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        val createUser=binding.btnCreateuser
        val tvcreateUser=binding.tvCreateuser
        setContentView(binding.root)
        if(userDao.existeAlgunUsuario()){
            createUser.visibility=View.INVISIBLE
             tvcreateUser.setText("")
        }

    }
    fun login(view: View) {

        val userDni:String=binding.etTextUser.text.toString()
        val userPassword:String=binding.etPassword.text.toString()
        val user=userDao.obtenerUsuarioPorDniYPassword(userDni,userPassword)
        var usuarioLogin=user?.dni
        var passwordLogin=user?.password
        var usuarioName=user?.nombre

// Verifica si los campos están vacíos
        if (userDni.isEmpty() || userPassword.isEmpty()) {
            if (userDni.isEmpty() && userPassword.isEmpty()) {
                Toast.makeText(this, "${getString(R.string.msgemptiesfield)}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "${getString(R.string.msgemptyfield)}", Toast.LENGTH_LONG).show()
            }
        } else {
            // Verifica las credenciales
            if (userDni == usuarioLogin && userPassword == passwordLogin) {
                // Inicio de sesión exitoso
                // Guardar el DNI en SharedPreferences después del inicio de sesión
                //Las SharedPreferences son una forma de almacenar datos clave-valor en Android
                //de manera sencilla y eficiente
                dni = usuarioLogin
                val nombre=usuarioName
                //Guarda dni y nombre en sharedpreferences de Android
                val sharedPreferences = getSharedPreferences("dataLogin", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("dni", dni)
                editor.putString("nombre",nombre)
                editor.apply()
                Toast.makeText(this, "${getString(R.string.msgsucces)}", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this, "${getString(R.string.msgnosucces)}", Toast.LENGTH_LONG).show()
            }
        }
    }

        fun createUser(view: View) {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }
    }
