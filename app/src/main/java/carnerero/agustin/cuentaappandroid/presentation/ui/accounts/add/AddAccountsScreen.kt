package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.components.AddAccountForm
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.components.CurrencyAndActions
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.model.AddAccountEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.model.AddAccountEvent
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp


//Mapa de divisas y simbolos
@Composable
fun AddAccountsScreen(
    createAccountViewModel: AddAccountViewModel,
    navToLogin: () -> Unit,
    navToBack: () -> Unit
) {
    val state by createAccountViewModel.uiState.collectAsStateWithLifecycle()
    val isPortrait = orientation == OrientationApp.Portrait
    val isEnableFormButton = state.isFormValid
    LaunchedEffect(Unit) {
        createAccountViewModel.effect.collect { effect ->
            when (effect) {
                AddAccountEffect.NavigateBack -> navToBack()
                AddAccountEffect.NavigateToLogin -> navToLogin()
                else -> Unit
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
    ) {
        if (isPortrait) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = dimens.extraLarge)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AddAccountForm(
                    state.accountName,
                    state.balance,
                    { createAccountViewModel.onEvent(AddAccountEvent.AccountNameChanged(it)) },
                    { createAccountViewModel.onEvent(AddAccountEvent.BalanceChanged(it)) },
                    { createAccountViewModel.onEvent(AddAccountEvent.AddAccount) },
                    isEnableFormButton
                )

                CurrencyAndActions(
                    { createAccountViewModel.onEvent(AddAccountEvent.DropdownExpandedChange(it)) },
                    { createAccountViewModel.onEvent(AddAccountEvent.CurrencySelected(it)) },
                    state.isDropdownExpanded,
                    state.selectedCurrency,
                    { createAccountViewModel.onEvent(AddAccountEvent.Confirm) },
                    { { createAccountViewModel.onEvent(AddAccountEvent.BackToCreateProfile) } }
                )
            }
        } else {
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
                    AddAccountForm(
                        state.accountName,
                        state.balance,
                        { createAccountViewModel.onEvent(AddAccountEvent.AccountNameChanged(it)) },
                        { createAccountViewModel.onEvent(AddAccountEvent.BalanceChanged(it)) },
                        { createAccountViewModel.onEvent(AddAccountEvent.AddAccount) },
                        isEnableFormButton
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
                        {
                            createAccountViewModel.onEvent(
                                AddAccountEvent.DropdownExpandedChange(
                                    it
                                )
                            )
                        },
                        { createAccountViewModel.onEvent(AddAccountEvent.CurrencySelected(it)) },
                        state.isDropdownExpanded,
                        state.selectedCurrency,
                        { createAccountViewModel.onEvent(AddAccountEvent.Confirm) },
                        { { createAccountViewModel.onEvent(AddAccountEvent.BackToCreateProfile) } }
                    )
                }
            }
        }
    }
}
