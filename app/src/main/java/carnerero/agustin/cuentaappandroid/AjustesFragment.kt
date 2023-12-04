package carnerero.agustin.cuentaappandroid

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.databinding.FragmentAjustesBinding
import carnerero.agustin.cuentaappandroid.utils.Utils


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
    // Variables de instancia
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAjustesBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var lang: String
    private lateinit var country: String
    private val darkModeIcon = R.drawable.ic_dark_mode
    private val lightModeIcon = R.drawable.light_mode_24
    private val englishIcon = R.drawable.language_gb_english
    private val spanishIcon = R.drawable.language_spanish

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
        _binding = FragmentAjustesBinding.inflate(inflater, container, false)
        val view = binding.root

        // Referencias a elementos de diseño
        val switchTheme = binding.switchdark
        val imgTheme = binding.imgDarklight
        val switchLang = binding.switchen
        val imgLang = binding.imgEnes
        val selectCurrency = binding.selectCurrency

        // Obtener preferencias compartidas
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        // Obtener el estado actual del modo oscuro, idioma y divisa desde SharedPreferences
        val enableDarkTheme = sharedPreferences.getBoolean(getString(R.string.preferences_enable), false)
        val enableEnLang = sharedPreferences.getBoolean(getString(R.string.preferences_enable_lang), false)
        val currencySelected = sharedPreferences.getInt("lastSelectedOption", R.id.rb_euro)

        // Establecer iconos según el estado actual del modo oscuro y el idioma
        setIcon(enableDarkTheme, imgTheme, lightModeIcon, darkModeIcon)
        setIcon(enableEnLang, imgLang, spanishIcon, englishIcon)

        // Establecer el estado inicial del Switch y el radioGroup
        switchTheme.isChecked = enableDarkTheme
        switchLang.isChecked = enableEnLang
        selectCurrency.check(currencySelected)

        // Manejar cambios en el Switch de modo oscuro
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            Utils.applyTheme(isChecked)
            sharedPreferences.edit().putBoolean(getString(R.string.preferences_enable), isChecked).apply()
        }

        // Manejar cambios en el Switch de idioma
        switchLang.setOnCheckedChangeListener { _, isChecked ->
            Utils.applyLanguage(isChecked)
            sharedPreferences.edit().putBoolean(getString(R.string.preferences_enable_lang), isChecked).apply()
        }

        // Manejar cambios en la selección de divisa
        selectCurrency.setOnCheckedChangeListener { _, checkedId ->
            // Asignar valores de idioma y país según la selección
            when (checkedId) {
                R.id.rb_euro -> {
                    lang = "es"
                    country = "ES"
                }
                R.id.rb_dolar -> {
                    lang = "en"
                    country = "US"
                }
                R.id.rb_pound -> {
                    lang = "en"
                    country = "GB"
                }
            }

            // Guardar la selección en SharedPreferences
            sharedPreferences.edit().putInt("lastSelectedOption", checkedId).apply()
            sharedPreferences.edit().putString(getString(R.string.lang), lang).apply()
            sharedPreferences.edit().putString(getString(R.string.country), country).apply()



            // Actualizar fragmento de saldo en la actividad principal
            (activity as MainActivity).actualizarFragmentSaldo()
        }

        return view
    }

    // Función para establecer un icono según el estado de una característica
    private fun setIcon(enable: Boolean, icon: ImageView, iconEnable: Int, iconDisable: Int) {
        if (enable) {
            icon.setImageResource(iconEnable)
        } else {
            icon.setImageResource(iconDisable)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AjustesFragment.
         */
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
