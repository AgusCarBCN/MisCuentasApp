package carnerero.agustin.cuentaappandroid.presentation.ui.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.domain.apidata.ConvertCurrencyUseCase
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import kotlin.math.ln
import kotlin.math.pow

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val parser: ParserCalculator,
    private val converterCurrency: ConvertCurrencyUseCase
) : ViewModel() {

    var fromCurrency by mutableStateOf("EUR")
        private set

    var toCurrency by mutableStateOf("USD")
        private set

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

    var amount by mutableStateOf("")
    private set

    // Resultado calculado (puede actualizar _expression en CalculatorViewModel si quieres)
   /* var calculationResult by mutableStateOf<BigDecimal?>(null)
        private set*/

    // Texto que se muestra en la pantalla
    var expression by mutableStateOf("")
        private set

    // Mostrar / ocultar el diálogo
    var showDialogConverter by mutableStateOf(false)
        private set


    fun clear() {

        expression = ""
    }



    fun append(char: String) {
        if (char in "0123456789") {
            expression += char
        } else if (char in "+-×÷%√^") {
            if (expression.isNotEmpty()) {
                val lastChar = expression.last()
                // if last char is an operator, replace it with the new operator
                if (lastChar in "+-×÷%") {
                    expression = expression.dropLast(1)
                }
            }
            expression += char
        } else if (char == ".") {
            if (expression.isNotEmpty()) {
                val lastChar = expression.last()
                if (lastChar != '.') {
                    // if last char is an operator, and the current char is a dot, add a zero before the dot
                    if (lastChar in "+-×÷") {
                        expression += "0"
                    }
                    expression += char
                }
            }

        } else if (char == "(") {
            if (expression.isNotEmpty()) {
                val lastChar = expression.last()
                // if last char is not a operator, add a multiplication operator before the parenthesis
                if (lastChar !in "+-×÷") {
                    expression += "×"
                }
            }
            expression += char
        } else if (char == ")") {
            expression += char
        } else if (char == "log ") {
            expression += char
        } else if (char == " mod ") {
            expression += char
        }
    }

    fun delete() {
        if (expression.isNotEmpty()) {
            expression = expression.dropLast(1)
        }
    }

    fun changeSign() {
        val expr = expression
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
            expression = expr.substring(0, start) + newNumber + expr.substring(end)
        }
    }
    fun conversionCurrencyRate(amount: BigDecimal?, fromCurrency: String, toCurrency: String) {

        viewModelScope.launch {
            try {
                val rate = withContext(Dispatchers.IO) {
                    val response = converterCurrency.invoke(fromCurrency, toCurrency)
                    response.body()?.conversion_rate
                        ?: throw IllegalStateException("Invalid response")
                }

                val result = amount
                    ?.multiply(rate.toBigDecimal())
                    ?: BigDecimal.ZERO

                expression = result.toString()


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
    fun futureValueCompound(
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
        expression= BigDecimal(fvDouble).setScale(2, RoundingMode.HALF_UP).toString()
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
    fun presentValueCompound(
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
         expression=BigDecimal(pvDouble).setScale(2, RoundingMode.HALF_UP).toString()
    }
    /**
     * Calcula el Valor Futuro (FV) con interés simple.
     *
     * Fórmula:
     * FV = PV * (1 + r * n)
     *
     * @param presentValue Valor presente (PV)
     * @param rate Tasa de interés en porcentaje (por ejemplo 5 para 5%)
     * @param periods Número de períodos, puede ser decimal (por ejemplo 2.5 años)
     * @return Valor futuro redondeado a 2 decimales
     */
    fun futureValueSimple(
        presentValue: BigDecimal,
        rate: BigDecimal,
        periods: Double
    ) {
        // Convertimos la tasa de porcentaje a decimal
        val rateDecimal = rate.divide(BigDecimal(100), 10, RoundingMode.HALF_UP)

        // Convertimos periods a BigDecimal
        val periodsBD = BigDecimal.valueOf(periods)

        // FV = PV * (1 + r * n)
        val factor = BigDecimal.ONE.add(rateDecimal.multiply(periodsBD))
        val fv = presentValue.multiply(factor)

        // Redondeamos a 2 decimales y guardamos en expression
        expression = fv.setScale(2, RoundingMode.HALF_UP).toString()
    }


    /**
     * Calcula el Valor Presente (PV) con interés simple.
     *
     * Fórmula:
     * PV = FV / (1 + r * n)
     *
     * @param futureValue Valor futuro (FV)
     * @param rate Tasa de interés en porcentaje (por ejemplo 5 para 5%)
     * @param periods Número de períodos, puede ser decimal (por ejemplo 2.5 años)
     * @return Valor presente redondeado a 2 decimales
     */
    fun presentValueSimple(
        futureValue: BigDecimal,
        rate: BigDecimal,
        periods: Double
    ) {
        // Convertimos la tasa de porcentaje a decimal
        val rateDecimal = rate.divide(BigDecimal(100), 10, RoundingMode.HALF_UP)

        // Convertimos periods a BigDecimal
        val periodsBD = BigDecimal.valueOf(periods)

        // PV = FV / (1 + r * n)
        val denominator = BigDecimal.ONE.add(rateDecimal.multiply(periodsBD))
        val pv = futureValue.divide(denominator, 10, RoundingMode.HALF_UP)

        // Redondeamos a 2 decimales y guardamos en expression
        expression = pv.setScale(2, RoundingMode.HALF_UP).toString()
    }

    /**
     * Calcula el pago periódico de un préstamo (PMT) con interés compuesto.
     *
     * Fórmula:
     * PMT = PV × (r / (1 - (1 + r)^-n))
     */
    fun loanPaymentCompound(
        loanAmount: BigDecimal,
        rate: BigDecimal,
        periods: Double
    ) {
        if (loanAmount <= BigDecimal.ZERO) {
            expression = "Error: El monto del préstamo debe ser mayor que 0"
            return
        }
        if (rate <= BigDecimal.ZERO) {
            expression = "Error: La tasa debe ser mayor que 0"
            return
        }
        if (periods <= 0) {
            expression = "Error: El número de períodos debe ser mayor que 0"
            return
        }

        val rateDecimal = rate.divide(BigDecimal(100), 10, RoundingMode.HALF_UP)
        val pmt = loanAmount.toDouble() * (rateDecimal.toDouble() / (1 - (1 + rateDecimal.toDouble()).pow(-periods)))
        expression = BigDecimal(pmt).setScale(2, RoundingMode.HALF_UP).toString()
    }

    /**
     * Calcula el interés acumulado de una inversión con interés compuesto.
     *
     * Fórmula:
     * Interest = PV × ((1 + r)^n - 1)
     */
    fun interestEarnedCompound(
        presentValue: BigDecimal,
        rate: BigDecimal,
        periods: Double
    ) {
        if (presentValue <= BigDecimal.ZERO) {
            expression = "Error: El valor presente debe ser mayor que 0"
            return
        }
        if (rate <= BigDecimal.ZERO) {
            expression = "Error: La tasa debe ser mayor que 0"
            return
        }
        if (periods <= 0) {
            expression = "Error: El número de períodos debe ser mayor que 0"
            return
        }

        val rateDecimal = rate.divide(BigDecimal(100), 10, RoundingMode.HALF_UP)
        val interest = presentValue.toDouble() * ((1 + rateDecimal.toDouble()).pow(periods) - 1)
        expression = BigDecimal(interest).setScale(2, RoundingMode.HALF_UP).toString()
    }

    /**
     * Calcula el número de períodos necesarios para alcanzar un valor futuro con interés compuesto.
     *
     * Fórmula:
     * n = ln(FV / PV) / ln(1 + r)
     */
    fun periodsRequiredCompound(
        presentValue: BigDecimal,
        futureValue: BigDecimal,
        rate: BigDecimal
    ) {
        if (presentValue <= BigDecimal.ZERO) {
            expression = "Error: El valor presente debe ser mayor que 0"
            return
        }
        if (futureValue <= presentValue) {
            expression = "Error: El valor futuro debe ser mayor que el presente"
            return
        }
        if (rate <= BigDecimal.ZERO) {
            expression = "Error: La tasa debe ser mayor que 0"
            return
        }

        val rateDecimal = rate.divide(BigDecimal(100), 10, RoundingMode.HALF_UP)
        val periods = ln(futureValue.toDouble() / presentValue.toDouble()) / ln(1 + rateDecimal.toDouble())
        expression = BigDecimal(periods).setScale(2, RoundingMode.HALF_UP).toString()
    }

    /**
     * Calcula la tasa de interés requerida para alcanzar un valor futuro con interés compuesto.
     *
     * Fórmula:
     * r = (FV / PV)^(1/n) - 1
     */
    fun requiredInterestRateCompound(
        presentValue: BigDecimal,
        futureValue: BigDecimal,
        periods: Double
    ) {
        if (presentValue <= BigDecimal.ZERO) {
            expression = "Error: El valor presente debe ser mayor que 0"
            return
        }
        if (futureValue <= presentValue) {
            expression = "Error: El valor futuro debe ser mayor que el presente"
            return
        }
        if (periods <= 0) {
            expression = "Error: El número de períodos debe ser mayor que 0"
            return
        }

        val rate = (futureValue.toDouble() / presentValue.toDouble()).pow(1 / periods) - 1
        expression = BigDecimal(rate * 100).setScale(2, RoundingMode.HALF_UP).toString()
    }
    fun evaluate() {
        expression= try {
            val result = expression.let { parser.evaluate(it) }
            result.toString()
        } catch (_: Exception) {
            "Error"
        }
    }

    fun onShowDialogConverter(newValue: Boolean) {
        showDialogConverter = newValue
    }

    fun closeCurrencyDialog(){
        showDialogConverter=false
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
       // calculationResult = null
        showDialog = true
    }
    fun updateAmount(value:String){
        amount=value
    }
    fun onCurrenciesChange(newFromCurrency:String,newToCurrency:String){
        fromCurrency=newFromCurrency
        toCurrency=newToCurrency
    }

    // Actualizar un campo
    fun updateFieldValue(index: Int, value: String) {
        if (index in 0..2) fieldValues[index] = value
    }

    // Cerrar diálogo
    fun closeDialog() {
        showDialog = false
    }
    //Currency conversion

    fun currencyConvert(fromCurrency: String,toCurrency: String){
        val amount=amount.toBigDecimalOrNull()?: BigDecimal.ZERO
        conversionCurrencyRate(amount,fromCurrency,toCurrency)
        closeCurrencyDialog()
    }

    // CALCULAR Valor Presente (PV)
    fun calculatePresentValueCompound() {
        val fv = fieldValues.getOrNull(0)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val rate = fieldValues.getOrNull(1)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val periods = fieldValues.getOrNull(2)?.toDoubleOrNull() ?: 0.0

        if (fv == null || rate == null) {
            viewModelScope.launch {
                SnackBarController.sendEvent(SnackBarEvent("Campos inválidos"))
            }
        }

        presentValueCompound(fv, rate, periods)
        closeDialog()
    }

    fun calculateFutureValueCompound() {
        val pv = fieldValues.getOrNull(0)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val rate = fieldValues.getOrNull(1)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val periods = fieldValues.getOrNull(2)?.toDoubleOrNull() ?: 0.0

        if (pv == null || rate == null) {
            viewModelScope.launch {
                SnackBarController.sendEvent(SnackBarEvent("Campos inválidos"))
            }
        }
        futureValueCompound(pv, rate, periods)
        closeDialog()
    }
    fun calculatePresentValueSimple() {
        val fv = fieldValues.getOrNull(0)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val rate = fieldValues.getOrNull(1)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val periods = fieldValues.getOrNull(2)?.toDoubleOrNull() ?: 0.0

        if (fv == null || rate == null) {
            viewModelScope.launch {
                SnackBarController.sendEvent(SnackBarEvent("Campos inválidos"))
            }
        }

        presentValueSimple(fv, rate, periods)
        closeDialog()
    }
    fun calculateFutureValueSimple() {
        val pv = fieldValues.getOrNull(0)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val rate = fieldValues.getOrNull(1)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val periods = fieldValues.getOrNull(2)?.toDoubleOrNull() ?: 0.0

        if (pv == null || rate == null) {
            viewModelScope.launch {
                SnackBarController.sendEvent(SnackBarEvent("Campos inválidos"))
            }
        }
        futureValueSimple(pv, rate, periods)
        closeDialog()
    }
    fun calculateLoanPaymentCompound(){
        val pv = fieldValues.getOrNull(0)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val rate = fieldValues.getOrNull(1)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val periods = fieldValues.getOrNull(2)?.toDoubleOrNull() ?: 0.0
        if (pv == null || rate == null) {
            viewModelScope.launch {
                SnackBarController.sendEvent(SnackBarEvent("Campos inválidos"))
            }
        }
        loanPaymentCompound(pv, rate, periods)
        closeDialog()

    }
    fun calculateInterestEarned() {
        val pv = fieldValues.getOrNull(0)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val rate = fieldValues.getOrNull(1)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val periods = fieldValues.getOrNull(2)?.toDoubleOrNull() ?: 0.0
        //val compound = isCompoundField // por ejemplo, un toggle booleano

        if (pv == BigDecimal.ZERO || rate == BigDecimal.ZERO || periods == 0.0) {
            viewModelScope.launch {
                SnackBarController.sendEvent(SnackBarEvent("Campos inválidos"))
            }
            return
        }

        interestEarnedCompound(pv, rate, periods)
        closeDialog()
    }
    fun calculatePeriodsRequired() {
        val pv = fieldValues.getOrNull(0)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val fv = fieldValues.getOrNull(1)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val rate = fieldValues.getOrNull(2)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        //val compound = isCompoundField

        if (pv == BigDecimal.ZERO || fv == BigDecimal.ZERO || rate == BigDecimal.ZERO) {
            viewModelScope.launch {
                SnackBarController.sendEvent(SnackBarEvent("Campos inválidos"))
            }
            return
        }

        periodsRequiredCompound(pv, fv, rate)
        closeDialog()
    }
    fun calculateRequiredInterestRate() {
        val pv = fieldValues.getOrNull(0)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val fv = fieldValues.getOrNull(1)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val periods = fieldValues.getOrNull(2)?.toDoubleOrNull() ?: 0.0
        //val compound = isCompoundField

        if (pv == BigDecimal.ZERO || fv == BigDecimal.ZERO || periods == 0.0) {
            viewModelScope.launch {
                SnackBarController.sendEvent(SnackBarEvent("Campos inválidos"))
            }
            return
        }

        requiredInterestRateCompound(pv, fv, periods)
        closeDialog()
    }


}


