package carnerero.agustin.cuentaappandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.UsuarioDao
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.model.Usuario


class CreateUserActivity : AppCompatActivity() {
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var cuentaDao: CuentaDao
    private val admin=DataBaseAppSingleton.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)


    }

    fun cancelCreateUser(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun confirmCreateUser(view: View) {

        val intent = Intent(this, LoginActivity::class.java)
        //Acceso a los editText de Usuario
        val name: EditText = findViewById(R.id.et_username)
        val dni: EditText = findViewById(R.id.et_userdni)
        val address: EditText = findViewById(R.id.et_useraddress)
        val city: EditText = findViewById(R.id.et_usercity)
        val zipCode: EditText = findViewById(R.id.et_userzip)
        val phone: EditText = findViewById(R.id.et_userphone)
        val userpass: EditText = findViewById(R.id.et_userpass)
        //Acceso a los ediText de las cuentas
        val mainAccount: EditText = findViewById(R.id.et_useraccmain)
        val mainAmount: EditText = findViewById(R.id.et_amountaccmain)
        val secondaryAccount: EditText = findViewById(R.id.et_useraccsecond)
        val secondaryAmount: EditText = findViewById(R.id.et_secondccamount)
        //Creo objetos Usuario y Cuenta
        val user = Usuario(
            dni.text.toString(),
            name.text.toString(),
            address.text.toString(),
            city.text.toString(),
            zipCode.text.toString(),
            phone.text.toString(),
            userpass.text.toString()
        )
        val cuenta1 = Cuenta(
            mainAccount.text.toString(),
            mainAmount.text.toString().toDouble(),
            dni.text.toString()
        )
        val cuenta2 = Cuenta(
            secondaryAccount.text.toString(),
            secondaryAmount.text.toString().toDouble(),
            dni.text.toString()
        )
        usuarioDao=UsuarioDao(admin)
        cuentaDao=CuentaDao(admin)
        usuarioDao.insertarUsuario(user)
        cuentaDao.insertarCuenta(cuenta1)
        cuentaDao.insertarCuenta(cuenta2)

        startActivity(intent)
    }




}