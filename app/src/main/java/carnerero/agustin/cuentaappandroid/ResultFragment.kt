package carnerero.agustin.cuentaappandroid


import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import carnerero.agustin.cuentaappandroid.databinding.FragmentResultBinding
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentResultBinding?=null
    private val binding get() = _binding!!
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
        _binding= FragmentResultBinding.inflate(inflater,container,false)
        val view = binding.root
        val resultView=binding.result
        val euroLocale = Locale("es", "ES") // Establecer la Locale a español/españa para formato en euros
        val currencyFormat = NumberFormat.getCurrencyInstance(euroLocale)
        //Sacamos el valor double de arguments
        val result:Double= arguments?.getDouble("Result")!!
        //Le damos formato al resultado como valor absoluto y con formato de divisa euro
        val resultFormatted = currencyFormat.format(abs(result))
        // Usando String.format para mostrar solo dos decimales y con la divisa del euro
        /*La función run se utiliza para ejecutar un bloque de código en el contexto del objeto receptor .
        * actualiza el texto de la vista resultView y cambia su color en función del valor de result.
        *  Si result es negativo, el color del texto se establece en rojo; si es no negativo,
        *  se establece en verde oscuro.*/
        resultView.apply {
            text = resultFormatted.toString()
            if (result < 0) {
                setTextColor(ContextCompat.getColor(context, R.color.red))
            } else {
                setTextColor(ContextCompat.getColor(context, R.color.darkgreen))
            }
        }


        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Importante para evitar fugas de memoria
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResultFragment.
         */
        // TODO: Rename and change types and number of parameters
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