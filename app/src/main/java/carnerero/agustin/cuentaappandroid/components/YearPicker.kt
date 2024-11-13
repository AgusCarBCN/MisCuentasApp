package carnerero.agustin.cuentaappandroid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carnerero.agustin.cuentaappandroid.R

import carnerero.agustin.cuentaappandroid.barchart.BarChartViewModel
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun YearSelector(
    barChartViewModel: BarChartViewModel
) {
    // Obtén el año actual
    val currentYear = LocalDate.now().year
    val years = mutableListOf<String>()
    for (i in 2000..2100) {
        years.add(i.toString())
    }
    // Encuentra el índice inicial basado en el año actual
    val initialPage = years.indexOf(currentYear.toString()).coerceAtLeast(0)
    // Inicializamos el estado del VerticalPager basado en la cantidad de cuentas
    val pagerState = rememberPagerState(pageCount = { years.size }, initialPage = initialPage)
    val isDraggingUp by remember { derivedStateOf { pagerState.currentPage == 0 || pagerState.targetPage > pagerState.currentPage } }

    Column(
        modifier = Modifier
            .width(180.dp)
            .background(LocalCustomColorsPalette.current.backgroundPrimary)
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .background(LocalCustomColorsPalette.current.backgroundPrimary),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = if (isDraggingUp) R.drawable.arrow_up else R.drawable.arrow_down),
                contentDescription = null,
                tint = LocalCustomColorsPalette.current.textColor,
                modifier = Modifier
                    .width(36.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = stringResource(id = R.string.year),
                style= MaterialTheme.typography.bodyLarge,
                color = LocalCustomColorsPalette.current.textColor,
                modifier = Modifier.padding(vertical = 10.dp),
                textAlign = TextAlign.Start
            )
        }

        Card(
            modifier = Modifier.width(180.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalCustomColorsPalette.current.backgroundSecondary)
                    .height(60.dp),
            ) { page ->
                // Actualiza la cuenta seleccionada en ViewModel
                barChartViewModel.onSelectedYear(years[pagerState.targetPage])
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = years[page],
                        style= MaterialTheme.typography.bodyLarge,
                        color = LocalCustomColorsPalette.current.textColor,
                        textAlign = TextAlign.Center
                    )

            }
        }
    }
}}
