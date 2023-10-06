package carnerero.agustin.cuentaappandroid

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


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View){
        val et_user:EditText=findViewById(R.id.et_text_user)
        val et_password:EditText=findViewById(R.id.et_password)
        if(et_user.text.toString()=="Agustin"){
            if(et_password.text.toString()=="nina1971"){
                val intent= Intent(this,NavActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Inicio de sesion", Toast.LENGTH_LONG).show()
            }
        }
        else{ Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
        }
    }

    fun createUser(view: View){

        val intent = Intent(this, NavActivity::class.java)
        intent.putExtra("mostrarFragment", "createUser")
        startActivity(intent)
    }
}