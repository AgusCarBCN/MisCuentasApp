package carnerero.agustin.cuentaappandroid.transfer

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
import carnerero.agustin.cuentaappandroid.components.IconAnimated
import carnerero.agustin.cuentaappandroid.components.ModelButton
import carnerero.agustin.cuentaappandroid.components.TextFieldComponent
import carnerero.agustin.cuentaappandroid.components.message
import carnerero.agustin.cuentaappandroid.createaccounts.view.AccountsViewModel
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Entry
import carnerero.agustin.cuentaappandroid.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.newamount.view.EntriesViewModel
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

@Composable

fun Transfer(
    mainViewModel: MainViewModel,
    accountViewModel: AccountsViewModel,
    entryViewModel: EntriesViewModel
) {

    val accountFrom by accountViewModel.accountSelected.observeAsState()
    val accountTo by accountViewModel.destinationAccount.observeAsState()
    val amountTransfer by entryViewModel.entryAmount.observeAsState("")
    val confirmButton by entryViewModel.enableConfirmTransferButton.observeAsState(false)
    val scope = rememberCoroutineScope()
    val idAccountFrom = accountFrom?.id ?: 1
    val idAccountTo = accountTo?.id ?: 1
    val amount = amountTransfer.toDoubleOrNull() ?: 0.0
    val negativeAmount = (-1) * (amountTransfer.toDoubleOrNull() ?: 0.0)
    //accountViewModel.isValidTransfer()
    val transferFrom = stringResource(id = R.string.transferfrom)
    val transferTo = stringResource(id = R.string.transferTo)
    //SnackBarMessage
    val amountOverBalanceMessage = message(resource = R.string.overbalance)
    val transferSuccessMessage = message(resource = R.string.transferdone)
    var operationStatus = 1
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
        IconAnimated(
            R.drawable.transferoption,
            120,
            initColor = LocalCustomColorsPalette.current.buttonColorDefault,
            targetColor = LocalCustomColorsPalette.current.buttonColorPressed
        )

        TextFieldComponent(
            modifier = Modifier.width(320.dp),
            stringResource(id = R.string.amountentrie),
            amountTransfer,
            onTextChange = { entryViewModel.onAmountChanged(idAccountTo, idAccountFrom, it) },
            BoardType.DECIMAL,
            false
        )
        AccountSelector(300,20,stringResource(id = R.string.originaccount), accountViewModel)
        AccountSelector(300,20,stringResource(id = R.string.destinationaccount), accountViewModel, true)

        ModelButton(text = stringResource(id = R.string.confirmButton),
            R.dimen.text_title_medium,
            modifier = Modifier.width(320.dp),
            confirmButton,
            onClickButton = {
                operationStatus = if (accountViewModel.isValidExpense(amount)) {
                    1
                } else {
                    -1
                }
                scope.launch(Dispatchers.IO) {
                    if (operationStatus == 1) {
                        entryViewModel.addEntry(
                            Entry(
                                description = transferFrom,
                                amount = negativeAmount,
                                date = Date().dateFormat(),
                                categoryId = R.drawable.transferoption,
                                accountId = idAccountFrom
                            )
                        )
                        entryViewModel.addEntry(
                            Entry(
                                description = transferTo,
                                amount = amount,
                                date = Date().dateFormat(),
                                categoryId = R.drawable.transferoption,
                                accountId = idAccountTo
                            )
                        )
                        accountViewModel.transferAmount(idAccountFrom, idAccountTo, amount)
                        entryViewModel.onChangeTransferButton(false)
                    }
                    if (operationStatus == 1) {
                        withContext(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    transferSuccessMessage
                                )
                            )
                        }
                    } else if (operationStatus == -1) {
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
            R.dimen.text_title_medium,
            modifier = Modifier.width(320.dp),
            true,
            onClickButton = {
                mainViewModel.selectScreen(IconOptions.HOME)
            }

        )
    }
}