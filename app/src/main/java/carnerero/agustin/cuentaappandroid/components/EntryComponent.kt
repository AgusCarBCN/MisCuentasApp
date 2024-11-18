package carnerero.agustin.cuentaappandroid.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import carnerero.agustin.cuentaappandroid.main.data.database.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.newamount.view.EntriesViewModel
import carnerero.agustin.cuentaappandroid.setting.SpacerApp
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.utils.Utils
import kotlin.math.abs


@Composable
fun EntryList(
    entriesViewModel: EntriesViewModel,
    listOfEntries: List<EntryDTO>,
    currencyCode: String
) {

    val enableByDate by entriesViewModel.enableOptionList.observeAsState(true)

    // Agrupar las entradas por fecha
    val groupedEntriesByDate =
        listOfEntries.groupBy { it.date }  // Asumiendo que it.date es un String o LocalDate

    val entriesByCategory = Utils.getMapOfEntriesByCategory(listOfEntries)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (listOfEntries.isNotEmpty()) {
            TextButton(onClick = { entriesViewModel.onEnableByDate(true) }) {
                Text(
                    text = stringResource(id = R.string.bydate),
                    color = if (enableByDate) LocalCustomColorsPalette.current.textHeadColor
                    else LocalCustomColorsPalette.current.textColor,
                    fontSize = 18.sp
                )
            }
            TextButton(onClick = { entriesViewModel.onEnableByDate(false) }) {
                Text(
                    text = stringResource(id = R.string.bycategory),
                    color = if (enableByDate) LocalCustomColorsPalette.current.textColor
                    else LocalCustomColorsPalette.current.textHeadColor,
                    fontSize = 18.sp
                )
            }
        } else {
            Text(
                text = stringResource(id = R.string.noentries),
                color = LocalCustomColorsPalette.current.textColor,
                fontSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_body_extra_large).toSp() }
            )
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalCustomColorsPalette.current.backgroundPrimary)

    ) {
        if (enableByDate) {
            groupedEntriesByDate.forEach { (date, entries) ->
                // Mostrar una cabecera sticky para la fecha
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .background(LocalCustomColorsPalette.current.backgroundPrimary),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            modifier = Modifier
                                .background(LocalCustomColorsPalette.current.backgroundPrimary)
                                .fillParentMaxWidth()
                                .padding(start = 15.dp),
                            text = Utils.toDateEntry(date),
                            textAlign = TextAlign.Start,
                            color = LocalCustomColorsPalette.current.textColor,
                            style=MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Mostrar los elementos de ese grupo
                items(entries) { entry ->
                    ItemEntry(
                        entry,
                        currencyCode = currencyCode
                    )
                }
            }
        } else {
            items(entriesByCategory.toList()
                .sortedByDescending {(_, info) -> abs(info.second ?: 0.0) }) { (categoryName, info) ->
                val (icon, total) = info // Desestructurar el ícono y el total
                ItemCategory(
                    categoryName = categoryName,
                    categoryIcon = icon,
                    amount = total,
                    currencyCode
                )
            }

        }
    }
}

@Composable

fun ItemEntry(
    entry: EntryDTO,
    currencyCode: String
) {

    Column {

        Row(
            modifier = Modifier.padding(start = 15.dp, end = 20.dp, top = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.description,
                modifier = Modifier
                    .weight(0.6f),
                textAlign = TextAlign.Start,
                style=MaterialTheme.typography.bodyLarge,
                color = LocalCustomColorsPalette.current.textHeadColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = Utils.numberFormat(entry.amount, currencyCode),
                modifier = Modifier
                    .weight(0.4f),
                color = if (entry.amount >= 0) LocalCustomColorsPalette.current.incomeColor
                else LocalCustomColorsPalette.current.expenseColor,
                textAlign = TextAlign.End,
                style=MaterialTheme.typography.bodyLarge
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
                contentDescription = "icon",
                tint = LocalCustomColorsPalette.current.textColor
            )
            Spacer(modifier = Modifier.height(20.dp)) // Espacio entre el texto y el botón
            Text(
                text = stringResource(id = entry.nameResource),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f),
                color = LocalCustomColorsPalette.current.textColor,
                textAlign = TextAlign.Start,
                style=MaterialTheme.typography.bodyLarge

            )
            Text(
                text = entry.name,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f),
                color = LocalCustomColorsPalette.current.textColor,
                textAlign = TextAlign.End,
                style=MaterialTheme.typography.bodyMedium
            )

        }
        SpacerApp()
    }

}

@Composable
fun ItemCategory(
    categoryName: Int?,
    categoryIcon: Int?,
    amount: Double?,
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
                tint = LocalCustomColorsPalette.current.textColor
            )
            Spacer(modifier = Modifier.height(20.dp)) // Espacio entre el texto y el botón
            Text(
                text = stringResource(id = categoryName ?: 0),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f),
                color = LocalCustomColorsPalette.current.textColor,
                textAlign = TextAlign.Start,
                style=MaterialTheme.typography.bodyLarge

            )
            Text(
                text = Utils.numberFormat(amount ?: 0.0, currencyCode),
                modifier = Modifier
                    .weight(0.4f),
                color = if ((amount ?: 0.0) >= 0) LocalCustomColorsPalette.current.incomeColor
                else LocalCustomColorsPalette.current.expenseColor,
                textAlign = TextAlign.End,
                style=MaterialTheme.typography.bodyLarge

            )

        }

    }

}

