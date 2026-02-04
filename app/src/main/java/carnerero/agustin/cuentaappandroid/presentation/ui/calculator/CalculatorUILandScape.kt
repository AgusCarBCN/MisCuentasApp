package carnerero.agustin.cuentaappandroid.presentation.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component.CalculatorButton
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component.CalculatorButton2
import kotlinx.coroutines.delay


@Composable
fun CalculatorLandscapeUI(
    viewModel: CalculatorViewModel,
) {
    val expression by viewModel.expression.observeAsState("")
    val buttonSpacing = 6.dp
    var isCursorVisible by remember { mutableStateOf(true) }
    val context = LocalContext.current

    // Alternar la visibilidad del cursor cada 500 ms
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            isCursorVisible = !isCursorVisible
        }
    }

    // Definimos la grilla de botones ampliada para landscape
    val buttonRows = listOf(
        listOf("AC", "(", ")", "÷", "sin", "cos", "tan"),
        listOf("7", "8", "9", "×", "log", "ln", "√"),
        listOf("4", "5", "6", "-", "x²", "x³", "xʸ"),
        listOf("1", "2", "3", "+", "π", "e", "("),
        listOf("0", ".", "⌫", "=", ")", "%", "±")
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
                    style = MaterialTheme.typography.displayLarge,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Start,
                    color = colors.textColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                if (isCursorVisible) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(48.dp)
                            .background(colors.textColor)
                            .align(Alignment.CenterEnd)
                            .padding(horizontal = 6.dp)
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
                            val (bgStart, bgEnd, textStart, textEnd) = when(symbol) {
                                "AC", "⌫", "±" -> listOf(
                                    colors.initDelCalc,
                                    colors.targetDelCalc,
                                    colors.textInitOperatorCalc,
                                    colors.textTargetOperatorCalc
                                )
                                "+", "-", "×", "÷", "=", "(", ")", "%" -> listOf(
                                    colors.initOperatorCalc,
                                    colors.targetOperatorCalc,
                                    colors.textInitOperatorCalc,
                                    colors.textTargetOperatorCalc
                                )
                                "sin", "cos", "tan", "log", "ln", "√", "x²", "x³", "xʸ", "π", "e" -> listOf(
                                    colors.initOperatorCalc,
                                    colors.targetOperatorCalc,
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
                                        when(symbol) {
                                            "AC" -> viewModel.clear()
                                            "⌫" -> viewModel.delete()
                                            "=" -> viewModel.evaluate()
                                            "±" -> viewModel.evaluate()
                                            "sin" -> viewModel.append("sin(")
                                            "cos" -> viewModel.append("cos(")
                                            "tan" -> viewModel.append("tan(")
                                            "log" -> viewModel.append("log(")
                                            "ln" -> viewModel.append("ln(")
                                            "√" -> viewModel.append("√(")
                                            "x²" -> viewModel.append("²")
                                            "x³" -> viewModel.append("³")
                                            "xʸ" -> viewModel.append("^")
                                            "π" -> viewModel.append("π")
                                            "e" -> viewModel.append("e")
                                            "%" -> viewModel.append("%")
                                            else -> viewModel.append(symbol)
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