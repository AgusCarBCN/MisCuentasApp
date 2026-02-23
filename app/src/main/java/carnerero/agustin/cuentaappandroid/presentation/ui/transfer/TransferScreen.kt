package carnerero.agustin.cuentaappandroid.presentation.ui.transfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.AccountSelector
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.IconAnimated
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.data.db.entities.Records
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.components.AccountSearchRecordsSelector
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp


@Composable
fun TransferScreen(
    transferViewModel: TransferViewModel,
    //accountViewModel: AccountsViewModel,
    //entryViewModel: EntriesViewModel,
    navToBack:()->Unit,
    navToHome: () -> Unit
) {
    val state by transferViewModel.uiState.collectAsStateWithLifecycle()
    //val accountFrom by accountViewModel.accountSelected.observeAsState()
    // val accountTo by accountViewModel.destinationAccount.observeAsState()
    // val amountTransfer by entryViewModel.entryAmount.observeAsState("0.0")
    // val confirmButton by entryViewModel.enableConfirmTransferButton.observeAsState(false)
    // val scope = rememberCoroutineScope()
    // val idAccountFrom = accountFrom?.id ?: 1
    // val idAccountTo = accountTo?.id ?: 1
    // val amount = amountTransfer.toBigDecimalOrNull() ?: BigDecimal.ZERO
    // val negativeAmount = amountTransfer.toBigDecimalOrNull()?.negate() ?: BigDecimal("1E10")
    //accountViewModel.isValidTransfer()
    val labelTransferFrom = stringResource(id = R.string.transferfrom)
    val labelTransferTo = stringResource(id = R.string.transferTo)
    //SnackBarMessage
    val amountOverBalanceMessage = message(resource = R.string.overbalance)
    val transferSuccessMessage = message(resource = R.string.transferdone)
    val confirmButton =state.enableConfirm
    //val isValidExpense=accountViewModel.isValidExpense(amount)
    val isLandscape =
        orientation == OrientationApp.Landscape
    LaunchedEffect(Unit) {
        transferViewModel.effect.collect { effect ->
            when (effect) {
                TransferEffects.MessageSuccess -> SnackBarController.sendEvent(SnackBarEvent(transferSuccessMessage))
                TransferEffects.NavBack -> navToBack()
                TransferEffects.NavToHome ->navToHome()
                TransferEffects.OverBalanceError ->  SnackBarController.sendEvent(SnackBarEvent(amountOverBalanceMessage))
            }
        }
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxWidthDp = maxWidth

        val fieldModifier = Modifier
            .fillMaxWidth(0.85f)
            .heightIn(min = 48.dp)
            .padding(dimens.small)

        if (isLandscape) {
            /** ---------------- LANDSCAPE ---------------- */
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = dimens.smallMedium),
                horizontalArrangement = Arrangement.Center
            ) {

                /** -------- LEFT (ICON + INPUT AMOUNT) -------- */
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(top=dimens.extraLarge),

                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    IconAnimated(
                        R.drawable.transferoption,
                        200,
                        initColor = colors.buttonColorDefault,
                        targetColor = colors.buttonColorPressed
                    )

                    TextFieldComponent(
                        modifier = fieldModifier,
                        stringResource(id = R.string.amountentrie),
                        state.amount.toString(),
                        onTextChange = {
                           transferViewModel.onUserEvent(TransferUiEvent.OnAmountChange(it))
                        },
                        BoardType.DECIMAL,
                        false
                    )
                }

                /** -------- RIGHT (ACTIONS) -------- */
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(0.5f)
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AccountSearchRecordsSelector(
                        R.string.originaccount,
                        state.accounts,
                        state.currencyCode,
                        {
                            transferViewModel.onUserEvent(TransferUiEvent.OnAccountOriginChange(it)) },
                        false,
                        fieldModifier
                    )
                    AccountSearchRecordsSelector(
                        R.string.destinationaccount,
                        state.accounts,
                        state.currencyCode,
                        {
                            transferViewModel.onUserEvent(TransferUiEvent.OnAccountDestinationChange(it))   },
                        true,
                        fieldModifier
                    )
                   /* AccountSelector(
                        stringResource(id = R.string.originaccount),
                        accountViewModel,
                        modifier = fieldModifier
                    )

                    AccountSelector(
                        stringResource(id = R.string.destinationaccount),
                        accountViewModel,
                        true,
                        modifier = fieldModifier
                    )*/

                    /** --- lógica intacta --- */
                   /* if ((accountTo == accountFrom) ||
                        (amountTransfer.isEmpty()
                                || amountTransfer.toDoubleOrNull() == 0.0) ||
                        amountTransfer == "."
                    ) {
                        entryViewModel.onChangeTransferButton(false)
                    } else {
                        entryViewModel.onChangeTransferButton(true)
                    }*/
                    Row(modifier = fieldModifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.medium)) {
                        ModelButton(
                            text = stringResource(id = R.string.confirmButton),
                            MaterialTheme.typography.labelLarge,
                            modifier = fieldModifier.weight(1f),
                            confirmButton,
                            onClickButton = {
                                transferViewModel.onUserEvent(TransferUiEvent.OnConfirm(labelTransferFrom,labelTransferTo ))
                              /*  scope.launch(Dispatchers.IO) {
                                    if (isValidExpense) {
                                        entryViewModel.addEntry(
                                            Records(
                                                description = transferFrom,
                                                amount = negativeAmount,
                                                date = Date().dateFormat(),
                                                categoryId = 53,
                                                accountId = idAccountFrom
                                            )
                                        )
                                        entryViewModel.addEntry(
                                            Records(
                                                description = transferTo,
                                                amount = amount,
                                                date = Date().dateFormat(),
                                                categoryId = 53,
                                                accountId = idAccountTo
                                            )
                                        )
                                        accountViewModel.transferAmount(
                                            idAccountFrom,
                                            idAccountTo,
                                            amount
                                        )
                                        entryViewModel.onChangeTransferButton(false)
                                    }

                                    withContext(Dispatchers.Main) {
                                        SnackBarController.sendEvent(
                                            SnackBarEvent(
                                                if (isValidExpense)
                                                    transferSuccessMessage
                                                else
                                                    amountOverBalanceMessage
                                            )
                                        )
                                    }
                                }*/
                            }
                        )

                        ModelButton(
                            text = stringResource(id = R.string.backButton),
                            MaterialTheme.typography.labelLarge,
                            modifier = fieldModifier.weight(1f),
                            true
                        ) {
                            transferViewModel.onUserEvent(TransferUiEvent.NavToBack)
                        }
                    }
                }
            }

        } else {
            /** ---------------- PORTRAIT ---------------- */
            Column(
                modifier = Modifier
                    .width(maxWidthDp)
                    .padding(top = dimens.extraLarge)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                IconAnimated(
                    R.drawable.transferoption,
                    120,
                    initColor = colors.buttonColorDefault,
                    targetColor = colors.buttonColorPressed
                )

                TextFieldComponent(
                    modifier = fieldModifier,
                    stringResource(id = R.string.amountentrie),
                    state.amount.toString(),
                    onTextChange = {
                        transferViewModel.onUserEvent(TransferUiEvent.OnAmountChange(it))
                    },
                    BoardType.DECIMAL,
                    false
                )

                AccountSearchRecordsSelector(
                    R.string.originaccount,
                    state.accounts,
                    state.currencyCode,
                    {
                        transferViewModel.onUserEvent(TransferUiEvent.OnAccountOriginChange(it)) },
                    false,
                    fieldModifier
                )
                AccountSearchRecordsSelector(
                    R.string.destinationaccount,
                    state.accounts,
                    state.currencyCode,
                    {
                        transferViewModel.onUserEvent(TransferUiEvent.OnAccountDestinationChange(it))   },
                    true,
                    fieldModifier
                )

                /** --- lógica intacta --- */
                /*if ((accountTo == accountFrom) ||
                    (amountTransfer.isEmpty()
                            || amountTransfer.toDoubleOrNull() == 0.0) ||
                    amountTransfer == "."
                ) {
                    entryViewModel.onChangeTransferButton(false)
                } else {
                    entryViewModel.onChangeTransferButton(true)
                }*/

                ModelButton(
                    text = stringResource(id = R.string.confirmButton),
                    MaterialTheme.typography.labelLarge,
                    modifier = fieldModifier,
                    confirmButton,
                    onClickButton = {
                        transferViewModel.onUserEvent(TransferUiEvent.OnConfirm(labelTransferFrom,labelTransferTo ))
                        /*scope.launch(Dispatchers.IO) {
                            if (isValidExpense) {
                                entryViewModel.addEntry(
                                    Records(
                                        description = labelTransferFrom,
                                        amount = negativeAmount,
                                        date = Date().dateFormat(),
                                        categoryId = 53,
                                        accountId = idAccountFrom
                                    )
                                )
                                entryViewModel.addEntry(
                                    Records(
                                        description = labelTransferTo,
                                        amount = amount,
                                        date = Date().dateFormat(),
                                        categoryId = 53,
                                        accountId = idAccountTo
                                    )
                                )
                                accountViewModel.transferAmount(
                                    idAccountFrom,
                                    idAccountTo,
                                    amount
                                )
                                entryViewModel.onChangeTransferButton(false)
                            }

                            withContext(Dispatchers.Main) {
                                SnackBarController.sendEvent(
                                    SnackBarEvent(
                                        if (isValidExpense)
                                            transferSuccessMessage
                                        else
                                            amountOverBalanceMessage
                                    )
                                )
                            }
                        }*/
                    }
                )

                ModelButton(
                    text = stringResource(id = R.string.backButton),
                    MaterialTheme.typography.labelLarge,
                    modifier = fieldModifier,
                    true
                ) {
                    transferViewModel.onUserEvent(TransferUiEvent.NavToBack)
                }
            }
        }
    }
}


/*
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

    val isValidExpense=accountViewModel.isValidExpense(amount)
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxWidthDp = maxWidth
        val maxHeightDp = maxHeight
        val fieldModifier = Modifier
            .fillMaxWidth(0.85f) // mismo ancho para TODOS
            .heightIn(min = 48.dp)
            .padding(dimens.small)
    Column(
        modifier = Modifier
            .width(maxWidthDp)
            .padding(top = dimens.extraLarge)
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
            modifier = fieldModifier,
            stringResource(id = R.string.amountentrie),
            amountTransfer,
            onTextChange = { entryViewModel.onAmountChanged(idAccountTo, idAccountFrom, it) },
            BoardType.DECIMAL,
            false
        )
        AccountSelector(stringResource(id = R.string.originaccount),
            accountViewModel,
            modifier=fieldModifier)
        AccountSelector(
            stringResource(id = R.string.destinationaccount),
            accountViewModel,
            true,
            modifier = fieldModifier
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
            modifier = fieldModifier,
            confirmButton,
            onClickButton = {
                scope.launch(Dispatchers.IO) {
                    if (isValidExpense) {
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
                    if (isValidExpense) {
                        withContext(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    transferSuccessMessage
                                )
                            )
                        }
                    } else  {
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
            modifier = fieldModifier,
            true,
            onClickButton = {
                navToHome()
            }

        )
    }
}
}*/