package carnerero.agustin.cuentaappandroid.presentation.ui.barchart

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import carnerero.agustin.cuentaappandroid.R

import carnerero.agustin.cuentaappandroid.presentation.ui.barchart.model.BarChartData
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.AccountSelector
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.ui.barchart.components.YearSelector
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.utils.Utils
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.Calendar
import kotlin.math.abs


@Composable

fun BarChartScreen(
    accountViewModel: AccountsViewModel,
    barChartViewModel: BarChartViewModel,
    settingViewModel: SettingViewModel

) {
    val year = Calendar.getInstance().get(Calendar.YEAR)
    val accountSelected by accountViewModel.accountSelected.observeAsState()
    val yearSelected by barChartViewModel.selectedYear.observeAsState()
    val data by barChartViewModel.barChartData.observeAsState(mutableListOf())
    val isDarkTheme by settingViewModel.switchDarkTheme.observeAsState(false)
    val context= LocalContext.current
    val idAccount = accountSelected?.id ?: 1
    Log.d("account",accountSelected?.name?:"")
    LaunchedEffect(idAccount,yearSelected) {
        barChartViewModel.barChartDataByMonth(idAccount,yearSelected?:year.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

        ) {

        Row(
            modifier = Modifier
                .width(300.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AccountSelector(200, 10, stringResource(id = R.string.account), accountViewModel)
            YearSelector(barChartViewModel)
        }

        HeadSetting(title = stringResource(id = R.string.alloption), MaterialTheme.typography.headlineMedium)
        BarChart(context, data,isDarkTheme)
        HeadSetting(title = stringResource(id = R.string.result), MaterialTheme.typography.headlineMedium)
        BarChartResult(context, data,isDarkTheme)
        Table(accountViewModel,data)
    }

}


@Composable
fun BarChart(
    context: Context,
    data: MutableList<BarChartData>,
    isDarkTheme: Boolean
) {
    val months= mutableListOf<String>()
    data.forEach {element->
        months.add(stringResource(id =element.month ))
    }

    val incomeLabel= stringResource(id = R.string.incomechart)
    val expensesLabel= stringResource(id = R.string.expensechart)
    AndroidView(
        factory = { ctx ->
            BarChart(ctx).apply {
                // Set up chart properties
                setBackgroundColor(if(isDarkTheme) ContextCompat.getColor(context, R.color.darkBrown)
                else ContextCompat.getColor(context, R.color.lightYellow))
                setGridBackgroundColor(if(isDarkTheme) ContextCompat.getColor(context, R.color.darkBrown)
                else ContextCompat.getColor(context, R.color.lightYellow))
                description.isEnabled = false
                setTouchEnabled(true) // Correct usage to enable touch
                setDrawGridBackground(false)
                axisRight.isEnabled = false// Disable right Y axis

                // Setup the X-axis
                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(months)
                    granularity = 1f
                    isGranularityEnabled = true // Asegura que la granularidad se respete
                    labelCount = months.size // Establece el número de etiquetas en el total de meses
                    position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    textColor = if(isDarkTheme) ContextCompat.getColor(context, R.color.lightYellow)
                    else ContextCompat.getColor(context, R.color.darkBrown)
                    axisLineColor = if(isDarkTheme) ContextCompat.getColor(context, R.color.lightYellow)
                    else ContextCompat.getColor(context, R.color.darkBrown)
                    textSize = 10f
                }


                // Configuración del eje Y izquierdo
                axisLeft.apply {
                    textColor =  if(isDarkTheme) ContextCompat.getColor(context, R.color.lightYellow)
                    else ContextCompat.getColor(context, R.color.darkBrown)
                    axisLineColor = if(isDarkTheme) ContextCompat.getColor(context, R.color.lightYellow)
                    else ContextCompat.getColor(context, R.color.darkBrown)
                    setDrawGridLines(false) // Habilitar líneas de cuadrícula en el eje Y
                    gridColor =  if(isDarkTheme) ContextCompat.getColor(context, R.color.lightYellow)
                    else ContextCompat.getColor(context, R.color.darkBrown)
                    textSize = 12f
                    axisMinimum = 0f

                }
                // Configuración de la leyenda
                legend.apply {
                    isEnabled = true // Habilitar la leyenda
                    textColor =  if(isDarkTheme) ContextCompat.getColor(context, R.color.lightYellow)
                    else ContextCompat.getColor(context, R.color.darkBrown)
                    textSize = 14f // Establecer el tamaño del texto de la leyenda
                    form = Legend.LegendForm.CIRCLE // Forma de la leyenda (puede ser SQUARE, CIRCLE, LINE)
                    formSize = 10f // Tamaño de la forma en la leyenda
                    horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT // Alineación horizontal
                    verticalAlignment = Legend.LegendVerticalAlignment.TOP // Alineación vertical
                    orientation = Legend.LegendOrientation.HORIZONTAL // Orientación de la leyenda
                }

            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(410.dp)
            .padding(5.dp)
            ,
        update = { chart ->
            // Prepare bar entries for each category
            val barEntriesIncomes = data.mapIndexed { index, value ->
                BarEntry(index.toFloat(), value.incomes)
            }
            val barEntriesExpenses = data.mapIndexed { index, value ->
                BarEntry(index.toFloat(), abs(value.expenses))
            }


            // Create datasets
            val incomesDataSet = BarDataSet(barEntriesIncomes,incomeLabel ).apply {
                color = if(isDarkTheme)ContextCompat.getColor(context, R.color.darkGreenPistacho)
                else ContextCompat.getColor(context, R.color.darkgreen)
                setDrawValues(false)


            }
            val expensesDataSet = BarDataSet(barEntriesExpenses, expensesLabel).apply {
                color = if(isDarkTheme)ContextCompat.getColor(context, R.color.coralred)
                else ContextCompat.getColor(context, R.color.red)
                setDrawValues(false)

            }

            // Create bar data
            val dataBarChart = BarData(incomesDataSet, expensesDataSet)
            chart.data = dataBarChart

            // Refresh chart
            chart.invalidate()
        }
    )
}

@Composable
fun BarChartResult(
    context: Context,
    data: MutableList<BarChartData>,
    isDarkTheme: Boolean
) {
    val months= mutableListOf<String>()
    data.forEach {element->
        months.add(stringResource(id =element.month ))
    }
    Log.d("data",data.toString())
    val resultLabel= stringResource(id = R.string.result)

    AndroidView(
        factory = { ctx ->
            BarChart(ctx).apply {
                // Set up chart properties
                setBackgroundColor(if(isDarkTheme) ContextCompat.getColor(context, R.color.darkBrown)
                else ContextCompat.getColor(context, R.color.lightYellow))
                setGridBackgroundColor(if(isDarkTheme) ContextCompat.getColor(context, R.color.darkBrown)
                else ContextCompat.getColor(context, R.color.lightYellow))
                description.isEnabled = false
                setTouchEnabled(true) // Correct usage to enable touch
                setDrawGridBackground(false)
                axisRight.isEnabled = false// Disable right Y axis

                // Setup the X-axis
                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(months)
                    granularity = 1f
                    isGranularityEnabled = true // Asegura que la granularidad se respete
                    labelCount = months.size // Establece el número de etiquetas en el total de meses
                    position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    textColor = if(isDarkTheme) ContextCompat.getColor(context, R.color.lightYellow)
                    else ContextCompat.getColor(context, R.color.darkBrown)
                    axisLineColor = if(isDarkTheme) ContextCompat.getColor(context, R.color.lightYellow)
                    else ContextCompat.getColor(context, R.color.darkBrown)
                    textSize = 10f
                }


                // Configuración del eje Y izquierdo
                axisLeft.apply {
                    textColor =  if(isDarkTheme) ContextCompat.getColor(context, R.color.lightYellow)
                    else ContextCompat.getColor(context, R.color.darkBrown)
                    axisLineColor = if(isDarkTheme) ContextCompat.getColor(context, R.color.lightYellow)
                    else ContextCompat.getColor(context, R.color.darkBrown)
                    setDrawGridLines(false) // Habilitar líneas de cuadrícula en el eje Y
                    gridColor =  if(isDarkTheme) ContextCompat.getColor(context, R.color.lightYellow)
                    else ContextCompat.getColor(context, R.color.darkBrown)
                    textSize = 12f

                }
                // Configuración de la leyenda
                legend.apply {
                    isEnabled = true // Habilitar la leyenda
                    textColor =  if(isDarkTheme) ContextCompat.getColor(context, R.color.lightYellow)
                    else ContextCompat.getColor(context, R.color.darkBrown)
                    textSize = 14f // Establecer el tamaño del texto de la leyenda
                    form = Legend.LegendForm.CIRCLE // Forma de la leyenda (puede ser SQUARE, CIRCLE, LINE)
                    formSize = 10f // Tamaño de la forma en la leyenda
                    horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT // Alineación horizontal
                    verticalAlignment = Legend.LegendVerticalAlignment.TOP // Alineación vertical
                    orientation = Legend.LegendOrientation.HORIZONTAL // Orientación de la leyenda
                }

            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(410.dp)
            .padding(5.dp)
        ,
        update = { chart ->
            // Prepare bar entries for each category
            val barEntriesResult = data.mapIndexed { index, value ->
                BarEntry(index.toFloat(), value.result)

            }

            // Create datasets
            val resultDataSet = BarDataSet(barEntriesResult,resultLabel).apply {
                color = if(isDarkTheme)ContextCompat.getColor(context, R.color.lightblue)
                else ContextCompat.getColor(context, R.color.blue)
                setDrawValues(false)
            }

            // Create bar data
            val dataBarChart = BarData(resultDataSet)
            chart.data = dataBarChart
            // Refresh chart
            chart.invalidate()

        }
    )
}

@Composable
fun Table(accountsViewModel: AccountsViewModel, data: MutableList<BarChartData>) {

    val currencyCodeSelected by accountsViewModel.currencyCodeSelected.observeAsState("EUR")
    // Encabezados de la tabla
    val header = listOf(
        stringResource(id = R.string.months),
        stringResource(id = R.string.incomechart),
        stringResource(id = R.string.expensechart),
        stringResource(id = R.string.result)
    )

    Column(modifier = Modifier
        .padding(15.dp)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        // Encabezados
        Row(
            modifier = Modifier.fillMaxWidth().
            padding(start=15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            header.forEach { item ->
                Text(
                    text = item,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = LocalCustomColorsPalette.current.textHeadColor
                )
            }
        }
        SpacerTable()

        // Datos
        data.forEach { element ->
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start=15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,

            ) {
                Text(
                    text = stringResource(element.month), // Obtener el nombre del mes
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalCustomColorsPalette.current.textColor
                )
                Text(
                    text = Utils.numberFormat(element.incomes.toBigDecimal(),currencyCodeSelected), // Formato para ingresos
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalCustomColorsPalette.current.incomeColor
                )
                Text(
                    text = Utils.numberFormat(element.expenses.toBigDecimal(),currencyCodeSelected), // Formato para gastos
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalCustomColorsPalette.current.expenseColor
                )
                Text(
                    text = Utils.numberFormat(element.result.toBigDecimal(),currencyCodeSelected), // Formato para resultado
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if(element.result>=0) LocalCustomColorsPalette.current.incomeColor
                    else LocalCustomColorsPalette.current.expenseColor
                )
            }
            Spacer(modifier = Modifier.height(4.dp)) // Espaciado entre filas
        }
    }
}

@Composable
private fun SpacerTable() {
    Spacer(
        modifier = Modifier
            .width(360.dp)
            .padding(10.dp)
            .background(LocalCustomColorsPalette.current.textColor.copy(alpha = 0.2f)) // Ajusta el valor alpha para la opacidad
            .height(1.dp) // Cambié a height para que la línea sea horizontal, ajusta si es necesario
    )
}