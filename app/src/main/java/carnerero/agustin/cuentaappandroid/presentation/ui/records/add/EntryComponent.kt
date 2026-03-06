package carnerero.agustin.cuentaappandroid.presentation.ui.records.add


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SpacerApp
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.math.BigDecimal

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EntryListV2(
    modifier: Modifier,
    entries: List<RecordDTO>,
    currencyCode: String,
    enableByDate:Boolean,
    onEnableByDate:(Boolean)->Unit,
    onBackToHome:()->Unit,

    ) {

    // Agrupar las entradas por fecha
    val groupedEntriesByDate = entries.groupBy { it.date }
    // Agrupar las entradas por categoría
    val entriesByCategory = Utils.getMapOfEntriesByCategory(entries)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        // Icono de volver atrás alineado a la izquierda
        IconButton(
            onClick = { onBackToHome() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Volver",
                tint = colors.textColor
            )
        }

        // Contenedor de los botones centrado horizontalmente
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { onEnableByDate(true) }) {
                Text(
                    text = stringResource(id = R.string.bydate),
                    color = if (enableByDate) colors.textHeadColor else colors.textColor,
                    fontSize = 18.sp
                )
            }
            TextButton(onClick = { onEnableByDate(false) }) {
                Text(
                    text = stringResource(id = R.string.bycategory),
                    color = if (!enableByDate) colors.textHeadColor else colors.textColor,
                    fontSize = 18.sp
                )
            }
        }
    }

            // LazyColumn con los registros
            LazyColumn(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (enableByDate) {
                    groupedEntriesByDate.forEach { (date, entries) ->
                        // Sticky Header con la fecha
                        stickyHeader {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .background(colors.backgroundPrimary),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = Utils.toDateFormatDayMonth(date),
                                    color = colors.textColor,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                        items(entries) { entry ->
                            ItemEntry(
                                entry = entry,
                                currencyCode = currencyCode
                            )
                        }
                    }
                } else {
                    entriesByCategory.toList()
                        .sortedByDescending { (_, info) -> info.second?.abs() }
                        .forEach { (categoryName, info) ->
                            val (icon, total) = info
                            item {
                                ItemCategory(
                                    categoryName = categoryName,
                                    categoryIcon = icon,
                                    amount = total,
                                    currencyCode = currencyCode
                                )
                            }
                        }
                }
            }

        }




@Composable

fun ItemEntry(
    entry: RecordDTO,
    currencyCode: String
) {
    val date = stringResource(id = R.string.fromdate)
    val amount = stringResource(id = R.string.amountentrie)
    val entryAmount=entry.amount
    "${entry.description} ${stringResource(id = R.string.itemicon)}  ${stringResource(entry.nameResource)}"
    Column(modifier = Modifier.semantics {
        contentDescription =
            " ${entry.description}, $date: ${entry.date}, $amount:${
                Utils.numberFormat(
                    entry.amount,
                    currencyCode
                )
            }"
    }) {

        Row(
            modifier = Modifier
                .padding(start = 15.dp, end = 20.dp, top = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.description,
                modifier = Modifier
                    .weight(0.6f),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textHeadColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = Utils.numberFormat(entry.amount, currencyCode),
                modifier = Modifier
                    .weight(0.4f),

                    color = if (entryAmount >= BigDecimal.ZERO) colors.incomeColor
                    else colors.expenseColor
               ,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyLarge
            )

        }
        Row(
            modifier = Modifier.padding(start = 15.dp, end = 20.dp, top = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = entry.iconResource),
                contentDescription = null,
                tint = colors.textColor
            )
            Spacer(modifier = Modifier.height(20.dp)) // Espacio entre el texto y el botón
            Text(
                text = stringResource(id = entry.nameResource),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f),
                color = if (entryAmount.toDouble() > 0.0 )
                    colors.incomeColor
                else
                    colors.expenseColor
                ,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge

            )
            Text(
                text = entry.name,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f),
                color = colors.textColor,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium
            )

        }
        SpacerApp()
    }

}

@Composable
fun ItemCategory(
    categoryName: Int?,
    categoryIcon: Int?,
    amount: BigDecimal?,
    currencyCode: String
) {
    Column {

        Row(
            modifier = Modifier.padding(start = 15.dp, end = 20.dp, top = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = categoryIcon ?: 0),
                contentDescription = "icon",
                tint = colors.textColor
            )
            Spacer(modifier = Modifier.height(20.dp)) // Espacio entre el texto y el botón
            Text(
                text = stringResource(id = categoryName ?: 0),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f),
                color = colors.textColor,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge

            )
            Text(

                text = Utils.numberFormat((amount ?: BigDecimal.ZERO) , currencyCode),
                modifier = Modifier
                    .weight(0.4f),
                color = if ((amount ?: BigDecimal.ZERO) >= BigDecimal.ZERO) colors.incomeColor
                else colors.expenseColor,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyLarge

            )

        }

    }

}
