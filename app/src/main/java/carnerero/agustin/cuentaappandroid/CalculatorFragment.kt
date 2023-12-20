package carnerero.agustin.cuentaappandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import carnerero.agustin.cuentaappandroid.databinding.FragmentCalculatorBinding
import carnerero.agustin.cuentaappandroid.utils.Utils
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

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

    private val decimalFormat =  DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.GERMANY))

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
    ): View {
        // Inflar el diseño del fragmento utilizando View Binding
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        val view = binding.root

        with(binding) {
            val buttons = listOf(
                btn1, btn2, btn3, btn4, btn5,
                btn6, btn7, btn8, btn9, btn0,
                btnComa, btnRetro, btnClear, btnResult,
                btnPlus, btnMinus, btnTimes, btnDiv, btnPorc
            )
            buttons.forEach { button ->
                button.setOnClickListener(this@CalculatorFragment)
            }
        }
        val operation = binding.tvOperation

        //Detectamos cambios en tiempo real en operation para reemplazar el operador cuando se repita

        operation.run {
            addTextChangedListener { charSequence ->
                if (Utils.replaceOperator(charSequence.toString())) {
                    val length = text.length
                    // reemmplazar el penultimo caracter en el caso de que se añaden dos operadores seguidos en la calculadora
                    val newOperation = "${text.substring(0, length - 2)}${text.substring(length - 1)}"
                    text = newOperation
                }

            }
        }

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

        val strValue = (view as Button).text.toString()
        val operation=binding.tvOperation
        val showResult=binding.tvResult
        val operationStr = operation.text.toString()
        val operadores = arrayOf(AppConst.SUMAR, AppConst.RESTAR, AppConst.MULTIPLICAR, AppConst.DIVIDIR, AppConst.COMA)
        when (view.id) {
            R.id.btn_clear -> {
                operation.text=""
                showResult.text=""
            }

            R.id.btn_result -> {
                showResultOrError(operationStr,true)
                operation.text = ""
            }

            R.id.btn_plus,
            R.id.btn_minus,
            R.id.btn_times,
            R.id.btn_div -> {
                showResultOrError(operationStr,false)
                addOperator(strValue, operationStr)
            }

            R.id.btn_coma -> {
                addPoint(operationStr)
            }

            R.id.btn_porc -> {
                showResultOrError(operationStr,false)
                // Verificar si el último carácter es un operador o si ya hay un porcentaje en la operación
                val ultimoCaracterEsOperador = operadores.any { operationStr.endsWith(it) }
                val contienePorcentaje = operationStr.contains(AppConst.PORCENTAJE)
                if (!ultimoCaracterEsOperador && !contienePorcentaje) {
                    // Obtener el número antes del porcentaje
                    val numeroAntesDelPorcentaje = try {
                        operationStr.toDouble()
                    } catch (e: NumberFormatException) {
                        // Manejar la excepción si la cadena no se puede convertir a un número
                        0.0
                    }

                    // Calcular el porcentaje y mostrar el resultado en la operación
                    val resultadoPorcentaje = numeroAntesDelPorcentaje / 100
                    val resultFormat=decimalFormat.format(resultadoPorcentaje)
                    operation.text = resultFormat.toString()
                }
            }

            R.id.btn_retro -> {
                val length = operation.text.length
                operation.run {
                    if(text.isNotEmpty()){
                        text= operationStr.substring(0, length - 1)
                    }
                }
            }
            else -> run {
                operation.append(strValue)

            }
        }
    }

    //Extrae el operador de la cadena operacion


    private fun showResultOrError(operation: String, isResolve: Boolean) {
        Utils.tryResolve(operation,isResolve,object :OnResolveListener{
            override fun showResult(result: Double) {
                // Format the result to have two decimal places and a comma
                val decimalFormatSymbols = DecimalFormatSymbols()
                decimalFormatSymbols.decimalSeparator = ','


                val formattedResult = decimalFormat.format(result)

                binding.tvResult.text = formattedResult

                if (binding.tvResult.text.isNotEmpty() && !isResolve) {
                    binding.tvOperation.text = binding.tvResult.text
                }
            }
            override fun showMessageError(errorMsg: Int) {
               showMessage(getString(errorMsg))
            }
        })
    }




    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.orange)).show()
    }

    private fun addOperator(operator: String, operation: String) {
        val lastElement = if (operation.isEmpty()) ""
        else operation.substring(operation.length - 1)

        if (operator == AppConst.RESTAR) {
            if (operation.isEmpty() || lastElement != AppConst.RESTAR && lastElement != AppConst.COMA) {
                binding.tvOperation.append(operator)
            }
        } else {
            if (operation.isNotEmpty() && lastElement != AppConst.COMA) {
                binding.tvOperation.append(operator)
            }
        }
    }
    //Previene de errores de formato al añadir puntos en las operaciones




    private fun addPoint(operation: String ) {
        //Si el string que muestra la operacion no contiene un punto. Permite añadir un punto

        if (!operation.contains(AppConst.COMA)) {
            binding.tvOperation.append(AppConst.COMA)
            //Verificamos si existe un operador
        } else {
            val operator = Utils.getOperator(operation)
            var values = arrayOfNulls<String>(0)
            if (operator != AppConst.NULL) {
                if (operator == AppConst.RESTAR) {
                    val index = operation.lastIndexOf(AppConst.RESTAR)
                    if (index < operation.length - 1) {
                        values = arrayOfNulls(2)
                        values[0] = operation.substring(0, index)
                        values[1] = operation.substring(index + 1)
                    } else {
                        values = arrayOfNulls(1)
                        values[0] = operation.substring(0, index)
                    }
                } else {
                    values = operation.split(operator).toTypedArray()
                }
            }
            if(values.isNotEmpty()){
                val number1=values[0]!!
                if(values.size>1){
                    val number2=values[1]!!
                    if(number1.contains(AppConst.COMA) && !number2.contains(AppConst.COMA)){
                        binding.tvOperation.append(AppConst.COMA)
                    }
                }else{
                    if(number1.contains(AppConst.COMA)){
                        binding.tvOperation.append(AppConst.COMA)
                    }
                }
            }
        }
    }
}