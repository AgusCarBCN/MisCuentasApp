package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.components.CreateAccountForm
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.components.CurrencyAndActions

@Composable
fun PortraitLayout(
    viewModel: AccountsViewModel,
    enableCurrencySelector: Boolean,
    isCurrencyExpanded: Boolean,
    isEnableButton: Boolean,
    accountName: String,
    accountBalance: String,
    currencyShowedCode: String,
    navToLogin: () -> Unit,
    navToBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dimens.extraLarge)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CreateAccountForm(
            viewModel,
            enableCurrencySelector,
            isCurrencyExpanded,
            isEnableButton,
            accountName,
            accountBalance
        )

        CurrencyAndActions(
            viewModel,
            enableCurrencySelector,
            isCurrencyExpanded,
            currencyShowedCode,
            navToLogin,
            navToBack
        )
    }
}
