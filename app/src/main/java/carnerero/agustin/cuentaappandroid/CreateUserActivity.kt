package carnerero.agustin.cuentaappandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class CreateUserActivity : AppCompatActivity() {
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
        //Acceso a botones
        val cancel: Button =findViewById(R.id.btn_canceluser)
        val confirm: Button =findViewById(R.id.btn_confirmuser)
        //Acceso a los editText de Usuario
        val name: EditText =findViewById(R.id.et_username)
        val dni: EditText =findViewById(R.id.et_userdni)
        val address: EditText =findViewById(R.id.et_useraddress)
        val city: EditText =findViewById(R.id.et_usercity)
        val zipCode: EditText =findViewById(R.id.et_userzip)
        val phone: EditText =findViewById(R.id.et_userphone)
        val userpass: EditText =findViewById(R.id.et_userpass)
        //Acceso a los ediText de las cuentas
        val mainAccount: EditText =findViewById(R.id.et_useraccmain)
        val mainAmount: EditText =findViewById(R.id.et_amountaccmain)
        val secondaryAccount: EditText =findViewById(R.id.et_useraccsecond)
        val secondaryAmount: EditText =findViewById(R.id.et_useraccsecond)

        /*Logica para cuentaDao y usuarioDao insertarreg
        //Se crea la base de datos
            val admin=DataBaseApp(context,"cuentaApp",null,1)
            //obtengo una base de datos en la que puedo agregar datos
            val db=admin.writableDatabase
            //Creo registro, a traves de la cual envio datos
            val reg=ContentValues()
            reg.put("dni",dni.text.toString())
            reg.put("nombre",name.text.toString())
            reg.put("domicilio",address.text.toString())
            reg.put("ciudad",city.text.toString())
            reg.put("codigopostal",zipCode.text.toString())
            reg.put("telefono",phone.text.toString())
            reg.put("password",userpass.text.toString())
            //se inserta registro a tabla usuario
            db.insert("USUARIO",null,reg)
            val regAccount1=ContentValues()
            regAccount1.put("iban",mainAccount.text.toString())
            regAccount1.put("saldo",mainAmount.text.toString().toDouble())
            regAccount1.put("dni",dni.text.toString())
            db.insert("CUENTA",null,regAccount1)
            val regAccount2=ContentValues()
            regAccount2.put("iban",secondaryAccount.text.toString())
            regAccount2.put("saldo",secondaryAmount.text.toString().toDouble())
            regAccount2.put("dni",dni.text.toString())
            db.insert("CUENTA",null,regAccount2)
            db.close()

         */
        startActivity(intent)
    }
}