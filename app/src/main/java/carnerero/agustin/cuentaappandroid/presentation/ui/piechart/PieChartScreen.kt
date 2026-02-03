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
import androidx.compose.ui.graphics.drawscope.Fill
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
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Date

@Composable
fun PieChartScreen(
    entriesViewModel: EntriesViewModel,
    accountViewModel: AccountsViewModel,
    searchViewModel: SearchViewModel
) {

    val listOfEntries by entriesViewModel.listOfEntriesDTO.collectAsState()
    val accountSelected by accountViewModel.accountSelected.observeAsState()
    val toDate by searchViewModel.selectedToDate.observeAsState(Date().dateFormat())
    val fromDate by searchViewModel.selectedFromDate.observeAsState(Date().dateFormat())

    val idAccount = accountSelected?.id ?: 1

    LaunchedEffect(idAccount, fromDate, toDate) {
        entriesViewModel.getAllEntriesByDateDTO(idAccount,fromDate,toDate)

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
            .map { it.second to it.first }
    }

    // Gastos
    val expenseList = remember(categoryTotals) {
        categoryTotals
            .filter { it.second < BigDecimal.ZERO }
            .map { it.second.abs() to it.first }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AccountSelector(300, 20, stringResource(id = R.string.selectanaccount), accountViewModel)
        Row(
            modifier = Modifier
                .width(360.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DatePickerSearch(
                modifier = Modifier.weight(0.5f)
                    .padding(10.dp),
                R.string.fromdate,
                searchViewModel,
                true
            )
            DatePickerSearch(
                modifier = Modifier.weight(0.5f)
                    .padding(10.dp),
                R.string.todate,
                searchViewModel,
                false
            )
        }
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
fun ChartPie(listOfEntries: List<Pair<BigDecimal, Int>>) {

    if (listOfEntries.isEmpty()) return

    val initAngle = -90f
    val isDarkTheme = isSystemInDarkTheme()
    // Graphics colors
    val chartColors=generateChartColors(listOfEntries.size,isDarkTheme)
    // Total como BigDecimal
    val total = remember(listOfEntries) {
        listOfEntries.fold(BigDecimal.ZERO) { acc, entry ->
            acc + entry.first
        }
    }
    if (total == BigDecimal.ZERO) return

    val legends = remember(listOfEntries, chartColors) {
        listOfEntries.mapIndexed { index, entry ->
            val percent = listOfEntries[index].first
                    .divide(total, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal(100))
                    .toInt()
            Legend(
                label = entry.second,
                percent="${percent}%",
                color = chartColors[index]
            )
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
                    currentAngle + sweepAngle / 2
                    drawArc(
                        color = chartColors[index],
                        startAngle = currentAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        style = Fill
                    )

                    currentAngle += sweepAngle
                }
            }
        }

        Column(modifier = Modifier.weight(0.35f)) {
            legends.forEach {
                LegendItem(it.color, it.label,it.percent)
            }
        }
    }
}



fun generateChartColors(
    itemCount: Int,
    isDarkTheme: Boolean
): List<Color> {

    val saturation = if (isDarkTheme) 0.65f else 0.55f
    val lightness = if (isDarkTheme) 0.55f else 0.45f

    return List(itemCount) { index ->
        val hue = (index * 360f / itemCount) % 360f
        Color.hsl(
            hue = hue,
            saturation = saturation,
            lightness = lightness
        )
    }
}

@Composable
fun LegendItem(color: Color,
               label: Int,
               percent: String) {
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
            text = stringResource(label)+" "+percent,
            color = colors.textColor,
            style=MaterialTheme.typography.bodyMedium

        )
    }
}



