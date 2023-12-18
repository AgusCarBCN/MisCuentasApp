package carnerero.agustin.cuentaappandroid

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.databinding.FragmentAjustesBinding
import carnerero.agustin.cuentaappandroid.repo.CurrencyRepo
import carnerero.agustin.cuentaappandroid.utils.Utils
import kotlinx.coroutines.launch


class AjustesFragment : Fragment() {


    private val repo=CurrencyRepo()
    private var _binding: FragmentAjustesBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var lang: String
    private lateinit var country: String
    private lateinit var currency:String
    private var currencySelected=1
    private val darkModeIcon = R.drawable.dark_mode_20
    private val lightModeIcon = R.drawable.light_mode_20
    private val english ="en"
    private val spanish = "es"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAjustesBinding.inflate(inflater, container, false)
        val view = binding.root
        //viewModel=ViewModelProvider(this,CurrencyVmFac(repo)).get(CurrencyVm::class.java)
        // Referencias a elementos de diseño
        val switchTheme = binding.switchdark
        val imgTheme = binding.imgDarklight
        val switchLang = binding.switchen
        val langText = binding.tvEnes
        val selectCurrency = binding.selectCurrency

        // Obtener preferencias compartidas
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        // Obtener el estado actual del modo oscuro, idioma y divisa desde SharedPreferences
        val enableDarkTheme = sharedPreferences.getBoolean(getString(R.string.preferences_enable), false)
        val enableEnLang = sharedPreferences.getBoolean(getString(R.string.preferences_enable_lang), false)

        val currency = sharedPreferences.getString(getString(R.string.basecurrency),null).toString()
        currencySelected = when(currency){
            "EUR"-> sharedPreferences.getInt("lastSelectedOption", R.id.rb_euro)
            "USD"-> sharedPreferences.getInt("lastSelectedOption", R.id.rb_dolar)
            else-> sharedPreferences.getInt("lastSelectedOption", R.id.rb_pound)

        }
        // Establecer iconos según el estado actual del modo oscuro y el idioma
        setIcon(enableDarkTheme, imgTheme, lightModeIcon, darkModeIcon)
        setTextLang(enableEnLang,langText)

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
                    getConversionRate(currency,"EUR")

                }
                R.id.rb_dolar -> {
                    lang = "en"
                    country = "US"
                    getConversionRate(currency,"USD")

                }
                R.id.rb_pound -> {
                    lang = "en"
                    country = "GB"
                    getConversionRate(currency,"GBP")

                }
            }
            // Guardar la selección en SharedPreferences
            sharedPreferences.edit().putInt("lastSelectedOption", checkedId).apply()
            sharedPreferences.edit().putString(getString(R.string.lang), lang).apply()
            sharedPreferences.edit().putString(getString(R.string.country), country).apply()
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
    private fun getConversionRate(from:String,to:String){
        lifecycleScope.launch {
            if (from == to) {
                sharedPreferences.edit().putString(getString(R.string.conversion_rate), "1.0").apply ()
                Toast.makeText(
                    requireContext(),
                    "$from $to 1.0",
                    Toast.LENGTH_SHORT
                ).show()
                (activity as MainActivity).actualizarFragmentSaldo()
            } else {
                try {
                    val response = repo.getCurrency(from, to)
                    val rate = response.body()?.conversion_rate
                    Toast.makeText(
                        requireContext(),
                        "$from $to $rate",
                        Toast.LENGTH_SHORT
                    ).show()
                    sharedPreferences.edit()
                        .putString(getString(R.string.conversion_rate), rate.toString()).apply()
                    // Actualizar fragmento de saldo en la actividad principal
                    (activity as MainActivity).actualizarFragmentSaldo()
                } catch (e: Exception) {
                    // Manejar errores, como mostrar un mensaje al usuario
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
    private fun setTextLang(enable: Boolean,langText:TextView){
        if(enable){
            langText.text=spanish
        }else langText.text=english
    }


}
