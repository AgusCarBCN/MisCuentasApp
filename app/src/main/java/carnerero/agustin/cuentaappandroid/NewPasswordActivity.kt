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

    // Instancias necesarias para acceder a la base de datos y realizar operaciones
    private val admin = DataBaseAppSingleton.getInstance(this)


    // Variable para almacenar el DNI del usuario que inició sesión
    private lateinit var login: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el diseño de la actividad utilizando View Binding
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        setContentView(binding.root)

        // Referencia de los componentes de la interfaz
        val et_userDni = binding.etDniuser
        val et_newPass = binding.etNewpassword
        val et_repeatPass = binding.etRepeatpassword
        val btn_confirmUser = binding.confirmdni
        val btn_confirmNewPass = binding.confirmnewpass

        // Recuperar el DNI del usuario que inició sesión
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        login = sharedPreferences.getString(getString(R.string.userlogin), null).toString()

        // Inicializar componentes con fondo azul claro para indicar que están deshabilitados
        et_newPass.isEnabled = false
        et_repeatPass.isEnabled = false
        btn_confirmNewPass.visibility = View.INVISIBLE

        // Acciones a realizar cuando se hace clic en el botón de confirmar usuario
        btn_confirmUser.setOnClickListener {

            if (et_userDni.text.toString() == login) {
                et_newPass.isEnabled = true
                et_repeatPass.isEnabled = true
                btn_confirmNewPass.visibility = View.VISIBLE
                btn_confirmUser.visibility = View.GONE
                et_userDni.isEnabled = false
            } else {
                Toast.makeText(this, "${getString(R.string.msgnosucces)}" , Toast.LENGTH_LONG).show()
            }
        }

        // Acciones a realizar cuando se hace clic en el botón de confirmar nueva contraseña
        btn_confirmNewPass.setOnClickListener {

            val newpass = et_newPass.text.toString()
            val repeatPass = et_repeatPass.text.toString()

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
                        et_newPass.text.toString()
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
    }
}
