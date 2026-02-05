package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.IconAnimated
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors

@Composable
public fun CreateAccountForm(
    viewModel: AccountsViewModel,
    enableCurrencySelector: Boolean,
    isCurrencyExpanded: Boolean,
    isEnableButton: Boolean,
    accountName: String,
    accountBalance: String
) {
    val messageSuccess = message(resource = R.string.newaccountcreated)
    val messageError = message(resource = R.string.erroraccountcreated)
    if (isCurrencyExpanded) return

    val fieldModifier = Modifier
        .fillMaxWidth(0.85f)
        .heightIn(min = 48.dp)

    Text(
        text = stringResource(R.string.createAccount),
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        color = colors.textColor
    )

    if (!enableCurrencySelector) {
        IconAnimated(
            iconResource = R.drawable.configaccountoption,
            sizeIcon = 120,
            colors.imageTutorialInit,
            colors.imageTutorialTarget
        )
    }

    TextFieldComponent(
        modifier = fieldModifier,
        stringResource(R.string.amountName),
        accountName,
        onTextChange = { viewModel.onTextFieldsChanged(it, accountBalance) },
        BoardType.TEXT,
        false
    )

    TextFieldComponent(
        modifier = fieldModifier,
        stringResource(R.string.enteramount),
        accountBalance,
        onTextChange = { viewModel.onTextFieldsChanged(accountName, it) },
        BoardType.DECIMAL,
        false
    )

    ModelButton(
        text = stringResource(R.string.addAccount),
        MaterialTheme.typography.labelLarge,
        modifier = fieldModifier,
        isEnableButton,
        onClickButton = {
            viewModel.createNewAccount(
                accountBalance,
                accountName,
                messageSuccess,
                messageError
            )
        }
    )
}
