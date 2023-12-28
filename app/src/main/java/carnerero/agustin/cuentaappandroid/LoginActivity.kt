package carnerero.agustin.cuentaappandroid

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.ActivityLoginBinding

import carnerero.agustin.cuentaappandroid.utils.Utils
import java.util.Calendar

class LoginActivity : AppCompatActivity() {

    // Instancias de DAO y otros elementos necesarios
    private val admin = DataBaseAppSingleton.getInstance(this)
    private val movDAO = MovimientoBancarioDAO(admin)
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización del enlace de diseño
        binding = ActivityLoginBinding.inflate(layoutInflater)
        //Obtener componentes
        val btn_Login=binding.login
        val btn_forgetPassword=binding.btnNewpassword


        // Obtener preferencias compartidas
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // Obtener configuraciones de preferencias compartidas
        val enableDarkTheme = sharedPreferences.getBoolean(getString(R.string.preferences_enable), false)
        val enableEnLang = sharedPreferences.getBoolean(getString(R.string.preferences_enable_lang), false)
        // Obtener nombre,nombre de usuario y contraseña
        val name=sharedPreferences.getString(getString(R.string.username),"usuario")
        val userName=sharedPreferences.getString(getString(R.string.userlogin),"")
        val password=sharedPreferences.getString(getString(R.string.userpass),"")

        val wellcome=binding.tvWellcome

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
        // Aplicar tema y configuración de idioma según las preferencias
        Utils.applyTheme(enableDarkTheme)
        Utils.applyLanguage(enableEnLang)

        // Establecer el diseño de la actividad
        setContentView(binding.root)

        btn_Login.setOnClickListener {
            val userField: String = binding.etTextUser.text.toString()
            val passwordField: String = binding.etPassword.text.toString()
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
                if (userName == userField && password== passwordField) {
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
        btn_forgetPassword.setOnClickListener {
            val intent = Intent(this, NewPasswordActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onDestroy() {

        super.onDestroy()
    }

}
