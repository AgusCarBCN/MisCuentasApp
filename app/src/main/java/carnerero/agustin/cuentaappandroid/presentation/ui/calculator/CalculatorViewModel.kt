package carnerero.agustin.cuentaappandroid.presentation.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(private val parser: ParserCalculator) : ViewModel() {

    private val _expression = MutableLiveData("")
    val expression: LiveData<String> = _expression


    fun clear() {
        _expression.value = ""
    }

    fun append(char: String) {
        if (char in "0123456789") {
            _expression.value += char
        } else if (char in "+-×÷%√^") {
            if (_expression.value?.isNotEmpty() == true) {
                val lastChar = _expression.value!!.last()
                // if last char is an operator, replace it with the new operator
                if (lastChar in "+-×÷%") {
                    _expression.value = _expression.value!!.dropLast(1)
                }
            }
            _expression.value += char
        } else if (char == ".") {
            if (_expression.value?.isNotEmpty() == true) {
                val lastChar = _expression.value!!.last()
                if (lastChar != '.') {
                    // if last char is an operator, and the current char is a dot, add a zero before the dot
                    if (lastChar in "+-×÷") {
                        _expression.value += "0"
                    }
                    _expression.value += char
                }
            }

        } else if (char == "(") {
            if (_expression.value?.isNotEmpty() == true) {
                val lastChar = _expression.value!!.last()
                // if last char is not a operator, add a multiplication operator before the parenthesis
                if (lastChar !in "+-×÷") {
                    _expression.value += "×"
                }
            }
            _expression.value += char
        } else if (char == ")") {
            _expression.value += char
        }
        else if(char=="log "){
            _expression.value+=char
        }
        else if(char==" mod "){
            _expression.value+=char
        }
    }

    fun delete() {
        if (_expression.value?.isNotEmpty() == true) {
            _expression.value = _expression.value!!.dropLast(1)
        }
    }
    fun changeSign() {
        val expr = _expression.value ?: ""
        if (expr.isEmpty()) return

        // Regex para encontrar el último número (entero o decimal)
        val regex = """-?\d+(\.\d+)?$""".toRegex()
        val match = regex.find(expr)

        if (match != null) {
            val number = match.value
            val start = match.range.first
            val end = match.range.last + 1

            // Cambiamos el signo
            val newNumber = if (number.startsWith("-")) {
                number.removePrefix("-")
            } else {
                "-$number"
            }

            // Reemplazamos solo el último número en la expresión
            _expression.value = expr.substring(0, start) + newNumber + expr.substring(end)
        }
    }





    fun evaluate() {
        _expression.value = try {
            val result = _expression.value?.let { parser.evaluate(it) }
            result.toString()
        } catch (_: Exception) {
            "Error"
        }
    }
}


