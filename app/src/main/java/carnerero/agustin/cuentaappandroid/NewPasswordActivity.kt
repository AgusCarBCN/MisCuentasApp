package carnerero.agustin.cuentaappandroid

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import androidx.transition.Visibility
import carnerero.agustin.cuentaappandroid.R.color.lightblue
import carnerero.agustin.cuentaappandroid.dao.UsuarioDao
import carnerero.agustin.cuentaappandroid.databinding.ActivityLoginBinding
import carnerero.agustin.cuentaappandroid.databinding.ActivityNewPasswordBinding
import carnerero.agustin.cuentaappandroid.utils.Utils

class NewPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPasswordBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val admin = DataBaseAppSingleton.getInstance(this)
    private val userDao = UsuarioDao(admin)
    private lateinit var dni: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        setContentView(binding.root)

        //Referencia de los componentes
        val et_userDni = binding.etDniuser
        val et_newPass = binding.etNewpassword
        val et_repeatPass = binding.etRepeatpassword
        val btn_confirmUser = binding.confirmdni
        val btn_confirmNewPass = binding.confirmnewpass

        //Recuperacion dni del usuario que inicio sesion
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        dni = sharedPreferences.getString(getString(R.string.id), null).toString()
        //Inicializamos componenetes con fondo azul claro para indicar que estan deshabilitados
        et_newPass.isEnabled = false
        et_repeatPass.isEnabled = false
        btn_confirmNewPass.visibility=View.INVISIBLE
        btn_confirmUser.setOnClickListener {
            Utils.sound(this)
            if (et_userDni.text.toString().equals(dni)) {
                et_newPass.isEnabled = true
                et_repeatPass.isEnabled = true
                btn_confirmNewPass.visibility=View.VISIBLE
                btn_confirmUser.visibility=View.INVISIBLE
                et_userDni.isEnabled=false

            } else {
                Toast.makeText(this, getString(R.string.msgnosucces), Toast.LENGTH_LONG).show()
            }
        }
        btn_confirmNewPass.setOnClickListener {
            Utils.sound(this)
            val newpass = et_newPass.text.toString()
            val repeatPass = et_repeatPass.text.toString()
            if (newpass.isEmpty() || repeatPass.isEmpty()) {
                Toast.makeText(this, getString(R.string.msgemptiesfield), Toast.LENGTH_LONG)
                    .show()
            } else {
                if (newpass == repeatPass) {
                    userDao.actualizarPassword(dni, newpass)
                    Toast.makeText(this, getString(R.string.successnewpass), Toast.LENGTH_LONG)
                        .show()
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
