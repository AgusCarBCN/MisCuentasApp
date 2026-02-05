package carnerero.agustin.cuentaappandroid.presentation.ui.transfer

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
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.IconAnimated
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.data.db.entities.Entry
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.Date

@Composable

fun TransferScreen(
    accountViewModel: AccountsViewModel,
    entryViewModel: EntriesViewModel,
    navToHome:()->Unit
) {

    val accountFrom by accountViewModel.accountSelected.observeAsState()
    val accountTo by accountViewModel.destinationAccount.observeAsState()
    val amountTransfer by entryViewModel.entryAmount.observeAsState("0.0")
    val confirmButton by entryViewModel.enableConfirmTransferButton.observeAsState(false)
    val scope = rememberCoroutineScope()
    val idAccountFrom = accountFrom?.id ?: 1
    val idAccountTo = accountTo?.id ?: 1
    val amount = amountTransfer.toBigDecimalOrNull() ?: BigDecimal.ZERO
    val negativeAmount = amountTransfer.toBigDecimalOrNull()?.negate() ?: BigDecimal("1E10")
    //accountViewModel.isValidTransfer()
    val transferFrom = stringResource(id = R.string.transferfrom)
    val transferTo = stringResource(id = R.string.transferTo)
    //SnackBarMessage
    val amountOverBalanceMessage = message(resource = R.string.overbalance)
    val transferSuccessMessage = message(resource = R.string.transferdone)
    var operationStatus: Int
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
            initColor = colors.buttonColorDefault,
            targetColor = colors.buttonColorPressed
        )

        TextFieldComponent(
            modifier = Modifier.width(320.dp),
            stringResource(id = R.string.amountentrie),
            amountTransfer,
            onTextChange = { entryViewModel.onAmountChanged(idAccountTo, idAccountFrom, it) },
            BoardType.DECIMAL,
            false
        )
        AccountSelector(stringResource(id = R.string.originaccount), accountViewModel,modifier=Modifier.width(300.dp))
        AccountSelector(


            stringResource(id = R.string.destinationaccount),
            accountViewModel,
            true,
            modifier = Modifier.width(300.dp)
        )
        Log.d("amount", amountTransfer)
        if ((accountTo == accountFrom) ||
            (amountTransfer.isEmpty() ||
                    amountTransfer.toDoubleOrNull() == 0.0) ||
            amountTransfer == "."
        ) {
            entryViewModel.onChangeTransferButton(false)
        } else {
            entryViewModel.onChangeTransferButton(true)
        }
        ModelButton(text = stringResource(id = R.string.confirmButton),
            MaterialTheme.typography.labelLarge,
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
                                categoryId = 53,
                                accountId = idAccountFrom
                            )
                        )
                        entryViewModel.addEntry(
                            Entry(
                                description = transferTo,
                                amount = amount,
                                date = Date().dateFormat(),
                                categoryId = 53,
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
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(320.dp),
            true,
            onClickButton = {
                navToHome()
                //mainViewModel.selectScreen(IconOptions.HOME)
            }

        )
    }
}