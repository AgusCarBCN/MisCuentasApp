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
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

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
                if (replaceOperator(charSequence.toString())) {
                    val length = text.length
                    // reemmplazar el penultimo caracter en el caso de que se añaden dos operadores seguidos en la calculadora
                    val newOperation = "${text.substring(0, length - 2)}${text.substring(length - 1)}"
                    text = newOperation
                }
            }
        }

        return view
    }

    private fun replaceOperator(charSequence: CharSequence): Boolean {

        if (charSequence.length < 2) return false
        val lastElement = charSequence[charSequence.length - 1].toString()
        val penultElement = charSequence[charSequence.length - 2].toString()

        return (lastElement == MULTIPLICAR || lastElement == DIVIDIR || lastElement == SUMAR) && (penultElement == MULTIPLICAR || penultElement == DIVIDIR || penultElement == SUMAR || penultElement==RESTAR)
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
        var operation=binding.tvOperation
        var showResult=binding.tvResult
        var showOperation = operation.text.toString()
        val OPERADORES = arrayOf(SUMAR, RESTAR, MULTIPLICAR, DIVIDIR, COMA)
        when (view?.id) {
            R.id.btn_clear -> {
                operation.text=""
                showResult.text=""
            }

            R.id.btn_result -> {
                tryResolve(operation.text.toString(),true)
                operation.text = ""
            }

            R.id.btn_plus,
            R.id.btn_minus,
            R.id.btn_times,
            R.id.btn_div -> {
                tryResolve(operation.text.toString(),false)
                val operator = strValue
                val operationStr = operation.text.toString()
                addOperator(operator, operationStr)
            }

            R.id.btn_coma -> {

                addPoint(strValue,showOperation)
            }

            R.id.btn_porc -> {
                tryResolve(operation.text.toString(),false)
                // Verificar si el último carácter es un operador o si ya hay un porcentaje en la operación
                val ultimoCaracterEsOperador = OPERADORES.any { showOperation.endsWith(it) }
                val contienePorcentaje = showOperation.contains(PORCENTAJE)
                if (!ultimoCaracterEsOperador && !contienePorcentaje) {
                    // Obtener el número antes del porcentaje
                    val numeroAntesDelPorcentaje = try {
                        showOperation.toDouble()
                    } catch (e: NumberFormatException) {
                        // Manejar la excepción si la cadena no se puede convertir a un número
                        0.0
                    }

                    // Calcular el porcentaje y mostrar el resultado en la operación
                    val resultadoPorcentaje = numeroAntesDelPorcentaje / 100
                    operation.text = resultadoPorcentaje.toString()
                }
            }

            R.id.btn_retro -> {
                val length = operation.text.length
                if (length > 0) {
                    val newOperation = operation.text.toString().substring(0, length - 1)
                    operation.text = newOperation
                }
            }
            else -> run {
                operation.append(strValue)

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
        } else if (operation.contains(PORCENTAJE)) {
            operator = PORCENTAJE
        } else {
            operator = NULL
        }
        if(operator==NULL && operation.lastIndexOf(RESTAR)>0){
            operator=RESTAR
        }
        return operator
    }




    private fun tryResolve(operationRef: String, isResolve: Boolean) {
        if (operationRef.isEmpty()) return

        var operation = operationRef

        if (operation.endsWith(COMA)) {
            operation = operation.substring(0, operation.length - 1)
        }

        val operator = getOperator(operationRef)
        var values: List<String> = emptyList()

        if (operator != NULL) {
            values = when (operator) {
                RESTAR -> {
                    val index = operation.lastIndexOf(RESTAR)
                    if (index < operationRef.length - 1) {
                        listOf(operation.substring(0, index), operation.substring(index + 1))
                    } else {
                        listOf(operation.substring(0, index))
                    }
                }
                else -> operation.split(operator)
            }
        }

        if (values.size > 1) {
            try {
                // Replace comma with dot in the input numbers
                val (number1, number2) = values.map { it.replace(',', '.').toDouble() }

                val result = result(number1, number2, operator)

                // Format the result to have two decimal places and a comma
                val decimalFormatSymbols = DecimalFormatSymbols()
                decimalFormatSymbols.decimalSeparator = ','

                val decimalFormat = DecimalFormat("#,##0.00", decimalFormatSymbols)
                val formattedResult = decimalFormat.format(result)

                binding.tvResult.text = formattedResult

                if (binding.tvResult.text.isNotEmpty() && !isResolve) {
                    binding.tvOperation.text = binding.tvResult.text
                }
            } catch (e: NumberFormatException) {
                if (isResolve) {
                    showMessage(getString(R.string.formaterror))
                }
            }
        } else {
            if (isResolve && operator != NULL) {
                showMessage(getString(R.string.incorrectExpresion))
            }
        }
    }


    private fun result(number1: Double, number2: Double, operator: String): Double {

        var resultado = 0.0

        when (operator) {
            SUMAR -> resultado = number1 + number2
            RESTAR -> resultado = number1 - number2
            MULTIPLICAR -> resultado = number1 * number2
            DIVIDIR -> resultado = number1 / number2

        }
        return resultado
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.orange)).show()
    }

    private fun addOperator(operator: String, operation: String) {
        val lastElement = if (operation.isEmpty()) ""
        else operation.substring(operation.length - 1)

        if (operator == RESTAR) {
            if (operation.isEmpty() || lastElement != RESTAR && lastElement != COMA) {
                binding.tvOperation.append(operator)
            }
        } else {
            if (!operation.isEmpty() && lastElement != COMA) {
                binding.tvOperation.append(operator)
            }
        }
    }
    //Previene de errores de formato al añadir puntos en las operaciones
    private fun addPoint(pointStr:String,operation: String ) {
        //Si el string que muestra la operacion no contiene un punto. Permite añadir un punto
        if (!operation.contains(COMA)) {
            binding.tvOperation.append(COMA)
            //Verificamos si existe un operador
        } else {
            val operator = getOperator(operation)
            var values = arrayOfNulls<String>(0)
            if (operator != NULL) {
                if (operator == RESTAR) {
                    val index = operation.lastIndexOf(RESTAR)
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
            if(values.size>0){
                val number1=values[0]!!
                if(values.size>1){
                    val number2=values[1]!!
                    if(number1.contains(COMA) && !number2.contains(COMA)){
                        binding.tvOperation.append(COMA)
                    }
                }else{
                    if(number1.contains(COMA)){
                        binding.tvOperation.append(COMA)
                    }
                }
            }
        }
    }
}