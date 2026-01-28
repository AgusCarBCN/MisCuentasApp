package carnerero.agustin.cuentaappandroid.presentation.ui.entries.components


import android.R.attr.entries
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
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
import androidx.hilt.navigation.compose.hiltViewModel
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.model.EntryWithCheckBox
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.EntryCardWithCheckBox
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.EntryCardWithIcon
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SpacerApp
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalFoundationApi::class) // Habilitar API experimental
@Composable
fun EntryList(


    entriesViewModel: EntriesViewModel = hiltViewModel(),
    accountViewModel: AccountsViewModel= hiltViewModel()
) {
    val enableByDate by entriesViewModel.enableOptionList.observeAsState(true)
    val currencyCode by accountViewModel.currencyCodeSelected.observeAsState("EUR")
    val listOfEntries by entriesViewModel.listOfEntriesDTO.collectAsState()

    val isLoading by entriesViewModel.loading.collectAsState()


        if(isLoading) {
            CircularProgressIndicator()
        }

        else  {

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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.recordsnotfound),
                            color = LocalCustomColorsPalette.current.textColor,
                            textAlign = TextAlign.Center,
                            fontSize = with(LocalDensity.current) {
                                dimensionResource(id = R.dimen.text_body_extra_large).toSp()
                            }
                        )
                    }

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
                                    text = Utils.toDateFormatDayMonth(date),
                                    textAlign = TextAlign.Start,
                                    color = LocalCustomColorsPalette.current.textColor,
                                    style = MaterialTheme.typography.bodyLarge
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
                    items(
                        entriesByCategory.toList()
                            .sortedByDescending { (_, info) ->

                                info.second?.abs()

                            }) { (categoryName, info) ->
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
    }
@Composable

fun ItemEntry(
    entry: EntryDTO,
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
                color = LocalCustomColorsPalette.current.textHeadColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = Utils.numberFormat(entry.amount, currencyCode),
                modifier = Modifier
                    .weight(0.4f),

                    color = if (entryAmount >= BigDecimal.ZERO) LocalCustomColorsPalette.current.incomeColor
                    else LocalCustomColorsPalette.current.expenseColor
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
                tint = LocalCustomColorsPalette.current.textColor
            )
            Spacer(modifier = Modifier.height(20.dp)) // Espacio entre el texto y el botón
            Text(
                text = stringResource(id = entry.nameResource),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f),
                color = if (entryAmount.toDouble() > 0.0 )
                    LocalCustomColorsPalette.current.incomeColor
                else
                    LocalCustomColorsPalette.current.expenseColor
                ,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge

            )
            Text(
                text = entry.name,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f),
                color = LocalCustomColorsPalette.current.textColor,
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
                style = MaterialTheme.typography.bodyLarge

            )
            Text(

                text = Utils.numberFormat((amount ?: BigDecimal.ZERO) , currencyCode),
                modifier = Modifier
                    .weight(0.4f),
                color = if ((amount ?: BigDecimal.ZERO) >= BigDecimal.ZERO) LocalCustomColorsPalette.current.incomeColor
                else LocalCustomColorsPalette.current.expenseColor,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyLarge

            )

        }

    }

}

@Composable
fun EntriesWithCheckBox(
    entriesViewModel: EntriesViewModel,
    accountViewModel: AccountsViewModel,
    listOfEntries: List<EntryDTO>,
    currencyCode: String
) {
    // Sincronizar listOfEntriesWithCheckBox con listOfEntries:
    // remember(listOfEntries):
    // - Al observar listOfEntries, remember reconstruye listOfEntriesWithCheckBox cada vez que listOfEntries cambia.
    // - Esto asegura que los datos estén siempre sincronizados con la fuente original (listOfEntries).
    // map y toMutableStateList:
    // - map crea una nueva lista de EntryWithCheckBox, donde cada elemento de listOfEntries se asocia a un checkbox inicializado en false.
    // - toMutableStateList convierte esa lista en un estado observable para que las actualizaciones dinámicas funcionen en la interfaz de manera reactiva.
    val listOfEntriesWithCheckBox = remember(listOfEntries) {
        listOfEntries.map { EntryWithCheckBox(it, false) }.toMutableStateList()
    }
    val scope = rememberCoroutineScope()
    val messageDeleteEntries = stringResource(id = R.string.deleteentries)
    val messageNotSelectedEntries = stringResource(id = R.string.nodeleteentries)
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (listOfEntries.isNotEmpty()) {
            HeadSetting(
                title = stringResource(id = R.string.selectentriesToDelete),
                MaterialTheme.typography.titleLarge
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.recordsnotfound),
                    color = LocalCustomColorsPalette.current.textColor,
                    textAlign = TextAlign.Center,
                    fontSize = with(LocalDensity.current) {
                        dimensionResource(id = R.dimen.text_body_extra_large).toSp()
                    }
                )
            }

        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(LocalCustomColorsPalette.current.backgroundPrimary)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), // Espacio entre elementos
            contentPadding = PaddingValues(16.dp) // Padding alrededor del contenido
        ) {
            items(listOfEntriesWithCheckBox) { entry ->
                EntryCardWithCheckBox(
                    entry.entry,
                    currencyCode,
                    entry.checkbox,
                    onSelectionChange = {
                        val index = listOfEntriesWithCheckBox.indexOf(entry)
                        if (index != -1) {
                            listOfEntriesWithCheckBox[index] =
                                entry.copy(checkbox = !entry.checkbox)
                        }

                    }
                )
            }
        }
        if (listOfEntries.isNotEmpty()) {
            ModelButton(text = stringResource(
                id = R.string.deleteButton
            ),
                MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(320.dp),
                true,
                onClickButton = {

                    val entriesToRemove =
                        listOfEntriesWithCheckBox.filter { it.checkbox } // Filtra los elementos a eliminar
                    if (entriesToRemove.isEmpty()) {
                        scope.launch(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    messageNotSelectedEntries
                                )
                            )
                        }
                    } else {
                        entriesToRemove.forEach { entryWithCheckBox ->
                            val idAccount = entryWithCheckBox.entry.accountId
                            val amount=entryWithCheckBox.entry.amount
                            listOfEntriesWithCheckBox.remove(entryWithCheckBox) // Modifica la lista original
                            entriesViewModel.deleteEntry(entryWithCheckBox.entry) // Borra de la base de datos
                            accountViewModel.updateAccountBalance(
                                                        idAccount,
                                                        amount.negate(),
                                                        false
                                                    )

                        }
                        entriesViewModel.getTotal()
                        scope.launch(Dispatchers.Main) {
                            SnackBarController.sendEvent(event = SnackBarEvent(messageDeleteEntries))
                        }

                    }
                }
            )
        }
    }
}

@Composable
fun EntriesWithEditIcon(
    entriesViewModel: EntriesViewModel,
    mainViewModel: MainViewModel,
    listOfEntries: List<EntryDTO>,
    currencyCode: String
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalCustomColorsPalette.current.backgroundPrimary)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp), // Espacio entre elementos
        contentPadding = PaddingValues(16.dp) // Padding alrededor del contenido
    ) {
        items(listOfEntries) { entry ->
            EntryCardWithIcon(
                entry,
                currencyCode,
                entriesViewModel,
                mainViewModel
            )
        }
    }


}


