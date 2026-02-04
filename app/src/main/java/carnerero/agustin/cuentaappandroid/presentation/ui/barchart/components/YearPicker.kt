package carnerero.agustin.cuentaappandroid.presentation.ui.barchart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.ui.barchart.BarChartViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearSelector(
    barChartViewModel: BarChartViewModel,
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
    val isDraggingUp by remember {
        derivedStateOf { pagerState.currentPage == 0 || pagerState.targetPage > pagerState.currentPage }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Row del título con ícono
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(colors.backgroundPrimary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = if (isDraggingUp) R.drawable.arrow_up else R.drawable.arrow_down),
                contentDescription = null,
                tint = colors.textColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(dimens.small))
            Text(
                text = stringResource(id = R.string.year),
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textColor
            )
        }

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
                    barChartViewModel.onSelectedYear(years[pagerState.currentPage])
                }
            }
        }

