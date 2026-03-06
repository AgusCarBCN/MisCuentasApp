package carnerero.agustin.cuentaappandroid.presentation.ui.statistics.barchart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearSelector(
    onSelectedYear:(String)->Unit,
    modifier: Modifier = Modifier
) {
    // Obtén el año actual
    val currentYear = LocalDate.now().year
    val years = (2000..2100).map { it.toString() }

    // Índice inicial basado en el año actual
    val initialPage = years.indexOf(currentYear.toString()).coerceAtLeast(0)

    // Estado del VerticalPager
    val pagerState = rememberPagerState(
        pageCount = { years.size },
        initialPage = initialPage
    )

    // Determina dirección de scroll

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        // Card del VerticalPager
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(colors.backgroundSecondary)
            ) { page ->
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = years[page],
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

                // Actualiza el ViewModel
                LaunchedEffect(pagerState.currentPage) {
                    onSelectedYear(years[pagerState.currentPage])
                }
            }
        }

