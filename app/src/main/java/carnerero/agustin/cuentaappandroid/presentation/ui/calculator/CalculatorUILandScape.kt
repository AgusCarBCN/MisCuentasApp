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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.network.model.Currency
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component.CalculatorButton
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component.CurrencyDialogConverter
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun CalculatorLandscapeUI(
    calculatorViewModel: CalculatorViewModel,
    accountsViewModel: AccountsViewModel
) {
    val fromCurrency by accountsViewModel.fromCurrency.observeAsState("EUR")
    val toCurrency by accountsViewModel.toCurrency.observeAsState("USD")
    val showConverterDialog by accountsViewModel.showDialogConverter.observeAsState(false)

    val expression by calculatorViewModel.expression.observeAsState("")
    val currencyCodeShowed by accountsViewModel.currencyCodeShowed.observeAsState("EUR")
    val currencyCodeSelected by accountsViewModel.currencyCodeSelected.observeAsState("EUR")
    val scope = rememberCoroutineScope()
    val message = stringResource(R.string.noImplement)
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
        listOf("AC", "(", ")", "÷", "mod", "PV", "FV"),
        listOf("7", "8", "9", "×", "log", "SI", "CI"),
        listOf("4", "5", "6", "-", "√", "DI", "DR"),
        listOf("1", "2", "3", "+", "xʸ", "FX", "PMT"),
        listOf("0", ".", "⌫", "±", "%", "EAR", "=")
    )
    //PV valor presente
    //FV valor futuro
    //PMT cuota prestamo
    //IS Interes simple
    //IC interes compuesto
    //DI Rendimiento por dividendo
    //PR Payout ratio
    //FX cambio de divisas o tipo de cambio
    //EAR Tasa de interés efectiva (Effective Interest Rate, EAR)


    if(showConverterDialog) {
        CurrencyDialogConverter(
            { accountsViewModel.onShowDialogConverter(false) },
            accountsViewModel = accountsViewModel,
        )
    }
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

                                "PV", "FV", "SI", "CI", "DI", "FX", "PMT", "EAR", "DR" -> listOf(
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
                                            "PV" -> notImplement("$message PV", scope)
                                            "FV" -> notImplement("$message FV", scope)
                                            "SI" -> notImplement("$message SI", scope)
                                            "CI" -> notImplement("$message SC", scope)
                                            "DI" -> notImplement("$message DI", scope)
                                            "DR" -> notImplement("$message DR", scope)
                                            "FX" -> {
                                                accountsViewModel.onShowDialogConverter(true)
                                                /*scope.launch(Dispatchers.IO) {
                                                    val ratio =
                                                        accountsViewModel.conversionCurrencyRate(
                                                            fromCurrency,
                                                            toCurrency
                                                        )
                                                    val number = expression.toBigDecimalOrNull()
                                                    if (number != null) {
                                                        val result = expression.plus(ratio)
                                                        calculatorViewModel.onExpressionChange(
                                                            result
                                                        )
                                                    }
                                                }*/
                                            }

                                            "PMT" -> notImplement("$message PMT", scope)
                                            "EAR" -> notImplement("$message EAR", scope)

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

private fun notImplement(
    message: String,
    scope: CoroutineScope
) {
    scope.launch(Dispatchers.Main) {
        SnackBarController.sendEvent(
            event = SnackBarEvent(
                message
            )
        )
    }
}

@Composable
private fun ModelCurrencyDialog(
    title: Int,
    message: Int,
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    if (showDialog) {
        AlertDialog(
            containerColor = colors.drawerColor,
            onDismissRequest = { onDismiss() },

            title = {
                Text(
                    stringResource(id = title),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textHeadColor
                )
            },

            text = {
                Text(
                    stringResource(id = message),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textColor
                )
            },
            confirmButton = {
                ModelButton(
                    text = stringResource(id = R.string.confirmButton),
                    MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(130.dp),
                    true,
                    onClickButton = {
                        onConfirm()
                    })
            },
            dismissButton = {
                ModelButton(
                    text = stringResource(id = R.string.cancelButton),
                    MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(130.dp),
                    true,
                    onClickButton = {
                        onDismiss()
                    })
            }
        )
    }
}