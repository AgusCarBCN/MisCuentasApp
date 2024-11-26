package carnerero.agustin.cuentaappandroid.entries

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.SnackBarController
import carnerero.agustin.cuentaappandroid.SnackBarEvent
import carnerero.agustin.cuentaappandroid.components.AccountSelector
import carnerero.agustin.cuentaappandroid.components.BoardType
import carnerero.agustin.cuentaappandroid.components.DatePickerSearch
import carnerero.agustin.cuentaappandroid.components.HeadSetting
import carnerero.agustin.cuentaappandroid.components.IconAnimated
import carnerero.agustin.cuentaappandroid.components.ModelButton
import carnerero.agustin.cuentaappandroid.components.TextFieldComponent
import carnerero.agustin.cuentaappandroid.createaccounts.view.AccountsViewModel
import carnerero.agustin.cuentaappandroid.main.data.database.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.main.data.database.dto.MutableEntryDTO
import carnerero.agustin.cuentaappandroid.main.data.database.entities.CategoryType
import carnerero.agustin.cuentaappandroid.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.search.SearchViewModel
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Math.abs

@Composable
fun ModifyEntry(entryDTO: EntryDTO,
                entriesViewModel: EntriesViewModel,
                searchViewModel: SearchViewModel,
                accountsViewModel: AccountsViewModel,
                mainViewModel: MainViewModel
                )
{
    val descriptionEntry by entriesViewModel.entryName.observeAsState(entryDTO.description)
    val amountEntry by entriesViewModel.entryAmount.observeAsState(kotlin.math.abs(entryDTO.amount).toString())
    val dateSelected by searchViewModel.selectedFromDate.observeAsState(entryDTO.date)

    val messageModify= stringResource(id = R.string.modifyentrymsg)
    val scope = rememberCoroutineScope()
    //val categorySelected by categoriesViewModel.categorySelected.observeAsState(null)
    val initColor =
        if (entryDTO.categoryType== CategoryType.INCOME) LocalCustomColorsPalette.current.iconIncomeInit
        else LocalCustomColorsPalette.current.iconExpenseInit
    val targetColor = if (entryDTO.categoryType== CategoryType.INCOME) LocalCustomColorsPalette.current.iconIncomeTarget
    else LocalCustomColorsPalette.current.iconExpenseTarget
    val amountBefore=entryDTO.amount

    val updateBalanceIncome=amountEntry.toDouble()-amountBefore
    val updateBalanceExpense=-(amountEntry.toDouble())+amountBefore
    val mutableEntryDTO = remember {
        MutableEntryDTO(
            id = entryDTO.id,
            description = mutableStateOf(entryDTO.description),
            amount = mutableDoubleStateOf(kotlin.math.abs(entryDTO.amount)),
            date = mutableStateOf(entryDTO.date),
            iconResource = entryDTO.iconResource,
            nameResource = entryDTO.nameResource,
            accountId = entryDTO.accountId,
            name = entryDTO.name,
            categoryId = entryDTO.categoryId,
            categoryType = entryDTO.categoryType
        )
    }



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
        IconAnimated(entryDTO.iconResource, 120, initColor, targetColor)
        HeadSetting(
            title = stringResource(R.string.modifydes), MaterialTheme.typography.headlineMedium
        )
        TextFieldComponent(
            modifier = Modifier.width(320.dp),
            stringResource(id = R.string.desamount),
            descriptionEntry,
            onTextChange = { entriesViewModel.onTextFieldsChanged(it, amountEntry)},

            BoardType.TEXT,
            false
        )
        HeadSetting(
            title = stringResource(R.string.modifydate), MaterialTheme.typography.headlineMedium
        )
        DatePickerSearch(
            modifier = Modifier.width(300.dp),
            R.string.modifydate,
            searchViewModel,
            true
        )
        HeadSetting(
            title = stringResource(R.string.modifyamount), MaterialTheme.typography.headlineMedium
        )
        TextFieldComponent(
            modifier = Modifier.width(320.dp),
            stringResource(id = R.string.modifyamount),
            amountEntry,
            onTextChange = { entriesViewModel.onTextFieldsChanged(descriptionEntry, it)},

            BoardType.DECIMAL,
            false
        )
        ModelButton(text = stringResource(id = R.string.modifyButton),
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(320.dp),
            true,
            onClickButton = {
                val entryDTOUpdated=EntryDTO(
                    entryDTO.id,
                    descriptionEntry,
                    if(entryDTO.categoryType==CategoryType.INCOME) amountEntry.toDouble()
                    else (-1)*(amountEntry.toDouble()),
                    dateSelected,
                    entryDTO.iconResource,
                    entryDTO.nameResource,
                    entryDTO.accountId,
                    entryDTO.name,
                    entryDTO.categoryId,
                    entryDTO.categoryType
                )
                    entriesViewModel.updateEntry(entryDTO.id,
                        descriptionEntry,
                        if(entryDTO.categoryType==CategoryType.INCOME) amountEntry.toDouble()
                        else (-1)*(amountEntry.toDouble()),
                        dateSelected)
                entriesViewModel.updateEntries(entryDTO.id, entryDTOUpdated)
                mainViewModel.selectScreen(IconOptions.ENTRIES_TO_UPDATE)
                //Actualiza balance de cuenta
                /*accountsViewModel.updateAccountBalance(
                    entryDTO.accountId,
                    if(entryDTO.categoryType==CategoryType.INCOME) updateBalanceIncome
                    else updateBalanceExpense,
                    false
                )*/


                entriesViewModel.getTotal()
                scope.launch(Dispatchers.Main) {
                    SnackBarController.sendEvent(event = SnackBarEvent(messageModify))
                }
            }
        )
        ModelButton(text = stringResource(id = R.string.backButton),
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(320.dp),
            true,
            onClickButton = {
                mainViewModel.selectScreen(IconOptions.HOME)
            }
        )

    }

}