package carnerero.agustin.cuentaappandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class CreateUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }

    fun continuar(view: View){
        val createUser: Button =findViewById(R.id.btn_continue)
        val intent= Intent(this,CreateAccountUserActivity::class.java)
        startActivity(intent)
    }
}