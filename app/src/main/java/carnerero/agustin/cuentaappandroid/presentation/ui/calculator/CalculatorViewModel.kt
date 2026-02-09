package carnerero.agustin.cuentaappandroid.presentation.ui.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.R
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
import java.math.RoundingMode
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val parser: ParserCalculator,
    private val converterCurrency: ConvertCurrencyUseCase
) : ViewModel() {

    // Diálogo
    var showDialog by mutableStateOf(false)
        private set

    var currentTitleRes by mutableIntStateOf(R.string.presentValueTitle)
        private set
    var currentDescriptionRes by mutableIntStateOf(R.string.presentValueDes)
        private set
    var currentFieldLabels = mutableStateListOf<Int>()
        private set

    // Valores de los campos
    var fieldValues = mutableStateListOf("", "", "")
        private set

    // Resultado calculado (puede actualizar _expression en CalculatorViewModel si quieres)
    var calculationResult by mutableStateOf<BigDecimal?>(null)
        private set

    // Expresion de la pantalla de la calculadora
    private val _expression = MutableLiveData("")
    val expression: LiveData<String> = _expression


    private val _showDialogConverter = MutableLiveData<Boolean>()
    val showDialogConverter: LiveData<Boolean> = _showDialogConverter

    private val _showDialogFinance = MutableLiveData<Boolean>()
    val showDialogFinance: LiveData<Boolean> = _showDialogFinance

    fun clear() {

        _expression.value = ""
    }

    fun onShowFinanceDialog(newValue: Boolean) {
        _showDialogFinance.value = newValue
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
    /**
     * Calcula el Valor Futuro (FV) compuesto.
     *
     * Fórmula:
     * FV = PV * (1 + r)^n
     *
     * @param presentValue Valor presente (PV)
     * @param rate Tasa de interés en porcentaje (por ejemplo 5 para 5%)
     * @param periods Número de períodos, puede ser decimal (por ejemplo 2.5 años)
     * @return Valor futuro redondeado a 2 decimales
     */
    fun futureValue(
        presentValue: BigDecimal,
        rate: BigDecimal,
        periods: Double
    ) {
        // Convertimos la tasa de porcentaje a decimal
        val rateDecimal = rate.divide(BigDecimal(100), 10, RoundingMode.HALF_UP)

        // Base para la potencia: 1 + r
        val base = 1 + rateDecimal.toDouble()

        // Calculamos FV usando Double para permitir períodos decimales
        val fvDouble = presentValue.toDouble() * base.pow(periods)

        // Convertimos a BigDecimal y redondeamos a 2 decimales
        _expression.value= BigDecimal(fvDouble).setScale(2, RoundingMode.HALF_UP).toString()
    }

    /**
     * Calcula el Valor Presente (PV) compuesto.
     *
     * Fórmula:
     * PV = FV / (1 + r)^n
     *
     * @param futureValue Valor futuro (FV)
     * @param rate Tasa de interés en porcentaje (por ejemplo 5 para 5%)
     * @param periods Número de períodos, puede ser decimal (por ejemplo 2.5 años)
     * @return Valor presente redondeado a 2 decimales
     */
    fun presentValue(
        futureValue: BigDecimal,
        rate: BigDecimal,
        periods: Double
    ) {
        // Convertimos la tasa de porcentaje a decimal
        val rateDecimal = rate.divide(BigDecimal(100), 10, RoundingMode.HALF_UP)

        // Base para la potencia: 1 + r
        val base = 1 + rateDecimal.toDouble()

        // Calculamos PV usando Double para permitir períodos decimales
        val pvDouble = futureValue.toDouble() / base.pow(periods)

        // Convertimos a BigDecimal y redondeamos a 2 decimales
         _expression.value=BigDecimal(pvDouble).setScale(2, RoundingMode.HALF_UP).toString()
    }

    fun evaluate() {
        _expression.value = try {
            val result = _expression.value?.let { parser.evaluate(it) }
            result.toString()
        } catch (_: Exception) {
            "Error"
        }
    }

    fun onShowDialogConverter(newValue: Boolean) {
        _showDialogConverter.value = newValue
    }

    // Abrir diálogo
    fun openDialog(
        titleRes: Int,
        descriptionRes: Int,
        labels: List<Int>
    ) {
        currentTitleRes = titleRes
        currentDescriptionRes = descriptionRes
        currentFieldLabels.clear()
        currentFieldLabels.addAll(labels)
        fieldValues.clear()
        fieldValues.addAll(listOf("", "", "")) // limpiar valores
        calculationResult = null
        showDialog = true
    }

    // Actualizar un campo
    fun updateFieldValue(index: Int, value: String) {
        if (index in 0..2) fieldValues[index] = value
    }

    // Cerrar diálogo
    fun closeDialog() {
        showDialog = false
    }

    // CALCULAR Valor Presente (PV)
    fun calculatePresentValue() {
        val fv = fieldValues.getOrNull(0)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val rate = fieldValues.getOrNull(1)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val periods = fieldValues.getOrNull(2)?.toDoubleOrNull() ?: 0.0

        if (fv == null || rate == null) {
            viewModelScope.launch {
                SnackBarController.sendEvent(SnackBarEvent("Campos inválidos"))
            }
        }

        presentValue(fv, rate, periods)
        closeDialog()
    }

    fun calculateFutureValue() {
        val pv = fieldValues.getOrNull(0)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val rate = fieldValues.getOrNull(1)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val periods = fieldValues.getOrNull(2)?.toDoubleOrNull() ?: 0.0

        if (pv == null || rate == null) {
            viewModelScope.launch {
                SnackBarController.sendEvent(SnackBarEvent("Campos inválidos"))
            }
        }
        futureValue(pv, rate, periods)
        closeDialog()
    }


}


