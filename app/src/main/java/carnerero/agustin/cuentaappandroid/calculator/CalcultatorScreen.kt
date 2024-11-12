package carnerero.agustin.cuentaappandroid.calculator

import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.components.CalculatorButton
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
import kotlinx.coroutines.delay

@Composable

fun CalculatorUI(
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColorsPalette.current.backgroundPrimary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Start,
                            color = LocalCustomColorsPalette.current.textColor
                        )

                        // Cursor
                        if (isCursorVisible) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp) // Ancho del cursor
                                    .height(48.dp) // Altura del cursor
                                    .background(LocalCustomColorsPalette.current.textColor) // Color del cursor
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
                    LocalCustomColorsPalette.current.initDelCalc,
                    LocalCustomColorsPalette.current.targetDelCalc,
                    LocalCustomColorsPalette.current.textInitOperatorCalc,
                    LocalCustomColorsPalette.current.textTargetOperatorCalc
                )
                CalculatorButton(
                    symbol = "(",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("(")
                        },
                    LocalCustomColorsPalette.current.initOperatorCalc,
                    LocalCustomColorsPalette.current.targetOperatorCalc,
                    LocalCustomColorsPalette.current.textInitOperatorCalc,
                    LocalCustomColorsPalette.current.textTargetOperatorCalc
                )
                CalculatorButton(
                    symbol = ")",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append(")")
                        },
                    LocalCustomColorsPalette.current.initOperatorCalc,
                    LocalCustomColorsPalette.current.targetOperatorCalc,
                    LocalCustomColorsPalette.current.textInitOperatorCalc,
                    LocalCustomColorsPalette.current.textTargetOperatorCalc
                )
                CalculatorButton(
                    symbol = "÷",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("÷")
                        },
                    LocalCustomColorsPalette.current.initOperatorCalc,
                    LocalCustomColorsPalette.current.targetOperatorCalc,
                    LocalCustomColorsPalette.current.textInitOperatorCalc,
                    LocalCustomColorsPalette.current.textTargetOperatorCalc
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
                    LocalCustomColorsPalette.current.buttonColorDefault,
                    LocalCustomColorsPalette.current.buttonTransition,
                    LocalCustomColorsPalette.current.textButtonColorDefault,
                    LocalCustomColorsPalette.current.textTransition
                )
                CalculatorButton(
                    symbol = "8",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("8")
                        },
                    LocalCustomColorsPalette.current.buttonColorDefault,
                    LocalCustomColorsPalette.current.buttonTransition,
                    LocalCustomColorsPalette.current.textButtonColorDefault,
                    LocalCustomColorsPalette.current.textTransition
                )
                CalculatorButton(
                    symbol = "9",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("9")
                        },
                    LocalCustomColorsPalette.current.buttonColorDefault,
                    LocalCustomColorsPalette.current.buttonTransition,
                    LocalCustomColorsPalette.current.textButtonColorDefault,
                    LocalCustomColorsPalette.current.textTransition
                )
                CalculatorButton(
                    symbol = "×",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("×")
                        },
                    LocalCustomColorsPalette.current.initOperatorCalc,
                    LocalCustomColorsPalette.current.targetOperatorCalc,
                    LocalCustomColorsPalette.current.textInitOperatorCalc,
                    LocalCustomColorsPalette.current.textTargetOperatorCalc
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
                    LocalCustomColorsPalette.current.buttonColorDefault,
                    LocalCustomColorsPalette.current.buttonTransition,
                    LocalCustomColorsPalette.current.textButtonColorDefault,
                    LocalCustomColorsPalette.current.textTransition
                )
                CalculatorButton(
                    symbol = "5",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("5")
                        },
                    LocalCustomColorsPalette.current.buttonColorDefault,
                    LocalCustomColorsPalette.current.buttonTransition,
                    LocalCustomColorsPalette.current.textButtonColorDefault,
                    LocalCustomColorsPalette.current.textTransition
                )
                CalculatorButton(
                    symbol = "6",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("6")
                        },
                    LocalCustomColorsPalette.current.buttonColorDefault,
                    LocalCustomColorsPalette.current.buttonTransition,
                    LocalCustomColorsPalette.current.textButtonColorDefault,
                    LocalCustomColorsPalette.current.textTransition
                )
                CalculatorButton(
                    symbol = "-",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("-")
                        },
                    LocalCustomColorsPalette.current.initOperatorCalc,
                    LocalCustomColorsPalette.current.targetOperatorCalc,
                    LocalCustomColorsPalette.current.textInitOperatorCalc,
                    LocalCustomColorsPalette.current.textTargetOperatorCalc
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
                    LocalCustomColorsPalette.current.buttonColorDefault,
                    LocalCustomColorsPalette.current.buttonTransition,
                    LocalCustomColorsPalette.current.textButtonColorDefault,
                    LocalCustomColorsPalette.current.textTransition
                )
                CalculatorButton(
                    symbol = "2",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("2")
                        },
                    LocalCustomColorsPalette.current.buttonColorDefault,
                    LocalCustomColorsPalette.current.buttonTransition,
                    LocalCustomColorsPalette.current.textButtonColorDefault,
                    LocalCustomColorsPalette.current.textTransition
                )
                CalculatorButton(
                    symbol = "3",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("3")
                        },
                    LocalCustomColorsPalette.current.buttonColorDefault,
                    LocalCustomColorsPalette.current.buttonTransition,
                    LocalCustomColorsPalette.current.textButtonColorDefault,
                    LocalCustomColorsPalette.current.textTransition
                )
                CalculatorButton(
                    symbol = "+",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("+")
                        },
                    LocalCustomColorsPalette.current.initOperatorCalc,
                    LocalCustomColorsPalette.current.targetOperatorCalc,
                    LocalCustomColorsPalette.current.textInitOperatorCalc,
                    LocalCustomColorsPalette.current.textTargetOperatorCalc
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
                    LocalCustomColorsPalette.current.buttonColorDefault,
                    LocalCustomColorsPalette.current.buttonTransition,
                    LocalCustomColorsPalette.current.textButtonColorDefault,
                    LocalCustomColorsPalette.current.textTransition
                )
                CalculatorButton(
                    symbol = ".",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append(".")
                        },
                    LocalCustomColorsPalette.current.buttonColorDefault,
                    LocalCustomColorsPalette.current.buttonTransition,
                    LocalCustomColorsPalette.current.textButtonColorDefault,
                    LocalCustomColorsPalette.current.textTransition
                )
                CalculatorButton(
                    symbol = stringResource(id = R.string.calcback),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.delete()
                        },
                    LocalCustomColorsPalette.current.initDelCalc,
                    LocalCustomColorsPalette.current.targetDelCalc,
                    LocalCustomColorsPalette.current.textInitOperatorCalc,
                    LocalCustomColorsPalette.current.textTargetOperatorCalc
                )
                CalculatorButton(
                    symbol = "=",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.evaluate()
                        },
                    LocalCustomColorsPalette.current.initOperatorCalc,
                    LocalCustomColorsPalette.current.targetOperatorCalc,
                    LocalCustomColorsPalette.current.textInitOperatorCalc,
                    LocalCustomColorsPalette.current.textTargetOperatorCalc
                )
            }
        }
    }
}
