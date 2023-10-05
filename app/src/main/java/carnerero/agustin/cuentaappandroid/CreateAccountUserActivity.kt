package carnerero.agustin.cuentaappandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class CreateAccountUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account_user)
    }
    fun createAccounts(view: View){
        val createUser: Button =findViewById(R.id.btn_create)
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
}