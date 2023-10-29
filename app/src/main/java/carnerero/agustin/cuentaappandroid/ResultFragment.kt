package carnerero.agustin.cuentaappandroid

import android.graphics.Color
import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import carnerero.agustin.cuentaappandroid.R.color.red
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
    ): View? {
        // Inflate the layout for this fragment
        val rootview= inflater.inflate(R.layout.fragment_result, container, false)
        val resultView:TextView=rootview.findViewById(R.id.result)
        val euroLocale = Locale("es", "ES") // Establecer la Locale a español/españa para formato en euros
        val currencyFormat = NumberFormat.getCurrencyInstance(euroLocale)
        //Sacamos el valor double de arguments
        val result:Double= arguments?.getDouble("Result")!!
        //Le damos formato al resultado como valor absoluto y con formato de divisa euro
        val resultFormatted = currencyFormat.format(Math.abs(result))
        // Usando String.format para mostrar solo dos decimales y con la divisa del euro
        resultView.text=resultFormatted.toString()
        if(result<0){
            //Si result es menor que 0 se mostrara de color verde
            resultView.setTextColor(context?.let { ContextCompat.getColor(it, R.color.red) }!!)
        }else{
            resultView.setTextColor(context?.let { ContextCompat.getColor(it, R.color.darkgreen) }!!)
        }



        return rootview
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