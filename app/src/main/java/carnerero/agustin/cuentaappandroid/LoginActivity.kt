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


class LoginActivity : AppCompatActivity() {

    private val admin=DataBaseAppSingleton.getInstance(this)
    private val movDAO=MovimientoBancarioDAO(admin)
    private val userDao = UsuarioDao(admin)
    private var existUser: Boolean = false
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        val createUser=binding.btnCreateuser
        val tvcreateUser=binding.tvCreateuser

        existUser = userDao.existeAlgunUsuario()
        setContentView(binding.root)
        if (existUser) {
            createUser.setText(getString(R.string.forgetpass))
            tvcreateUser.setText("")
        }
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this)
        val enableDarkTheme = sharedPreferences.getBoolean(getString(R.string.preferences_enable), false)
        applyTheme(enableDarkTheme)
    }
    override fun onDestroy() {
        Utils.releaseSound()
        super.onDestroy()
    }

    fun login(view: View) {

        val userField:String=binding.etTextUser.text.toString()
        val passwordField:String=binding.etPassword.text.toString()
        val user=userDao.obtenerUsuarioPorDniYPassword(userField,passwordField)
        var userDni=user?.dni
        var userPassword=user?.password
        var userName=user?.nombre
        //Sonido al clickear boton
        Utils.sound(this)
// Verifica si los campos están vacíos
        if (userField.isEmpty() || passwordField.isEmpty()) {
            if (userField.isEmpty() && passwordField.isEmpty()) {
                Toast.makeText(this, "${getString(R.string.msgemptiesfield)}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "${getString(R.string.msgemptyfield)}", Toast.LENGTH_LONG).show()
            }
        } else {
            // Verifica las credenciales
            if (userDni == userField && userPassword ==passwordField) {

                sharedPreferences.edit {
                    putString(getString(R.string.id),userDni)
                    putString(getString(R.string.name),userName)
                    putString(getString(R.string.password),userPassword)
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
        if (!existUser) {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, NewPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    fun applyTheme(enableDarkTheme: Boolean) {
        if (enableDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }
}
