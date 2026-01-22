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
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.AccountSelector
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.IconAnimated
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.data.db.entities.Entry
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.Date

@Composable

fun NewEntry(
    mainViewModel: MainViewModel,
    entryViewModel: EntriesViewModel,
    categoriesViewModel: CategoriesViewModel,
    accountViewModel: AccountsViewModel
) {
    val scope = rememberCoroutineScope()
    val descriptionEntry by entryViewModel.entryName.observeAsState("")
    val amountEntry by entryViewModel.entryAmount.observeAsState("")
    val categorySelected by categoriesViewModel.categorySelected.observeAsState(null)
    val enableConfirmButton by entryViewModel.enableConfirmButton.observeAsState(false)
    val accountSelected by accountViewModel.accountSelected.observeAsState()

    val idAccount = accountSelected?.id ?: 1
    val categoryId=categorySelected?.id?:1
    val type = categorySelected?.type?: CategoryType.INCOME
    val iconResource = categorySelected?.iconResource ?: 0
    val titleResource = categorySelected?.nameResource ?: 0
    val amount = amountEntry.toBigDecimalOrNull() ?: BigDecimal.ZERO
    val negativeAmount = amountEntry.toBigDecimalOrNull()?.negate() ?: BigDecimal.ZERO
    //Snackbar messages
    val newIncomeMessage = message(resource = R.string.newincomecreated)
    val newExpenseMessage = message(resource = R.string.newexpensecreated)
    val amountOverBalanceMessage = message(resource = R.string.overbalance)
    val noAccounts = message(resource = R.string.noaccounts)
    var operationStatus: Int

    val initColor =
        if (type== CategoryType.INCOME) LocalCustomColorsPalette.current.iconIncomeInit
        else LocalCustomColorsPalette.current.iconExpenseInit
    val targetColor = if (type== CategoryType.INCOME) LocalCustomColorsPalette.current.iconIncomeTarget
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
        IconAnimated(iconResource = iconResource, sizeIcon = 120, initColor, targetColor)
        HeadSetting(title = stringResource(id = titleResource), MaterialTheme.typography.headlineMedium
        )
        TextFieldComponent(
            modifier = Modifier.width(320.dp),
            stringResource(id = R.string.desamount),
            descriptionEntry,
            onTextChange = { entryViewModel.onTextFieldsChanged(it, amountEntry) },
            BoardType.TEXT,
            false
        )
        TextFieldComponent(
            modifier = Modifier.width(320.dp),
            stringResource(id = R.string.enternote),
            amountEntry,
            onTextChange = { entryViewModel.onTextFieldsChanged(descriptionEntry, it) },
            BoardType.DECIMAL,
            false
        )
        AccountSelector(300,20,stringResource(id = R.string.selectanaccount), accountViewModel)
        ModelButton(text = stringResource(id = if (type== CategoryType.INCOME) R.string.newincome else R.string.newexpense),
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(320.dp),
            enableConfirmButton,
            onClickButton = {
                operationStatus = if (type== CategoryType.EXPENSE){
                    if (accountViewModel.isValidExpense(amountEntry.toBigDecimalOrNull() ?: BigDecimal.ZERO)) {
                        1
                    } else {
                        0
                    }
                } else {
                    1
                }
                scope.launch(Dispatchers.IO) {
                    if (operationStatus == 1) {
                        entryViewModel.addEntry(
                            Entry(
                                description = descriptionEntry,
                                amount = if (type== CategoryType.INCOME) amount
                                else negativeAmount,
                                date = Date().dateFormat(),
                                categoryId = categoryId,
                                accountId = idAccount
                            )
                        )
                        accountViewModel.updateAccountBalance(
                            idAccount,
                            if (type== CategoryType.INCOME) amount else negativeAmount,
                            false
                        )

                        if (type== CategoryType.INCOME) {
                            withContext(Dispatchers.Main) {
                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        newIncomeMessage
                                    )
                                )
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        newExpenseMessage
                                    )
                                )
                            }
                        }
                    } else if (accountSelected == null) {
                        withContext(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    noAccounts
                                )
                            )
                        }

                    } else {
                        withContext(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    amountOverBalanceMessage
                                )
                            )
                        }

                    }
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