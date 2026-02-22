package carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
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
import kotlin.collections.component1
import kotlin.collections.component2


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordsList(
    listOfRecords:List<RecordDTO>,
    currencyCode:String,
    enableByDate: Boolean,
    onChangeEnableByDate:(Boolean)->Unit
) {


    // Agrupar las entradas por fecha
    val groupedEntriesByDate = listOfRecords.groupBy { it.date }
    // Agrupar las entradas por categoría
    val entriesByCategory = Utils.getMapOfEntriesByCategory(listOfRecords)
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxWidthDp = maxWidth*0.85f
        val maxHeightDp = maxHeight
        val fieldModifier = Modifier
            .fillMaxWidth(0.85f) // mismo ancho para TODOS
            .heightIn(min = 48.dp)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.backgroundPrimary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Row de botones By Date / By Category
            if (listOfRecords.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { onChangeEnableByDate(true) }) {
                        Text(
                            text = stringResource(id = R.string.bydate),
                            color = if (enableByDate) colors.textHeadColor
                            else colors.textColor,
                            fontSize = 18.sp
                        )
                    }
                    TextButton(onClick = { onChangeEnableByDate(false) }) {
                        Text(
                            text = stringResource(id = R.string.bycategory),
                            color = if (!enableByDate) colors.textHeadColor
                            else colors.textColor,
                            fontSize = 18.sp
                        )
                    }
                }
            } else {
                // Mensaje cuando no hay registros
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.recordsnotfound),
                        color = colors.textColor,
                        textAlign = TextAlign.Center,
                        fontSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_body_extra_large).toSp() }
                    )
                }
            }

            // LazyColumn con los registros
            LazyColumn(
                modifier = Modifier
                    .width(maxWidthDp)
                    .weight(1f),
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
                            ItemRecord(
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
                                ItemCategoryRecord(
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
    }
}
@Composable

private fun ItemRecord(
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
private fun ItemCategoryRecord (
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
