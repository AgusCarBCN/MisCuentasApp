package carnerero.agustin.cuentaappandroid.presentation.ui.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.DatePickerSearch
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.IconAnimated
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun ModifyEntry(entryDTO: EntryDTO,
                entriesViewModel: EntriesViewModel,
                searchViewModel: SearchViewModel,
                accountsViewModel: AccountsViewModel,
                mainViewModel: MainViewModel
                )
{
    val selectedEntryDTO by entriesViewModel.entryDTOSelected.observeAsState()
    // Sincroniza los datos iniciales del ViewModel
    LaunchedEffect(entryDTO) {
        entriesViewModel.setInitialData(entryDTO)
    }
    val description=entryDTO.description
    val amount= kotlin.math.abs(entryDTO.amount).toString()
    val descriptionEntry by entriesViewModel.entryDescriptionModify.observeAsState(description)
    val amountEntry by entriesViewModel.entryAmountModify.observeAsState(amount)
    val dateSelected by searchViewModel.selectedFromDate.observeAsState(entryDTO.date)

    val messageModify= stringResource(id = R.string.modifyentrymsg)
    val scope = rememberCoroutineScope()

    val initColor =
        if (entryDTO.categoryType== CategoryType.INCOME) LocalCustomColorsPalette.current.iconIncomeInit
        else LocalCustomColorsPalette.current.iconExpenseInit
    val targetColor = if (entryDTO.categoryType== CategoryType.INCOME) LocalCustomColorsPalette.current.iconIncomeTarget
    else LocalCustomColorsPalette.current.iconExpenseTarget


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
            onTextChange = { entriesViewModel.onTextFieldsChangedModify(it, amountEntry)},

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
            onTextChange = { entriesViewModel.onTextFieldsChangedModify(descriptionEntry, it)},

            BoardType.DECIMAL,
            false
        )
        ModelButton(text = stringResource(id = R.string.modifyButton),
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(320.dp),
            true,
            onClickButton = {
                val amountBefore=entryDTO.amount
                val updateBalanceIncome=amountEntry.toDouble()-amountBefore
                val updateBalanceExpense = (-1)*amountEntry.toDouble() + abs(amountBefore)

                val entryDTOUpdated= EntryDTO(
                    entryDTO.id,
                    descriptionEntry,
                    if(entryDTO.categoryType== CategoryType.INCOME) amountEntry.toDouble()
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
                        if(entryDTO.categoryType== CategoryType.INCOME) amountEntry.toDouble()
                        else (-1)*(amountEntry.toDouble()),
                        dateSelected)
                entriesViewModel.updateEntries(entryDTO.id, entryDTOUpdated)
                mainViewModel.selectScreen(IconOptions.ENTRIES_TO_UPDATE)
                //Actualiza balance de cuenta
                accountsViewModel.updateAccountBalance(
                    entryDTO.accountId,
                    if(entryDTO.categoryType== CategoryType.INCOME) updateBalanceIncome
                    else updateBalanceExpense,
                    false
                )


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