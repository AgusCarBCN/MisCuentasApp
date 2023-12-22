package carnerero.agustin.cuentaappandroid

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.dao.UsuarioDao
import carnerero.agustin.cuentaappandroid.databinding.ActivityLoginBinding

import carnerero.agustin.cuentaappandroid.utils.Utils
import java.util.Calendar

class LoginActivity : AppCompatActivity() {

    // Instancias de DAO y otros elementos necesarios
    private val admin = DataBaseAppSingleton.getInstance(this)
    private val movDAO = MovimientoBancarioDAO(admin)
    private val userDao = UsuarioDao(admin)
    private var existUser: Boolean = false
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización del enlace de diseño
        binding = ActivityLoginBinding.inflate(layoutInflater)


        // Obtener preferencias compartidas
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // Obtener configuraciones de preferencias compartidas
        val enableDarkTheme = sharedPreferences.getBoolean(getString(R.string.preferences_enable), false)
        val enableEnLang = sharedPreferences.getBoolean(getString(R.string.preferences_enable_lang), false)
        val name=sharedPreferences.getString(getString(R.string.username),"usuario")
        // Referencias a elementos de diseño
        val createUser = binding.btnCreateuser
        val tvcreateUser = binding.tvCreateuser
        val wellcome=binding.tvWellcome
        // Verificar si ya existe un usuario
        existUser = userDao.existeAlgunUsuario()
        // Obtener la hora actual
        val cal = Calendar.getInstance()
        val currentHour = cal.get(Calendar.HOUR_OF_DAY)
        val greeting = when (currentHour) {
            in 6..11 -> getString(R.string.goodmorning)
            in 12..20 -> getString(R.string.goodafternoon)
            else -> getString(R.string.goodevening)
        }
        val msgWellcome="$greeting $name"
        wellcome.text=msgWellcome

        // Establecer el diseño de la actividad
        setContentView(binding.root)

        // Configuración del botón y el texto según si existe un usuario
        if (existUser) {
            createUser.setText(getString(R.string.forgetpass))
            tvcreateUser.setText("")
        }


        // Aplicar tema y configuración de idioma según las preferencias
        Utils.applyTheme(enableDarkTheme)
        Utils.applyLanguage(enableEnLang)
    }

    override fun onDestroy() {

        super.onDestroy()
    }

    // Función para manejar el evento de inicio de sesión
    fun login(view: View) {
        // Obtener valores de los campos de usuario y contraseña
        val userField: String = binding.etTextUser.text.toString()
        val passwordField: String = binding.etPassword.text.toString()

        // Obtener usuario de la base de datos
        val user = userDao.obtenerUsuarioPorDniYPassword(userField, passwordField)
        var userDni = user?.dni
        var userPassword = user?.password
        var userName = user?.nombre



        // Verificar si los campos están vacíos
        if (userField.isEmpty() || passwordField.isEmpty()) {
            if (userField.isEmpty() && passwordField.isEmpty()) {
                // Mostrar mensaje si ambos campos están vacíos
                Toast.makeText(this, "${getString(R.string.msgemptiesfield)}", Toast.LENGTH_LONG).show()
            } else {
                // Mostrar mensaje si al menos un campo está vacío
                Toast.makeText(this, "${getString(R.string.msgemptyfield)}", Toast.LENGTH_LONG).show()
            }
        } else {
            // Verificar las credenciales
            if (userDni == userField && userPassword == passwordField) {
                // Almacenar información del usuario en preferencias compartidas
                sharedPreferences.edit {
                    putString(getString(R.string.userdni), userDni)
                    putString(getString(R.string.username), userName)
                    putString(getString(R.string.userpass), userPassword)
                }

                // Mostrar mensaje de éxito y abrir la actividad principal
                Toast.makeText(this, "${getString(R.string.msgsucces)}", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // Mostrar mensaje de error si las credenciales no coinciden
                Toast.makeText(this, "${getString(R.string.msgnosucces)}", Toast.LENGTH_LONG).show()
            }
        }
    }


    // Función para manejar el evento de creación de usuario
    fun createUser(view: View) {
        // Abrir la actividad correspondiente según si ya existe un usuario
        if (!existUser) {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, NewPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}
