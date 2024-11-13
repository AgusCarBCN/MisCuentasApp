package carnerero.agustin.cuentaappandroid.piechart

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carnerero.agustin.cuentaappandroid.R

import carnerero.agustin.cuentaappandroid.components.AccountSelector
import carnerero.agustin.cuentaappandroid.components.DatePickerSearch
import carnerero.agustin.cuentaappandroid.components.HeadSetting
import carnerero.agustin.cuentaappandroid.createaccounts.view.AccountsViewModel
import carnerero.agustin.cuentaappandroid.newamount.view.EntriesViewModel
import carnerero.agustin.cuentaappandroid.piechart.model.Legend
import carnerero.agustin.cuentaappandroid.search.SearchViewModel
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.utils.dateFormat
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

    val getTotalIncomes by entriesViewModel.totalIncomes.observeAsState(0.0)
    val getTotalExpenses by entriesViewModel.totalExpenses.observeAsState(0.0)
    val toDate by searchViewModel.selectedToDate.observeAsState(Date().dateFormat())
    val fromDate by searchViewModel.selectedFromDate.observeAsState("01/01/1900")
    val listOfEntries by entriesViewModel.listOfEntriesDTO.collectAsState()
    val accountSelected by accountViewModel.accountSelected.observeAsState()

    val idAccount = accountSelected?.id ?: 1

    //LaunchedEffect para ejecutar lógica dependiente de la UI o que deba reaccionar a cambios
    // en la composición
    LaunchedEffect(idAccount, fromDate, toDate) {
        entriesViewModel.getTotalByDate(idAccount, fromDate, toDate)
        entriesViewModel.getFilteredEntries(
            idAccount,
            "",
            fromDate,
            toDate,
            0.0,
            Double.MAX_VALUE,
            2
        )
    }

    /* Agrupa las entradas por el nombre de la categoría. Esto crea un mapa donde la clave
    es el nombre de la categoría, y el valor es una lista de entradas que pertenecen a esa categoría.*/
    val groupedEntriesByCategoryName = listOfEntries.groupBy { it.nameResource }

    /*Calcula el total de cada categoría. Usamos `mapValues` para transformar los valores del mapa anterior.
   En lugar de la lista de entradas de cada categoría, ahora el valor será la suma de los montos de esas entradas.*/
    val categoryTotals = groupedEntriesByCategoryName.mapValues { (_, entries) ->
        entries.sumOf { it.amount } // Suma el monto de cada entrada en la lista de esa categoría
    }

    /* Convierte el mapa de totales de categorías en una lista de pares clave-valor.
    Cada elemento de la lista será un par (categoryName, totalAmount), que facilita el acceso secuencial*/
    val categoryList = categoryTotals.toList()

    val incomeList: MutableList<Pair<Float, String>> = mutableListOf()
    val expenseList: MutableList<Pair<Float, String>> = mutableListOf()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .verticalScroll(
                rememberScrollState()
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        AccountSelector(300, 20, stringResource(id = R.string.selectanaccount), accountViewModel)
        HeadSetting(title = stringResource(id = R.string.daterange), MaterialTheme.typography.headlineSmall)
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

        //Calculate percent of entries and adding to percentsList
        categoryList.forEach { category ->
            if (category.second >= 0) {
                incomeList.add((category.second / getTotalIncomes).toFloat() to stringResource(id = category.first))

            } else {
                expenseList.add((category.second / getTotalExpenses).toFloat() to stringResource(id = category.first))

            }
        }
        if (listOfEntries.isNotEmpty()) {
            if(incomeList.size>0) {
                HeadSetting(title = stringResource(id = R.string.incomechart), MaterialTheme.typography.headlineMedium)
                ChartPie(incomeList)
            }
            if(expenseList.size>0) {
                HeadSetting(title = stringResource(id = R.string.expensechart), MaterialTheme.typography.headlineMedium)
                ChartPie(expenseList)
            }

        } else {
            Text(
                modifier = Modifier.padding(50.dp),
                text = stringResource(id = R.string.noentries),
                color = LocalCustomColorsPalette.current.textColor,
                style=MaterialTheme.typography.bodyLarge

            )
        }
    }

}

@Composable
fun ChartPie(listOfEntries: MutableList<Pair<Float, String>>) {
    val initAngle = -90f
    var currentAngle: Float
    var endAngle: Float
    //val total = listOfEntries.sum()
    // Sumar todos los valores Float en la lista
    // Extraer los valores Float y sumar
    val total: Float = listOfEntries.map { it.first }.sum()
    val textColorPieChart = Color.Black
    val isDarkTheme = isSystemInDarkTheme()
    val legends = mutableListOf<Legend>()
    // Lista para almacenar los colores generados
    val colors = mutableListOf<Color>()

    // Generar colores aleatorios para cada segmento
    listOfEntries.forEach { entry ->
        val color = colorGenerator(isDarkTheme) // Cambia esto si deseas un método diferente
        colors.add(color)
        legends.add(Legend(entry.second, color)) // Cambia esto si deseas un método diferente
    }

    Row(modifier = Modifier.padding(10.dp)) {
        // Gráfico circular
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
                val centerX = size.width / 2
                val centerY = size.height / 2

                // Dibuja cada segmento
                currentAngle = initAngle // Reinicia el ángulo actual para el nuevo dibujo
                listOfEntries.forEachIndexed { index, element ->
                    endAngle = (element.first / total) * 360
                    val midAngle = currentAngle + endAngle / 2

                    // Dibuja el arco
                    drawArc(
                        color = colors[index],
                        startAngle = currentAngle,
                        sweepAngle = endAngle,
                        useCenter = true,
                        style = Fill
                    )

                    // Actualiza el ángulo actual
                    currentAngle += endAngle

                    // Calcula las coordenadas para el texto
                    val textX = centerX + (size.width / 3) * cos(
                        Math.toRadians(midAngle.toDouble()).toFloat()
                    )
                    val textY = centerY + (size.height / 3) * sin(
                        Math.toRadians(midAngle.toDouble()).toFloat()
                    )

                    // Dibuja el texto con el porcentaje
                    val percent = (element.first / total * 100).toInt()
                    val text = "$percent%"
                    val textPaint = Paint().asFrameworkPaint()
                    textPaint.color = textColorPieChart.toArgb()
                    textPaint.textSize = 35f
                    textPaint.isFakeBoldText = true
                    drawContext.canvas.nativeCanvas.drawText(text, textX, textY, textPaint)
                }
            }
        }
        Column(modifier = Modifier.weight(0.35f)) {
            legends.forEach { element ->
                LegendItem(element.color, element.legend)
            }
        }

    }
}


fun colorGenerator(isDarkTheme: Boolean): Color {
    val random = Random.Default
    return if (isDarkTheme) {
        // Genera colores vivos que destacan sobre un fondo oscuro
        val red = random.nextInt(128, 256) // Colores brillantes
        val green = random.nextInt(128, 256)
        val blue = random.nextInt(128, 256)
        Color(red, green, blue)
    } else {
        // Genera colores pastel que destacan sobre un fondo claro
        val red = random.nextInt(200, 256) // Colores pastel más claros
        val green = random.nextInt(200, 256)
        val blue = random.nextInt(200, 256)
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



