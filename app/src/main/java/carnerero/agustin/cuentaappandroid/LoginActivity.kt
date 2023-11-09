package carnerero.agustin.cuentaappandroid

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.dao.UsuarioDao
import carnerero.agustin.cuentaappandroid.databinding.ActivityLoginBinding
import carnerero.agustin.cuentaappandroid.utils.Utils


class LoginActivity : AppCompatActivity() {

    private val admin=DataBaseAppSingleton.getInstance(this)
    private val movDAO=MovimientoBancarioDAO(admin)
    private val userDao=UsuarioDao(admin)
    private lateinit var binding: ActivityLoginBinding

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
    override fun onDestroy() {
        Utils.releaseSound()
        super.onDestroy()
    }

    fun login(view: View) {

        val userDni:String=binding.etTextUser.text.toString()
        val userPassword:String=binding.etPassword.text.toString()
        val user=userDao.obtenerUsuarioPorDniYPassword(userDni,userPassword)
        var usuarioLogin=user?.dni
        var passwordLogin=user?.password
        var usuarioName=user?.nombre
        //Sonido al clickear boton
        Utils.sound(this)
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
                //Guarda dni y nombre en sharedpreferences de Android
                val sharedPreferences = getSharedPreferences("dataLogin", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                with(editor) {
                putString("dni", usuarioLogin)
                putString("nombre", usuarioName)
                apply()
            }
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
