package carnerero.agustin.cuentaappandroid.presentation.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.apidata.ConvertCurrencyUseCase
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val parser: ParserCalculator,
    private val converterCurrency: ConvertCurrencyUseCase
) : ViewModel() {

    private val _expression = MutableLiveData("")
    val expression: LiveData<String> = _expression

    private val _ratio = MutableLiveData(BigDecimal.ONE)

    val ratio: LiveData<BigDecimal> = _ratio

    private val _showDialogConverter = MutableLiveData<Boolean>()
    val showDialogConverter: LiveData<Boolean> = _showDialogConverter

    fun clear() {

        _expression.value = ""
    }

    fun onExpressionChange(newValue: String) {

        _expression.value = newValue
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
        } else if (char == "log ") {
            _expression.value += char
        } else if (char == " mod ") {
            _expression.value += char
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

    fun convertCurrency(
        fromCurrency: String,
        toCurrency: String,
        accountsViewModel: AccountsViewModel
    ) {
        val currentExpression = _expression.value ?: return

        viewModelScope.launch {

            val result = withContext(Dispatchers.IO) {
                val ratio = accountsViewModel
                    .conversionCurrencyRate(fromCurrency, toCurrency)

                currentExpression
                    .toBigDecimalOrNull()
                    ?.multiply(ratio ?: BigDecimal.ONE)
                    ?: BigDecimal.ZERO
            }

            _expression.value = result.toString()
            accountsViewModel.onShowDialogConverter(false)

        }
    }

    /*fun conversionCurrencyRate(fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            try {
                val response = converterCurrency.invoke(fromCurrency, toCurrency)
                response.body()?.conversion_rate?.let { rate ->
                   val currentExpression=_expression.value
                   val result= currentExpression
                       ?.toBigDecimalOrNull()
                       ?.multiply(rate.toBigDecimal())
                       ?: BigDecimal.ZERO
                    _expression.value=result.toString()
                    _showDialogConverter.value=false
                }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            "No internet connection"
                        )
                    )
                }
            }
        }
    }*/
    fun conversionCurrencyRate(fromCurrency: String, toCurrency: String) {
        val currentExpression = _expression.value ?: return

        viewModelScope.launch {
            try {
                val rate = withContext(Dispatchers.IO) {
                    val response = converterCurrency.invoke(fromCurrency, toCurrency)
                    response.body()?.conversion_rate
                        ?: throw IllegalStateException("Invalid response")
                }

                val result = currentExpression
                    .toBigDecimalOrNull()
                    ?.multiply(rate.toBigDecimal())
                    ?: BigDecimal.ZERO

                _expression.value = result.toString()
                _showDialogConverter.value = false

            } catch (_: IOException) {
                SnackBarController.sendEvent(
                    SnackBarEvent("No internet connection")
                )
            } catch (_: Exception) {
                SnackBarController.sendEvent(
                    SnackBarEvent("Currency conversion error")
                )
            }
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
    fun onShowDialogConverter(newValue: Boolean){
        _showDialogConverter.value=newValue
    }
}


