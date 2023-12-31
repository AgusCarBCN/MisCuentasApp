package carnerero.agustin.cuentaappandroid

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import carnerero.agustin.cuentaappandroid.adapter.OnBoardingAdapter
import carnerero.agustin.cuentaappandroid.databinding.ActivityOnBoardingBinding
import carnerero.agustin.cuentaappandroid.utils.Utils

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var titleList: MutableList<String>
    private lateinit var descriptionList: MutableList<String>
    private lateinit var imgList:MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        titleList = mutableListOf(getString(R.string.title0),
            getString(R.string.title1), getString(R.string.title2), getString(R.string.title3),
            getString(R.string.title4), getString(R.string.title5)
        )
        descriptionList = mutableListOf(getString(R.string.des0),
            getString(R.string.des1), getString(R.string.des2), getString(R.string.des3),
            getString(R.string.des4), getString(R.string.des5)
        )
        imgList = mutableListOf(
            R.drawable.contabilidad,
            R.drawable.person_intro,
            R.drawable.payments_intro,
            R.drawable.barchart_intro,
            R.drawable.notifications_intro,
            R.drawable.settings_intro
        )
        super.onCreate(savedInstanceState)
        // Inflar el diseño de la actividad utilizando View Binding
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Obtener nombre de usuario
        // Obtener preferencias compartidas
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val enableDarkTheme =
            sharedPreferences.getBoolean(getString(R.string.preferences_enable), false)
        val enableEnLang =
            sharedPreferences.getBoolean(getString(R.string.preferences_enable_lang), false)
        // Aplicar tema y configuración de idioma según las preferencias
        Utils.applyLanguage(enableEnLang)
        Utils.applyTheme(enableDarkTheme)

        val userName=sharedPreferences.getString(getString(R.string.userlogin),"")
        // Obtener referencias a los componentes desde el enlace de vista (binding)
        val btnCreateProfile=binding.btnCreateprofile
        val viewPager=binding.viewPager
        val circleIndicator=binding.circleindicator

        viewPager.adapter=OnBoardingAdapter(titleList,descriptionList,imgList)
        viewPager.orientation=ViewPager2.ORIENTATION_HORIZONTAL
        circleIndicator.setViewPager(viewPager)
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