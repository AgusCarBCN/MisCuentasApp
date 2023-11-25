package carnerero.agustin.cuentaappandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import carnerero.agustin.cuentaappandroid.databinding.FragmentCalculatorBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalculatorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
//Para gestionar los eventos de los botones de la calculadora implementamos View.OnClickListener
class CalculatorFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // Variable para manejar el View Binding
    private var _binding: FragmentCalculatorBinding? = null
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
    ): View? {
        // Inflar el diseño del fragmento utilizando View Binding
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        val view = binding.root
        val listOfButtons=ArrayList<Button>()


         // Asignar el click listener a los botones
        val uno = binding.btn1.setOnClickListener(this)
        val dos = binding.btn2.setOnClickListener(this)
        val tres = binding.btn3.setOnClickListener(this)
        val cuatro = binding.btn4.setOnClickListener(this)
        val cinco = binding.btn5.setOnClickListener(this)
        val seis = binding.btn6.setOnClickListener(this)
        val siete = binding.btn7.setOnClickListener(this)
        val ocho = binding.btn8.setOnClickListener(this)
        val nueve = binding.btn9.setOnClickListener(this)
        val cero = binding.btn0.setOnClickListener(this)
        val coma=binding.btnComa.setOnClickListener(this)
        val del=binding.btnRetro.setOnClickListener (this)
        val clear=binding.btnClear.setOnClickListener(this)
        val signoigual=binding.btnResult.setOnClickListener(this)
        //Botones de operaciones
        val suma=binding.btnPlus.setOnClickListener (this)
        val resta=binding.btnMinus.setOnClickListener(this)
        val multiplica=binding.btnTimes.setOnClickListener (this)
        val divide=binding.btnDiv.setOnClickListener(this)
        val porcentaje=binding.btnporc.setOnClickListener (this)

        val resultado=binding.tvResult
        val operation = binding.tvOperation

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalculatorFragment.
         */
        const val MULTIPLICAR = "×"
        const val DIVIDIR = "÷"
        const val SUMAR = "+"
        const val RESTAR = "-"
        const val PORCENTAJE = "%"
        const val NULL = "null"
        const val COMA = ","

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalculatorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(view: View?) {
        var result=0.0
        val strValue = (view as Button).text.toString()
        when (view?.id) {
            R.id.btn_clear -> {
                binding.tvOperation.text=""
                binding.tvResult.text=""
            }

            R.id.btn_result -> {
                tryResolve(binding.tvOperation.text.toString())

            }

            R.id.btn_retro -> {
                val length=binding.tvOperation.text.length
                if(length>0) {
                    val newOperation = binding.tvOperation.text.toString().substring(0, length - 1)
                    binding.tvOperation.text = newOperation
                }
            }

            else -> run {

                binding.tvOperation.append(strValue)

            }

        }

    }

    //Extrae el operador de la cadena operacion
    private fun getOperator(operation: String): String {
        var operator = ""

        if (operation.contains(MULTIPLICAR)) {
            operator = MULTIPLICAR
        } else if (operation.contains(DIVIDIR)) {
            operator = DIVIDIR
        } else if (operation.contains(SUMAR)) {
            operator = SUMAR
        }  else  {
            operator = NULL
        }
        if(operator==NULL && operation.lastIndexOf(RESTAR)>0){
            operator=RESTAR
        }
        return operator
    }

    private fun tryResolve(operationRef: String) {
        val operator = getOperator(operationRef)
        var values = arrayOfNulls<String>(0)
        if(operator!=NULL){
            if(operator== RESTAR){
                val index=operationRef.lastIndexOf(RESTAR)
                if(index<operationRef.length-1){
                values= arrayOfNulls(2)
                values[0]=operationRef.substring(0,index)
                values[1]=operationRef.substring(index+1)
                }else{
                    values= arrayOfNulls(1)
                    values[0]=operationRef.substring(0,index)
                }
            }else{
                values=operationRef.split(operator).toTypedArray()
            }
        }

        val number1=values[0]!!.toDouble()
        val number2=values[1]!!.toDouble()
        binding.tvResult.text=result(number1,number2,operator).toString()

    }

    private fun result(number1: Double, number2: Double, operator: String): Double {

        var resultado = 0.0

        when (operator) {
            SUMAR -> resultado = number1 + number2
            RESTAR -> resultado = number1 - number2
            MULTIPLICAR -> resultado = number1 * number2
            DIVIDIR -> resultado = number1 / number2
            PORCENTAJE -> resultado = (number1 / number2) * 100
        }
        return resultado
    }
}