package carnerero.agustin.cuentaappandroid.presentation.ui.entries

import android.content.res.Configuration
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.Date

@Composable

fun EntryFormScreen(
    entryViewModel: EntriesViewModel,
    categoriesViewModel: CategoriesViewModel,
    accountViewModel: AccountsViewModel,
    navToBack: () -> Unit,

) {
    val scope = rememberCoroutineScope()
    val descriptionEntry by entryViewModel.entryName.observeAsState("")
    val amountEntry by entryViewModel.entryAmount.observeAsState("")
    val categorySelected by categoriesViewModel.categorySelected.observeAsState(null)
    val enableConfirmButton by entryViewModel.enableConfirmButton.observeAsState(false)
    val accountSelected by accountViewModel.accountSelected.observeAsState()

    val idAccount = accountSelected?.id ?: 1
    val categoryId = categorySelected?.id ?: 1
    val type = categorySelected?.type ?: CategoryType.INCOME
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
    val isValidAmount=accountViewModel.isValidExpense(amountEntry.toBigDecimalOrNull() ?: BigDecimal.ZERO)
    val initColor =
        if (type == CategoryType.INCOME) colors.iconIncomeInit
        else colors.iconExpenseInit
    val targetColor = if (type == CategoryType.INCOME) colors.iconIncomeTarget
    else colors.iconExpenseTarget

    val isLandscape =
        orientation == OrientationApp.Landscape

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
                    .padding(top = 30.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                /** -------- LEFT (FORM) -------- */
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    IconAnimated(
                        iconResource = iconResource,
                        sizeIcon = 120,
                        initColor = initColor,
                        targetColor = targetColor
                    )

                    HeadSetting(
                        title = stringResource(id = titleResource),
                        MaterialTheme.typography.headlineMedium
                    )

                    TextFieldComponent(
                        modifier = fieldModifier,
                        stringResource(id = R.string.desamount),
                        descriptionEntry,
                        onTextChange = {
                            entryViewModel.onTextFieldsChanged(it, amountEntry)
                        },
                        BoardType.TEXT,
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
                    TextFieldComponent(
                        modifier = fieldModifier,
                        stringResource(id = R.string.enternote),
                        amountEntry,
                        onTextChange = {
                            entryViewModel.onTextFieldsChanged(descriptionEntry, it)
                        },
                        BoardType.DECIMAL,
                        false
                    )

                    AccountSelector(
                        stringResource(id = R.string.selectanaccount),
                        accountViewModel,
                        modifier = fieldModifier
                    )
                    Row(
                        modifier = fieldModifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.medium)
                    ) {
                        ModelButton(
                            text = stringResource(
                                id = if (type == CategoryType.INCOME)
                                    R.string.newincome
                                else
                                    R.string.newexpense
                            ),
                            MaterialTheme.typography.labelSmall,
                            modifier = fieldModifier.weight(1f),
                            enableConfirmButton,
                            onClickButton = {
                                /*operationStatus =
                                    if (type == CategoryType.EXPENSE) {
                                        if (accountViewModel.isValidExpense(
                                                amountEntry.toBigDecimalOrNull()
                                                    ?: BigDecimal.ZERO
                                            )
                                        ) 1 else 0
                                    } else 1*/

                                scope.launch(Dispatchers.IO) {
                                    if (isValidAmount) {
                                        entryViewModel.addEntry(
                                            Entry(
                                                description = descriptionEntry,
                                                amount = if (type == CategoryType.INCOME)
                                                    amount else negativeAmount,
                                                date = Date().dateFormat(),
                                                categoryId = categoryId,
                                                accountId = idAccount
                                            )
                                        )

                                        accountViewModel.updateAccountBalance(
                                            idAccount,
                                            if (type == CategoryType.INCOME)
                                                amount else negativeAmount,
                                            false
                                        )

                                        withContext(Dispatchers.Main) {
                                            SnackBarController.sendEvent(
                                                SnackBarEvent(
                                                    if (type == CategoryType.INCOME)
                                                        newIncomeMessage
                                                    else
                                                        newExpenseMessage
                                                )
                                            )
                                        }
                                    } else if (accountSelected == null) {
                                        withContext(Dispatchers.Main) {
                                            SnackBarController.sendEvent(
                                                SnackBarEvent(noAccounts)
                                            )
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            SnackBarController.sendEvent(
                                                SnackBarEvent(amountOverBalanceMessage)
                                            )
                                        }
                                    }
                                }
                            }
                        )
                        ModelButton(
                            text = stringResource(id = R.string.backButton),
                            MaterialTheme.typography.labelSmall,
                            modifier = fieldModifier.weight(1f),
                            true
                        ) {
                            navToBack()
                        }
                    }


                }

            }

        } else {
            /** ---------------- PORTRAIT ---------------- */
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                IconAnimated(
                    iconResource = iconResource,
                    sizeIcon = 120,
                    initColor = initColor,
                    targetColor = targetColor
                )

                HeadSetting(
                    title = stringResource(id = titleResource),
                    MaterialTheme.typography.headlineMedium
                )

                TextFieldComponent(
                    modifier = fieldModifier,
                    stringResource(id = R.string.desamount),
                    descriptionEntry,
                    onTextChange = {
                        entryViewModel.onTextFieldsChanged(it, amountEntry)
                    },
                    BoardType.TEXT,
                    false
                )

                TextFieldComponent(
                    modifier = fieldModifier,
                    stringResource(id = R.string.enternote),
                    amountEntry,
                    onTextChange = {
                        entryViewModel.onTextFieldsChanged(descriptionEntry, it)
                    },
                    BoardType.DECIMAL,
                    false
                )

                AccountSelector(
                    stringResource(id = R.string.selectanaccount),
                    accountViewModel,
                    modifier = fieldModifier
                )

                ModelButton(
                    text = stringResource(
                        id = if (type == CategoryType.INCOME)
                            R.string.newincome
                        else
                            R.string.newexpense
                    ),
                    MaterialTheme.typography.labelLarge,
                    modifier = fieldModifier,
                    enableConfirmButton,
                    onClickButton = {
                        scope.launch(Dispatchers.IO) {
                            if (isValidAmount) {
                                entryViewModel.addEntry(
                                    Entry(
                                        description = descriptionEntry,
                                        amount = if (type == CategoryType.INCOME)
                                            amount else negativeAmount,
                                        date = Date().dateFormat(),
                                        categoryId = categoryId,
                                        accountId = idAccount
                                    )
                                )

                                accountViewModel.updateAccountBalance(
                                    idAccount,
                                    if (type == CategoryType.INCOME)
                                        amount else negativeAmount,
                                    false
                                )

                                withContext(Dispatchers.Main) {
                                    SnackBarController.sendEvent(
                                        SnackBarEvent(
                                            if (type == CategoryType.INCOME)
                                                newIncomeMessage
                                            else
                                                newExpenseMessage
                                        )
                                    )
                                }
                            } else if (accountSelected == null) {
                                withContext(Dispatchers.Main) {
                                    SnackBarController.sendEvent(
                                        SnackBarEvent(noAccounts)
                                    )
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    SnackBarController.sendEvent(
                                        SnackBarEvent(amountOverBalanceMessage)
                                    )
                                }
                            }
                        }
                    }
                )

                ModelButton(
                    text = stringResource(id = R.string.backButton),
                    MaterialTheme.typography.labelLarge,
                    modifier = fieldModifier,
                    true
                ) {
                    navToBack()
                }
            }
        }
    }

}

/*fun NewEntry(

    entryViewModel: EntriesViewModel,
    categoriesViewModel: CategoriesViewModel,
    accountViewModel: AccountsViewModel,
    navToBack:()->Unit
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
        if (type== CategoryType.INCOME) colors.iconIncomeInit
        else colors.iconExpenseInit
    val targetColor = if (type== CategoryType.INCOME) colors.iconIncomeTarget
    else colors.iconExpenseTarget
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
        AccountSelector(stringResource(id = R.string.selectanaccount), accountViewModel, modifier = Modifier.width(300.dp))
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
              navToBack
            }
        )
    }
}*/