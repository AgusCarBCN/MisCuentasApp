package carnerero.agustin.cuentaappandroid.presentation.ui.calculator

import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component.CalculatorButton
import kotlinx.coroutines.delay


@Composable
fun CalculatorPortraitUI(
    viewModel: CalculatorViewModel,
) {
    val expression by viewModel.expression.observeAsState("")
    val buttonSpacing = 8.dp
    var isCursorVisible by remember { mutableStateOf(true) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    // Get cursor position
    val cursorOffset = textLayoutResult?.takeIf { it.layoutInput.text.text.length >= expression.length }
        ?.getCursorRect(expression.length)

    val context= LocalContext.current
    // Alternar la visibilidad del cursor cada 500 ms
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            isCursorVisible = !isCursorVisible
        }
    }

    // Definimos la grilla de botones
    val buttonRows = listOf(
        listOf("AC","(",")","÷"),
        listOf("7","8","9","×"),
        listOf("4","5","6","-"),
        listOf("1","2","3","+"),
        listOf("0",".","⌫","=")
    )

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val widthDp = maxWidth * 0.85f

        Column(
            modifier = Modifier
                .widthIn(widthDp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing),
        ) {

            // Display con cursor
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = expression,
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.End,
                    color = colors.textColor,
                    onTextLayout = { textLayoutResult = it }

                )
                // Get cursor position
                /*val cursorOffset = textLayoutResult?.takeIf { it.layoutInput.text.text.length >= expression.length }
                    ?.getCursorRect(expression.length)*/
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

            // Filas de botones
            buttonRows.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    row.forEach { symbol ->
                        val (bgStart, bgEnd, textStart, textEnd) = when(symbol) {
                            "AC", "⌫"-> listOf(
                                colors.initDelCalc,
                                colors.targetDelCalc,
                                colors.textInitOperatorCalc,
                                colors.textTargetOperatorCalc
                            )
                            "+","-","×","÷","=","(",")" -> listOf(
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




/*
@Composable

fun CalculatorPortraitUI(
    viewModel: CalculatorViewModel,
) {
    val expression by viewModel.expression.observeAsState("")
    val buttonSpacing = 8.dp
    var isCursorVisible by remember { mutableStateOf(true) }

    // Alternar la visibilidad del cursor cada 500 ms
    LaunchedEffect(Unit) {
        while (true) {
            delay(500) // Tiempo que el cursor está visible
            isCursorVisible = !isCursorVisible
        }
    }
    /*Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
    )*/
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val heightDp = maxHeight
        val widthDp=maxWidth*0.85f

        Column(
            modifier = Modifier
                .widthIn(widthDp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing),
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth(),
                reverseLayout = true
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 16.dp)
                    ) {
                        Text(
                            text = expression,
                            style = MaterialTheme.typography.displayMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Start,
                            color = colors.textColor
                        )

                        // Cursor
                        if (isCursorVisible) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp) // Ancho del cursor
                                    .height(36.dp) // Altura del cursor
                                    .background(colors.textColor) // Color del cursor
                                    .align(Alignment.CenterEnd) // Alinear al final del texto
                                    .padding(horizontal = 4.dp) // Espacio a los lados del cursor
                            )
                        }
                    }

                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    ,
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "AC",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.clear()
                        },
                    colors.initDelCalc,
                    colors.targetDelCalc,
                    colors.textInitOperatorCalc,
                    colors.textTargetOperatorCalc
                )
                CalculatorButton(
                    symbol = "(",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("(")
                        },
                    colors.initOperatorCalc,
                    colors.targetOperatorCalc,
                    colors.textInitOperatorCalc,
                    colors.textTargetOperatorCalc
                )
                CalculatorButton(
                    symbol = ")",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append(")")
                        },
                    colors.initOperatorCalc,
                    colors.targetOperatorCalc,
                    colors.textInitOperatorCalc,
                    colors.textTargetOperatorCalc
                )
                CalculatorButton(
                    symbol = "÷",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("÷")
                        },
                    colors.initOperatorCalc,
                    colors.targetOperatorCalc,
                    colors.textInitOperatorCalc,
                    colors.textTargetOperatorCalc
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    ,
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "7",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("7")
                        },
                    colors.buttonColorDefault,
                    colors.buttonTransition,
                    colors.textButtonColorDefault,
                    colors.textTransition
                )
                CalculatorButton(
                    symbol = "8",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("8")
                        },
                    colors.buttonColorDefault,
                    colors.buttonTransition,
                    colors.textButtonColorDefault,
                    colors.textTransition
                )
                CalculatorButton(
                    symbol = "9",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("9")
                        },
                    colors.buttonColorDefault,
                    colors.buttonTransition,
                    colors.textButtonColorDefault,
                    colors.textTransition
                )
                CalculatorButton(
                    symbol = "×",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("×")
                        },
                    colors.initOperatorCalc,
                    colors.targetOperatorCalc,
                    colors.textInitOperatorCalc,
                    colors.textTargetOperatorCalc
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                   ,
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "4",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("4")
                        },
                    colors.buttonColorDefault,
                    colors.buttonTransition,
                    colors.textButtonColorDefault,
                    colors.textTransition
                )
                CalculatorButton(
                    symbol = "5",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("5")
                        },
                    colors.buttonColorDefault,
                    colors.buttonTransition,
                    colors.textButtonColorDefault,
                    colors.textTransition
                )
                CalculatorButton(
                    symbol = "6",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("6")
                        },
                    colors.buttonColorDefault,
                    colors.buttonTransition,
                    colors.textButtonColorDefault,
                    colors.textTransition
                )
                CalculatorButton(
                    symbol = "-",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("-")
                        },
                    colors.initOperatorCalc,
                    colors.targetOperatorCalc,
                    colors.textInitOperatorCalc,
                    colors.textTargetOperatorCalc
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                     ,
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "1",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("1")
                        },
                    colors.buttonColorDefault,
                    colors.buttonTransition,
                    colors.textButtonColorDefault,
                    colors.textTransition
                )
                CalculatorButton(
                    symbol = "2",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("2")
                        },
                    colors.buttonColorDefault,
                    colors.buttonTransition,
                    colors.textButtonColorDefault,
                    colors.textTransition
                )
                CalculatorButton(
                    symbol = "3",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("3")
                        },
                    colors.buttonColorDefault,
                    colors.buttonTransition,
                    colors.textButtonColorDefault,
                    colors.textTransition
                )
                CalculatorButton(
                    symbol = "+",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("+")
                        },
                    colors.initOperatorCalc,
                    colors.targetOperatorCalc,
                    colors.textInitOperatorCalc,
                    colors.textTargetOperatorCalc
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ,
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "0",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("0")
                        },
                    colors.buttonColorDefault,
                    colors.buttonTransition,
                    colors.textButtonColorDefault,
                    colors.textTransition
                )
                CalculatorButton(
                    symbol = ".",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append(".")
                        },
                    colors.buttonColorDefault,
                    colors.buttonTransition,
                    colors.textButtonColorDefault,
                    colors.textTransition
                )
                CalculatorButton(
                    symbol = stringResource(id = R.string.calcback),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.delete()
                        },
                    colors.initDelCalc,
                    colors.targetDelCalc,
                    colors.textInitOperatorCalc,
                    colors.textTargetOperatorCalc
                )
                CalculatorButton(
                    symbol = "=",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.evaluate()
                        },
                    colors.initOperatorCalc,
                    colors.targetOperatorCalc,
                    colors.textInitOperatorCalc,
                    colors.textTargetOperatorCalc
                )
            }
        }
    }
}*/
