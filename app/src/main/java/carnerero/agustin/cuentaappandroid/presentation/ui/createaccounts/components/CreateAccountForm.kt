package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.IconAnimated
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.CreateAccountViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountEvent
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountUiState

@Composable
public fun CreateAccountForm(
   createAccountViewModel: CreateAccountViewModel
) {
    val state by createAccountViewModel.uiState.collectAsStateWithLifecycle()
    val enabledButton=state.isFormValid

    val fieldModifier = Modifier
        .fillMaxWidth(0.85f)
        .heightIn(min = 48.dp)

    Text(
        text = stringResource(R.string.createAccount),
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        color = colors.textColor
    )

    IconAnimated(
            iconResource = R.drawable.configaccountoption,
            sizeIcon = 120,
            colors.imageTutorialInit,
            colors.imageTutorialTarget
     )

    TextFieldComponent(
        modifier = fieldModifier,
        stringResource(R.string.amountName),
        state.accountName,
        onTextChange = {createAccountViewModel.onEvent(CreateAccountEvent.AccountNameChanged(it))},
        BoardType.TEXT,
        false
    )

    TextFieldComponent(
        modifier = fieldModifier,
        stringResource(R.string.enteramount),
        state.balance,
        onTextChange = { createAccountViewModel.onEvent(CreateAccountEvent.BalanceChanged(it))},
        BoardType.DECIMAL,
        false
    )

    ModelButton(
        text = stringResource(R.string.addAccount),
        MaterialTheme.typography.labelLarge,
        modifier = fieldModifier,
        enabledButton,
        onClickButton = {
            createAccountViewModel.onEvent(CreateAccountEvent.Submit)
        }
    )
}
