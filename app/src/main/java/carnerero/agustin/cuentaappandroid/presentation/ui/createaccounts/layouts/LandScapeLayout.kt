package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.CreateAccountViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.components.CreateAccountForm
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.components.CurrencyAndActions
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountEffect

@Composable
fun LandScapeLayout(
    createAccountViewModel: CreateAccountViewModel,
    navToLogin: () -> Unit,
    navToBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimens.extraLarge)
    ) {
        // IZQUIERDA
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CreateAccountForm(
                createAccountViewModel
            )
        }

        Spacer(modifier = Modifier.width(32.dp))

        // DERECHA
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CurrencyAndActions(
                createAccountViewModel,
                navToLogin,
                navToBack
            )
        }
    }
}
