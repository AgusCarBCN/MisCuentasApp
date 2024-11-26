package carnerero.agustin.cuentaappandroid.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.components.AccountSelector
import carnerero.agustin.cuentaappandroid.components.BoardType
import carnerero.agustin.cuentaappandroid.components.IconAnimated
import carnerero.agustin.cuentaappandroid.components.TextFieldComponent
import carnerero.agustin.cuentaappandroid.createaccounts.view.AccountsViewModel
import carnerero.agustin.cuentaappandroid.main.data.database.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.main.data.database.entities.CategoryType
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette

@Composable
fun ModifyEntry(entryDTO: EntryDTO,
                entriesViewModel: EntriesViewModel,
                accountsViewModel: AccountsViewModel
                )
{
    val descriptionEntry by entriesViewModel.entryName.observeAsState(entryDTO.description)
    val amountEntry by entriesViewModel.entryAmount.observeAsState(entryDTO.amount.toString())
    val accountSelected by accountsViewModel.accountSelected.observeAsState()
    //val categorySelected by categoriesViewModel.categorySelected.observeAsState(null)
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
        TextFieldComponent(
            modifier = Modifier.width(320.dp),
            stringResource(id = R.string.desamount),
            descriptionEntry,
            onTextChange = { entriesViewModel.onTextFieldsChanged(it,amountEntry) },
            BoardType.TEXT,
            false
        )
        TextFieldComponent(
            modifier = Modifier.width(320.dp),
            stringResource(id = R.string.enternote),
            amountEntry,
            onTextChange = { entriesViewModel.onTextFieldsChanged(descriptionEntry, it) },
            BoardType.DECIMAL,
            false
        )
        AccountSelector(300,20, stringResource(id = R.string.selectanaccount), accountsViewModel)
    }

}