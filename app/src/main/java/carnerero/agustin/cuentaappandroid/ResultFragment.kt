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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment : Fragment() {

    // View Binding para acceder a los componentes de la interfaz de usuario
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    // SharedPreferences para almacenar y recuperar datos de forma sencilla
    private lateinit var sharedPreferences: SharedPreferences

    // Parámetros de la instancia (puedes cambiarlos según tus necesidades)
    private var param1: String? = null
    private var param2: String? = null

    // Método llamado cuando se crea el fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperar los argumentos proporcionados al crear la instancia del fragmento
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
        val lang = sharedPreferences.getString(getString(R.string.lang), "es")
        val country = sharedPreferences.getString(getString(R.string.country), "ES")

        // Establecer la Locale para el formato en euros
        val euroLocale = Locale(lang!!, country!!)

        // Obtener una instancia de NumberFormat para el formato de moneda en euros
        val currencyFormat = NumberFormat.getCurrencyInstance(euroLocale)

        // Sacar el valor double de los argumentos
        val result: Double = arguments?.getDouble("Result")!!

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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResultFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
