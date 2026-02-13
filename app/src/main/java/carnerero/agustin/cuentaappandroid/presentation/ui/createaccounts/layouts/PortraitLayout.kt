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
import carnerero.agustin.cuentaappandroid.presentation.theme.smallCustomTextUnits
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.CreateAccountViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.components.CreateAccountForm
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.components.CurrencyAndActions
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountEffect

@Composable
fun PortraitLayout(
    createAccountViewModel: CreateAccountViewModel,
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
           createAccountViewModel
        )

        CurrencyAndActions(
           createAccountViewModel,
            navToLogin,
            navToBack
        )
    }
}
