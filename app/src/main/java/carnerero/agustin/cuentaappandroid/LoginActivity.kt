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

    fun login(view: View) {
        val et_user: EditText = findViewById(R.id.et_text_user)
        val et_pass: EditText = findViewById(R.id.et_password)

        // Crear una instancia de la base de datos
        val admin = DataBaseApp(this, "cuentaApp", null, 1)
        val db = admin.writableDatabase

        // Corregir la consulta SQL para usar un solo WHERE y un solo parámetro
        val rowUser = db.rawQuery("SELECT dni, password FROM USUARIO WHERE dni='${et_user.text.toString()}' AND password='${et_pass.text.toString()}'", null)

        var user = ""
        var pass = ""

        if (rowUser.moveToFirst()) {
            user = rowUser.getString(0)
            pass = rowUser.getString(1)
        }

        // Cerrar la base de datos después de usarla
        db.close()

        if (et_user.text.toString() == user && et_pass.text.toString() == pass) {
            // Inicio de sesión exitoso
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_LONG).show()

             val intent = Intent(this, NavActivity::class.java)
             startActivity(intent)
        } else {
            // Usuario o contraseña incorrectos
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
        }
    }


    fun createUser(view: View){

        val intent = Intent(this, NavActivity::class.java)
        intent.putExtra("mostrarFragment", "createUser")
        startActivity(intent)
    }
}