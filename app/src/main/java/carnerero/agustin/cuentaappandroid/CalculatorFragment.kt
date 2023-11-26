package carnerero.agustin.cuentaappandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import carnerero.agustin.cuentaappandroid.databinding.FragmentCalculatorBinding
import com.google.android.material.snackbar.Snackbar

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
        val coma = binding.btnComa.setOnClickListener(this)
        val del = binding.btnRetro.setOnClickListener(this)
        val clear = binding.btnClear.setOnClickListener(this)
        val signoigual = binding.btnResult.setOnClickListener(this)
        //Botones de operaciones
        val suma = binding.btnPlus.setOnClickListener(this)
        val resta = binding.btnMinus.setOnClickListener(this)
        val multiplica = binding.btnTimes.setOnClickListener(this)
        val divide = binding.btnDiv.setOnClickListener(this)
        val porcentaje = binding.btnPorc.setOnClickListener(this)

        val resultado = binding.tvResult
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
        const val PUNTO = "."

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
        val OPERADORES = arrayOf(SUMAR, RESTAR, MULTIPLICAR, DIVIDIR, PUNTO)
        when (view?.id) {
            R.id.btn_clear -> {
                operation.text=""
                showResult.text=""
            }

            R.id.btn_result -> {
                tryResolve(operation.text.toString())
                operation.text = ""
            }

            R.id.btn_plus,
            R.id.btn_minus,
            R.id.btn_times,
            R.id.btn_div -> {

                val ultimoCaracterEsOperador = OPERADORES.any { showOperation.endsWith(it) }
                // Verificar si strValue es un dígito
                val soloDigitos = strValue.all { it.isDigit() }

                tryResolve(operation.text.toString())

                if (!ultimoCaracterEsOperador || soloDigitos) {
                    // Si el último carácter no es un operador, entonces puedes añadir el nuevo valor (strValue)
                    operation.append(strValue)

                }
            }

            R.id.btn_coma -> {


                val ultimoCaracterEsOperador = OPERADORES.any { showOperation.endsWith(it) }
                // Verificar si strValue es un dígito
                val soloDigitos = strValue.all { it.isDigit() }

                if (!ultimoCaracterEsOperador || soloDigitos) {
                    // Si el último carácter no es un operador, entonces puedes añadir el nuevo valor (strValue)
                    operation.append(strValue)
                }
            }

            R.id.btn_porc -> {
                tryResolve(operation.text.toString())

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

    private fun tryResolve(operationRef: String) {
        //Si no hay nada en la pantalla de operaciones sale de la funcion ,no se ejecuta el codigo de la funcion tryResolve
        if(operationRef.isEmpty())return
        var operation=operationRef
        if (operation.contains(PUNTO) && operation.lastIndexOf(PUNTO) == operation.length - 1) {
            operation = operation.substring(0, operation.length - 1)
        }
        val operator = getOperator(operationRef)
        var values = arrayOfNulls<String>(0)
        if(operator!=NULL){
            if(operator== RESTAR){
                val index=operation.lastIndexOf(RESTAR)
                if(index<operationRef.length-1){
                values= arrayOfNulls(2)
                values[0]=operation.substring(0,index)
                values[1]=operation.substring(index+1)
                }else{
                    values= arrayOfNulls(1)
                    values[0]=operation.substring(0,index)
                }
            }else{
                values=operation.split(operator).toTypedArray()
            }
        }
        //Validamos si tiene los dos elementos para poder realizar la operacion
        if(values.size>1) {
            try {
                val number1 = values[0]!!.toDouble()
                val number2 = values[1]!!.toDouble()
                binding.tvResult.text = result(number1, number2, operator).toString()
                if(binding.tvResult.text.isNotEmpty()){
                    binding.tvOperation.text=binding.tvResult.text
                }
            }catch (e:NumberFormatException){
                showMessage(getString(R.string.formaterror))
                binding.tvOperation.text=""

            }
            }
        else {
            if (operator != NULL) {
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
            if (operation.isEmpty() || lastElement != RESTAR && lastElement != PUNTO) {
                binding.tvOperation.append(operator)
            }
        } else {
            if (!operation.isEmpty() && lastElement != PUNTO) {
                binding.tvOperation.append(operator)
            }
        }
    }
}