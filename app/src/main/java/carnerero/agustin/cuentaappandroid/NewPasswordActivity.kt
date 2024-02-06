package carnerero.agustin.cuentaappandroid

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.databinding.ActivityNewPasswordBinding

class NewPasswordActivity : AppCompatActivity() {
    // View Binding para acceder a los componentes de la interfaz de usuario
    private lateinit var binding: ActivityNewPasswordBinding
    // SharedPreferences para almacenar y recuperar datos de forma sencilla
    private lateinit var sharedPreferences: SharedPreferences
    // Variable para almacenar el DNI del usuario que inició sesión
    private lateinit var login: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el diseño de la actividad utilizando View Binding
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        setContentView(binding.root)

        // Referencia de los componentes de la interfaz
        val etUserName = binding.etDniuser
        val etNewPass = binding.etNewpassword
        val etRepeatPass = binding.etRepeatpassword
        val btnConfirmUser = binding.confirmdni
        val btnConfirmNewPass = binding.confirmnewpass
        val btnGoBackLogin=binding.backtoLogin
        // Recuperar el DNI del usuario que inició sesión
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        login = sharedPreferences.getString(getString(R.string.userlogin), null).toString()

        // Inicializar componentes con fondo azul claro para indicar que están deshabilitados
        etNewPass.isEnabled = false
        etRepeatPass.isEnabled = false
        btnConfirmNewPass.visibility = View.GONE

        // Acciones a realizar cuando se hace clic en el botón de confirmar usuario
        btnConfirmUser.setOnClickListener {

            if (etUserName.text.toString() == login) {
                etNewPass.isEnabled = true
                etRepeatPass.isEnabled = true
                btnConfirmNewPass.visibility = View.VISIBLE
                btnConfirmUser.visibility = View.GONE
                etUserName.isEnabled = false
            } else {
                Toast.makeText(this, getString(R.string.msgnosucces), Toast.LENGTH_LONG).show()
            }
        }

        // Acciones a realizar cuando se hace clic en el botón de confirmar nueva contraseña
        btnConfirmNewPass.setOnClickListener {

            val newpass = etNewPass.text.toString()
            val repeatPass = etRepeatPass.text.toString()

            // Verificar que los campos de contraseña no estén vacíos
            if (newpass.isEmpty() || repeatPass.isEmpty()) {
                Toast.makeText(this, getString(R.string.msgemptiesfield), Toast.LENGTH_LONG)
                    .show()
            } else {
                // Verificar que las contraseñas coincidan
                if (newpass == repeatPass) {
                    // Actualizar la contraseña en la base de datos
                    //Guadar moneda en sharepreferences
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
                    sharedPreferences.edit().putString(
                        getString(R.string.userpass),
                        etNewPass.text.toString()
                    ).apply()
                    Toast.makeText(this, getString(R.string.successnewpass), Toast.LENGTH_LONG)
                        .show()
                    // Redirigir al usuario a la pantalla de inicio de sesión
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, getString(R.string.passnotequals), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        btnGoBackLogin?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
    }
}
