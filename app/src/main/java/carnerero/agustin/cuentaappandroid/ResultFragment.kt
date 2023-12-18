package carnerero.agustin.cuentaappandroid


import android.content.SharedPreferences
import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.databinding.FragmentResultBinding
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.util.Locale
import kotlin.math.abs



class ResultFragment : Fragment() {

    // View Binding para acceder a los componentes de la interfaz de usuario
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    // SharedPreferences para almacenar y recuperar datos de forma sencilla
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var currency:String
    private lateinit var lang:String
    private lateinit var country:String


    // Método llamado cuando se crea el fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperar los argumentos proporcionados al crear la instancia del fragmento
        arguments?.let {

        }
    }

    // Método llamado para crear la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el diseño de fragment_result.xml utilizando View Binding
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val view = binding.root

        // Referencia al TextView en el diseño
        val resultView = binding.result

        // Acceder a SharedPreferences para obtener configuraciones de idioma y país
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        currency=sharedPreferences.getString(getString(R.string.basecurrency), null)?:"EUR"
        when(currency){
            "EUR"->{
                lang=sharedPreferences.getString(getString(R.string.lang), null)?:"es"
                country=sharedPreferences.getString(getString(R.string.country), null)?:"ES"
            }
            "USD"->{
                lang=sharedPreferences.getString(getString(R.string.lang), null)?:"en"
                country=sharedPreferences.getString(getString(R.string.country), null)?:"US"
            }else->{
            lang=sharedPreferences.getString(getString(R.string.lang), null)?:"en"
            country=sharedPreferences.getString(getString(R.string.country), null)?:"GB"
        }
        }

        val rateConversion=sharedPreferences.getString(getString(R.string.conversion_rate), "1.0")
        // Establecer la Locale para el formato en euros
        val euroLocale = Locale(lang, country)

        // Obtener una instancia de NumberFormat para el formato de moneda en euros
        val currencyFormat = NumberFormat.getCurrencyInstance(euroLocale)

        // Sacar el valor double de los argumentos
        val result: Double = arguments?.getDouble("Result")!!*rateConversion.toString().toDouble()

        // Dar formato al resultado como valor absoluto y con formato de moneda euro
        val resultFormatted = currencyFormat.format(abs(result))

        // Usar String.format para mostrar solo dos decimales y con la divisa del euro

        /* La función run se utiliza para ejecutar un bloque de código en el contexto del objeto receptor.
        *  Actualiza el texto de la vista resultView y cambia su color en función del valor de result.
        *  Si result es negativo, el color del texto se establece en rojo; si es no negativo,
        *  se establece en verde oscuro. */

        resultView.apply {
            text = resultFormatted.toString()
            if (result < 0) {
                setTextColor(ContextCompat.getColor(context, R.color.red))
                if (Utils.isDarkTheme) {
                    setTextColor(ContextCompat.getColor(context, R.color.coralred))
                }
            } else {
                setTextColor(ContextCompat.getColor(context, R.color.darkgreen))
                if (Utils.isDarkTheme) {
                    setTextColor(ContextCompat.getColor(context, R.color.darkGreenPistacho))
                }
            }
        }
        return view
    }

    // Método llamado cuando el fragmento es destruido
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Importante para evitar fugas de memoria
    }

    // Método companion utilizado para crear una nueva instancia del fragmento

}
