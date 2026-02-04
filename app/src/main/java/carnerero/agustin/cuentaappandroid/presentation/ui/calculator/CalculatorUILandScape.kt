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
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component.CalculatorButton
import kotlinx.coroutines.delay

@Composable
fun CalculatorUILandscape(
    viewModel: CalculatorViewModel,
) {
    val expression by viewModel.expression.observeAsState("")
    val buttonSpacing = 8.dp
    var isCursorVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            isCursorVisible = !isCursorVisible
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
    ) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Display - izquierda
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = expression,
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White
                    )
                    if (isCursorVisible) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(36.dp)
                                .background(Color.White)
                        )
                    }
                }
            }

            // Botones - derecha
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                val buttons = listOf(
                    listOf("AC","(",")","÷"),
                    listOf("7","8","9","×"),
                    listOf("4","5","6","-"),
                    listOf("1","2","3","+"),
                    listOf("0",".","⌫","=")
                )

                buttons.forEach { row ->
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                    ) {
                        row.forEach { symbol ->
                            val (bgStart, bgEnd, textStart, textEnd) = when(symbol) {
                                "AC","⌫" -> listOf(colors.initDelCalc, colors.targetDelCalc, colors.textInitOperatorCalc, colors.textTargetOperatorCalc)
                                "+","-","×","÷","=","(" ,")" -> listOf(colors.initOperatorCalc, colors.targetOperatorCalc, colors.textInitOperatorCalc, colors.textTargetOperatorCalc)
                                else -> listOf(colors.buttonColorDefault, colors.buttonTransition, colors.textButtonColorDefault, colors.textTransition)
                            }

                            CalculatorButton(
                                symbol = symbol,
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clickable {
                                        when(symbol) {
                                            "AC" -> viewModel.clear()
                                            "⌫" -> viewModel.delete()
                                            "=" -> viewModel.evaluate()
                                            else -> viewModel.append(symbol)
                                        }
                                    },
                                initColorButton = bgStart,
                                targetColorButton = bgEnd,
                                initColorText = textStart,
                                targetColorText = textEnd
                            )
                        }
                    }
                }
            }
        }
    }
}
