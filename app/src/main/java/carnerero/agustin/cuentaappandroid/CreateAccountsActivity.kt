package carnerero.agustin.cuentaappandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import carnerero.agustin.cuentaappandroid.databinding.ActivityMainBinding

class CreateAccountsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_accounts)
    }
}