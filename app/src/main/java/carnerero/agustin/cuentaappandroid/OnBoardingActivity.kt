package carnerero.agustin.cuentaappandroid

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar el diseño de la actividad utilizando View Binding
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Obtener nombre de usuario
        // Obtener preferencias compartidas
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val userName=sharedPreferences.getString(getString(R.string.userlogin),"")
        // Obtener referencias a los componentes desde el enlace de vista (binding)
        val btnCreateProfile=binding.btnCreateprofile
        if(userName!=""){
            btnCreateProfile.text=getString(R.string.login)
        }
        // Establecer un OnClickListener para el botón con ID btn_createProfile
        btnCreateProfile.setOnClickListener {
            if (userName.toString().isEmpty()) {
                // Si userName es una cadena vacía, abrir CreateProfileActivity
                val intent = Intent(this, CreateProfileActivity::class.java)
                startActivity(intent)
            } else {
                // Si userName no es una cadena vacía, abrir LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

    }
}