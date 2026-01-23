package carnerero.agustin.cuentaappandroid.presentation.ui.piechart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R

import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.AccountSelector
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.DatePickerSearch
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.piechart.model.Legend
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Date
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun PieChartScreen(
    entriesViewModel: EntriesViewModel,
    accountViewModel: AccountsViewModel,
    searchViewModel: SearchViewModel
) {

    val listOfEntries by entriesViewModel.listOfEntriesDTO.collectAsState()
    val accountSelected by accountViewModel.accountSelected.observeAsState()

    val toDate by searchViewModel.selectedToDate.observeAsState(Date().dateFormat())
    val fromDate by searchViewModel.selectedFromDate.observeAsState("01/01/1900")

    val idAccount = accountSelected?.id ?: 1

    LaunchedEffect(idAccount, fromDate, toDate) {
        entriesViewModel.getFilteredEntries(
            idAccount,
            "",
            fromDate,
            toDate,
            BigDecimal.ZERO,
            BigDecimal("1E10"),
            2
        )
    }

    // Agrupar por categoría
    val categoryTotals = remember(listOfEntries) {
        listOfEntries
            .groupBy { it.nameResource }
            .mapValues { (_, entries) ->
                entries.sumOf { it.amount }
            }
            .toList()
    }

    // Ingresos
    val incomeList = remember(categoryTotals) {
        categoryTotals
            .filter { it.second > BigDecimal.ZERO }
            .map { it.second to it.first.toString() }
    }

    // Gastos
    val expenseList = remember(categoryTotals) {
        categoryTotals
            .filter { it.second < BigDecimal.ZERO }
            .map { it.second.abs() to it.first.toString() }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AccountSelector(300, 20, stringResource(id = R.string.selectanaccount), accountViewModel)

        if (incomeList.isNotEmpty()) {
            HeadSetting(
                title = stringResource(id = R.string.incomechart),
                MaterialTheme.typography.headlineMedium
            )
            ChartPie(incomeList)
        }

        if (expenseList.isNotEmpty()) {
            HeadSetting(
                title = stringResource(id = R.string.expensechart),
                MaterialTheme.typography.headlineMedium
            )
            ChartPie(expenseList)
        }

        if (incomeList.isEmpty() && expenseList.isEmpty()) {
            Text(
                modifier = Modifier.padding(40.dp),
                text = stringResource(id = R.string.noentries),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ChartPie(listOfEntries: List<Pair<BigDecimal, String>>) {

    if (listOfEntries.isEmpty()) return

    val initAngle = -90f
    val textColorPieChart = Color.Black
    val isDarkTheme = isSystemInDarkTheme()

    // Total como BigDecimal
    val total = remember(listOfEntries) {
        listOfEntries.fold(BigDecimal.ZERO) { acc, entry ->
            acc + entry.first
        }
    }

    if (total == BigDecimal.ZERO) return

    // Colores estables
    val colors = remember(listOfEntries, isDarkTheme) {
        listOfEntries.map { colorGenerator(isDarkTheme) }
    }

    // Leyendas (Strings ya resueltos)
    val legends = remember(listOfEntries, colors) {
        listOfEntries.mapIndexed { index, entry ->
            Legend(entry.second, colors[index])
        }
    }

    Row(modifier = Modifier.padding(10.dp)) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .weight(0.65f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
            ) {
                var currentAngle = initAngle

                listOfEntries.forEachIndexed { index, element ->

                    // (valor / total) * 360 → BigDecimal
                    val sweepAngle =
                        element.first
                            .divide(total, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal(360))
                            .toFloat()

                    val midAngle = currentAngle + sweepAngle / 2

                    drawArc(
                        color = colors[index],
                        startAngle = currentAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        style = Fill
                    )

                    val percent =
                        element.first
                            .divide(total, 2, RoundingMode.HALF_UP)
                            .multiply(BigDecimal(100))
                            .toInt()

                    val textX = center.x + (size.width / 3) *
                            cos(Math.toRadians(midAngle.toDouble())).toFloat()
                    val textY = center.y + (size.height / 3) *
                            sin(Math.toRadians(midAngle.toDouble())).toFloat()

                    drawContext.canvas.nativeCanvas.drawText(
                        "$percent%",
                        textX,
                        textY,
                        Paint().asFrameworkPaint().apply {
                            color = textColorPieChart.toArgb()
                            textSize = 35f
                            isFakeBoldText = true
                        }
                    )

                    currentAngle += sweepAngle
                }
            }
        }

        Column(modifier = Modifier.weight(0.35f)) {
            legends.forEach {
                LegendItem(it.color, it.legend)
            }
        }
    }
}


fun colorGenerator(isDarkTheme: Boolean): Color {
    val random = Random.Default
    return if (isDarkTheme) {
        // Genera colores vivos que destacan sobre un fondo oscuro
        val red = random.nextInt(0, 256) // Colores brillantes
        val green = random.nextInt(0, 256)
        val blue = random.nextInt(0, 256)
        Color(red, green, blue)
    } else {
        // Genera colores pastel que destacan sobre un fondo claro
        val red = random.nextInt(0, 256) // Colores pastel más claros
        val green = random.nextInt(0, 256)
        val blue = random.nextInt(0, 256)
        Color(red, green, blue)
    }

}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(4.dp)
    ) {
        // Cuadro de color de la leyenda
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(color)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Texto de la leyenda
        Text(
            text = label,
            color = LocalCustomColorsPalette.current.textColor,
            style=MaterialTheme.typography.bodyMedium

        )
    }
}



