package carnerero.agustin.cuentaappandroid.presentation.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component.CalculatorButton
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component.CurrencyDialogConverter
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component.FinanceFunctionsDialog
import kotlinx.coroutines.delay


@Composable
fun CalculatorLandscapeUI(
    calculatorViewModel: CalculatorViewModel
) {
    val showDialog = calculatorViewModel.showDialog
    val titleRes = calculatorViewModel.currentTitleRes
    val descriptionRes = calculatorViewModel.currentDescriptionRes
    val fieldLabels = calculatorViewModel.currentFieldLabels
    val fieldValues = calculatorViewModel.fieldValues

    val fromCurrency =calculatorViewModel.fromCurrency
    val toCurrency =calculatorViewModel.toCurrency
    val showConverterDialog = calculatorViewModel.showDialogConverter
    val amount =calculatorViewModel.amount
    val expression = calculatorViewModel.expression


    val buttonSpacing = 6.dp
    var isCursorVisible by remember { mutableStateOf(true) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    // Alternar la visibilidad del cursor cada 500 ms
    // Get cursor position
    val cursorOffset =
        textLayoutResult?.takeIf { it.layoutInput.text.text.length >= expression.length }
            ?.getCursorRect(expression.length)
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            isCursorVisible = !isCursorVisible
        }
    }

    // Definimos la grilla de botones ampliada para landscape
    val buttonRows = listOf(

        listOf("AC", "(", ")", "÷", "mod", "PVc", "FVc"),
        listOf("7", "8", "9", "×", "log", "PVs", "FVs"),
        listOf("4", "5", "6", "-", "√", "Rate", "Time"),
        listOf("1", "2", "3", "+", "xʸ", "Int", "PMT"),
        listOf("0", ".", "⌫", "±", "%", "FX", "=")
    )
        CurrencyDialogConverter(
            fromCurrency,
            toCurrency,
            onCurrenciesChange = calculatorViewModel::onCurrenciesChange,
            amount,
            {calculatorViewModel.updateAmount(it)},
            showConverterDialog,
            onConfirm = {calculatorViewModel.currencyConvert(fromCurrency,toCurrency)}
            ) { calculatorViewModel.closeCurrencyDialog()}

        FinanceFunctionsDialog(
            titleRes = titleRes,
            descriptionRes = descriptionRes,
            fieldLabels = fieldLabels,
            fieldValues = fieldValues,
            onFieldValuesChange = listOf(
                { calculatorViewModel.updateFieldValue(0, it) },
                { calculatorViewModel.updateFieldValue(1, it) },
                { calculatorViewModel.updateFieldValue(2, it) }
            ),
            showDialog = showDialog,
            onConfirm = {
                // Decide según el título qué calcular
                when (titleRes) {
                    R.string.presentValueCompoundTitle -> {
                        calculatorViewModel.calculatePresentValueCompound()
                    }
                    R.string.futureValueCompoundTitle -> {
                        calculatorViewModel.calculateFutureValueCompound()
                    }
                    R.string.presentValueSimpleTitle -> {
                        calculatorViewModel.calculatePresentValueSimple()
                    }
                    R.string.futureValueSimpleTitle -> {
                        calculatorViewModel.calculateFutureValueSimple()
                    }
                    R.string.loanPaymentTitle->{
                        calculatorViewModel.calculateLoanPaymentCompound()
                    }
                    R.string.interestEarnedTitle->{
                        calculatorViewModel.calculateInterestEarned()
                    }
                    R.string.numberOfPeriodsTitle->{
                        calculatorViewModel.calculatePeriodsRequired()
                    }
                    R.string.requiredInterestRateTitle->{
                        calculatorViewModel.calculateRequiredInterestRate()
                    }
                }
            },
            onDismiss = { calculatorViewModel.closeDialog() }
        )




    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxWidthDp = maxWidth * 0.95f

        Column(
            modifier = Modifier
                .widthIn(maxWidthDp)
                .fillMaxHeight()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing),
        ) {
            // Display más grande para landscape
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f)
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            ) {
                Text(
                    text = expression,
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.End,
                    color = colors.textColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    onTextLayout = { textLayoutResult = it }
                )
                if (isCursorVisible && cursorOffset != null) {
                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    cursorOffset.left.toInt(),
                                    cursorOffset.top.toInt()
                                )
                            }
                            .width(2.dp)
                            .height(with(LocalDensity.current) {
                                cursorOffset.height.toDp()
                            })
                            .background(colors.textColor)
                    )
                }

            }

            // Contenedor de botones en landscape
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.75f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                buttonRows.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                    ) {
                        row.forEach { symbol ->
                            val (bgStart, bgEnd, textStart, textEnd) = when (symbol) {
                                "AC", "⌫", "±" -> listOf(
                                    colors.initDelCalc,
                                    colors.targetDelCalc,
                                    colors.textInitOperatorCalc,
                                    colors.textTargetOperatorCalc
                                )

                                "+", "-", "×", "÷", "=", "(", ")", "%",
                                "log", "√", "xʸ", "mod" -> listOf(
                                    colors.initOperatorCalc,
                                    colors.targetOperatorCalc,
                                    colors.textInitOperatorCalc,
                                    colors.textTargetOperatorCalc
                                )

                                "PVc", "FVc", "PVs", "FVs", "Rate", "FX", "PMT", "Time", "Int" -> listOf(
                                    colors.financeCalColor,
                                    colors.financeCalColor,
                                    colors.textInitOperatorCalc,
                                    colors.textTargetOperatorCalc
                                )

                                else -> listOf(
                                    colors.buttonColorDefault,
                                    colors.buttonTransition,
                                    colors.textButtonColorDefault,
                                    colors.textTransition
                                )
                            }

                            CalculatorButton(
                                symbol = symbol,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clickable {
                                        when (symbol) {
                                            "AC" -> calculatorViewModel.clear()
                                            "⌫" -> calculatorViewModel.delete()
                                            "=" -> calculatorViewModel.evaluate()
                                            "±" -> calculatorViewModel.changeSign()
                                            "mod" -> calculatorViewModel.append(" mod ")
                                            "log" -> calculatorViewModel.append("log ")
                                            "√" -> calculatorViewModel.append("√")
                                            "xʸ" -> calculatorViewModel.append("^")
                                            "%" -> calculatorViewModel.append("%")
                                            "PVc" -> {
                                                    calculatorViewModel.openDialog(R.string.presentValueCompoundTitle,
                                                        R.string.presentValueCompoundDes,
                                                        listOf(R.string.futureValueTitle,
                                                            R.string.rateperiod,
                                                        R.string.numberOfPeriods))
                                            }
                                            "FVc" ->  {
                                                calculatorViewModel.openDialog(R.string.futureValueCompoundTitle,
                                                    R.string.futureValueCompoundDes,
                                                    listOf(R.string.presentValueTitle,
                                                        R.string.rateperiod,
                                                        R.string.numberOfPeriods))
                                            }
                                            "PVs" -> {
                                                calculatorViewModel.openDialog(R.string.presentValueSimpleTitle,
                                                    R.string.presentValueSimpleDes,
                                                    listOf(R.string.futureValueTitle,
                                                        R.string.rateperiod,
                                                        R.string.numberOfPeriods) )
                                            }
                                            "FVs" -> {
                                                calculatorViewModel.openDialog(R.string.futureValueSimpleTitle,
                                                    R.string.futureValueSimpleDes,
                                                    listOf(R.string.presentValueTitle,
                                                        R.string.rateperiod,
                                                        R.string.numberOfPeriods) )
                                            }
                                            "Rate" -> {
                                                calculatorViewModel.openDialog(R.string.requiredInterestRateTitle,
                                                    R.string.requiredInterestRateDes,
                                                    listOf(R.string.rate_inputPV,
                                                        R.string.rate_inputFV,
                                                        R.string.rate_inputPeriods))
                                            }
                                            "Time" -> {
                                                calculatorViewModel.openDialog(R.string.numberOfPeriodsTitle,
                                                    R.string.numberOfPeriodsDes,
                                                    listOf(R.string.time_inputPV,
                                                        R.string.time_inputFV,
                                                        R.string.time_inputRate))
                                            }
                                            "FX" -> {
                                                calculatorViewModel.onShowDialogConverter(true)
                                            }
                                            "PMT" -> {
                                                calculatorViewModel.openDialog(R.string.loanPaymentTitle,
                                                    R.string.loanPaymentDes,
                                                    listOf(R.string.presentValueTitle,
                                                        R.string.rateperiod,
                                                        R.string.numberOfPeriods) )
                                            }
                                            "Int" -> {
                                                calculatorViewModel.openDialog(R.string.interestEarnedTitle,
                                                    R.string.interestEarnedDes,
                                                    listOf(R.string.int_inputPV,
                                                        R.string.int_inputRate,
                                                        R.string.int_inputPeriods))
                                            }

                                            else -> calculatorViewModel.append(symbol)
                                        }
                                    },
                                initColorButton = bgStart,
                                targetColorButton = bgEnd,
                                initColorText = textStart,
                                targetColorText = textEnd,
                                true
                            )
                        }
                    }
                }


            }
        }
    }
}

