package carnerero.agustin.cuentaappandroid

import android.content.SharedPreferences
import android.media.VolumeShaper
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.databinding.FragmentAjustesBinding
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AjustesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AjustesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAjustesBinding?=null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private val darkModeIcon=R.drawable.ic_dark_mode
    private val lightModeIcon=R.drawable.ic_light_mode
    private val englishIcon=R.drawable.english
    private val spanishIcon=R.drawable.spanish

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentAjustesBinding.inflate(inflater,container,false)
        val view = binding.root
        val switchTheme= binding.switchdark
        val imgTheme=binding.imgDarklight
        val switchLang=binding.switchen
        val imgLang=binding.imgEnes

        //Recupero dni del usuario que inicio sesion
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(requireContext())
        // Obtiene el estado actual del modo oscuro desde SharedPreferences
        val enableDarkTheme = sharedPreferences.getBoolean(getString(R.string.preferences_enable), false)
        val enableEnLang=sharedPreferences.getBoolean(getString(R.string.preferences_enable_lang), false)
        if(enableDarkTheme){
            imgTheme.setImageResource(lightModeIcon)
            imgTheme.setColorFilter(ContextCompat.getColor(requireContext(), R.color.lightOrange))

        }else{
            imgTheme.setImageResource(darkModeIcon)
            imgTheme.setColorFilter(ContextCompat.getColor(requireContext(), R.color.darkBrown))

        }
        if(enableEnLang){
            imgLang.setImageResource(spanishIcon)
        }else{
            imgLang.setImageResource(englishIcon)
        }
        // Establece el estado inicial del Switch
        switchTheme.isChecked = enableDarkTheme
        switchLang.isChecked=enableEnLang
        applyTheme(enableDarkTheme)
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            applyTheme(isChecked)
        }
        switchLang.setOnCheckedChangeListener { _, isChecked ->
            applyLanguage(isChecked)
            if(isChecked){
                imgLang.setImageResource(spanishIcon)
            }else{
                imgLang.setImageResource(englishIcon)
            }
        }
        return view
    }
    override fun onPause() {
        super.onPause()
        // Guarda el estado del Switch en SharedPreferences cuando la actividad se pausa
        val switchTheme: SwitchCompat = binding.switchdark
        val switchLang=binding.switchen
        sharedPreferences.edit().putBoolean(getString(R.string.preferences_enable), switchTheme.isChecked).apply()
        sharedPreferences.edit().putBoolean(getString(R.string.preferences_enable_lang), switchTheme.isChecked).apply()
    }

    override fun onResume() {

        super.onResume()
    }
    private fun applyTheme(enableDarkTheme: Boolean) {
        if (enableDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }
    private fun applyLanguage(enableEnLang: Boolean) {

        if (enableEnLang) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("es"))
        }



    }

    // ... (resto del código)

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AjustesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AjustesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}