package carnerero.agustin.cuentaappandroid.presentation.ui.statistics.piechart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.ui.statistics.piechart.model.Legend
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.components.AccountSearchRecordsSelector
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.components.DatePickerSearchRecords
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField
import carnerero.agustin.cuentaappandroid.presentation.ui.statistics.piechart.model.PieChartUiEvents
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun PieChartScreen(
   pieChartViewModel: PieChartViewModel
) {
    val state by pieChartViewModel.uiState.collectAsStateWithLifecycle()
    val accountId=state.accountSelected
    val fromDate=state.fromDate
    val toDate=state.toDate
    val records=state.records
    val messageRecordsEmpty=stringResource(R.string.noentries)
    val isPortrait = orientation == OrientationApp.Portrait


    LaunchedEffect(accountId, fromDate, toDate) {
         pieChartViewModel.getPieChartData (accountId, fromDate, toDate)
    }
    LaunchedEffect(Unit) {
        pieChartViewModel.effect.collect { effect ->
        when(effect){
            PieChartEffects.MessageNoRecords -> SnackBarController.sendEvent(SnackBarEvent(messageRecordsEmpty))
        }
      }
    }

    // Agrupar por categoría
    val categoryTotals = remember(records) {
        records
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
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val layoutWidth = maxWidth * 0.85f
        val layoutHeight=maxHeight
        if (isPortrait) {
            Column(
                modifier = Modifier
                    .widthIn(layoutWidth)
                    .padding(top = dimens.extraLarge)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AccountSearchRecordsSelector(
                    title = R.string.selectanaccount,
                    state.accounts,
                    state.currencyCode,
                    {pieChartViewModel.onEventUser(PieChartUiEvents.OnAccountChange(it))},
                    false,
                    modifier = Modifier.width(300.dp)
                )

                Row(
                    modifier = Modifier
                        .width(360.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DatePickerSearchRecords(
                        modifier = Modifier.weight(0.5f)
                            .padding(10.dp),
                        R.string.fromdate,
                        state.showDatePickerFrom,
                        state.fromDate,
                        {date ->
                            pieChartViewModel.onEventUser(
                                PieChartUiEvents.OnSelectDate(DateField.FROM, date)
                            )
                        },
                        {visible ->
                            pieChartViewModel.onEventUser(
                                PieChartUiEvents.OnShowDatePicker(DateField.FROM, visible)
                            )
                        },
                        DateField.FROM
                    )
                    DatePickerSearchRecords(
                        modifier = Modifier.weight(0.5f)
                            .padding(10.dp),
                        R.string.todate,
                        state.showDatePickerTo,
                        state.toDate,
                        {date ->
                            pieChartViewModel.onEventUser(
                                PieChartUiEvents.OnSelectDate(DateField.TO, date)
                            )
                        },
                        {visible ->
                            pieChartViewModel.onEventUser(
                                PieChartUiEvents.OnShowDatePicker(DateField.TO, visible)
                            )
                        },
                        DateField.TO
                    )
                }
                if (incomeList.isNotEmpty()) {
                    HeadSetting(
                        title = stringResource(id = R.string.incomechart),
                        MaterialTheme.typography.headlineMedium
                    )
                    ChartPie(Modifier.fillMaxWidth().padding(top = dimens.extraLarge), incomeList)
                }

                if (expenseList.isNotEmpty()) {
                    HeadSetting(
                        title = stringResource(id = R.string.expensechart),
                        MaterialTheme.typography.headlineMedium
                    )
                    ChartPie(Modifier.fillMaxWidth().padding(top = dimens.extraLarge), expenseList)
                }
            }

        }
        else{
            Row(
            modifier = Modifier
                .widthIn(layoutWidth)
                .padding(top = dimens.extraLarge),
            horizontalArrangement = Arrangement.spacedBy(dimens.smallMedium),
                verticalAlignment = Alignment.CenterVertically
        ){
                ChartPie(Modifier.weight(1f), incomeList)
                ChartPie(Modifier.weight(1f), expenseList)
            }

        }

    }
}

@Composable
fun ChartPie(modifier:Modifier,listOfEntries: List<Pair<BigDecimal, Int>>) {

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

    Row(modifier ) {
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



