package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.view


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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.CreateAccountViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.components.CreateAccountForm
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.components.CurrencyAndActions
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountEvent
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp


//Mapa de divisas y simbolos
@Composable
fun CreateAccountsScreen(
    createAccountViewModel: CreateAccountViewModel,
    navToLogin: () -> Unit,
    navToBack: () -> Unit
) {
    val state by createAccountViewModel.uiState.collectAsStateWithLifecycle()
    //val effects by createAccountViewModel.effect.collectAsState(initial = CreateAccountEffect.Idle)
    val isPortrait = orientation == OrientationApp.Portrait
    val isEnableFormButton = state.isFormValid
    LaunchedEffect(Unit) {
        createAccountViewModel.effect.collect { effect ->
            when (effect) {
                CreateAccountEffect.NavigateBack -> navToBack()
                CreateAccountEffect.NavigateToLogin -> navToLogin()
                else -> Unit
            }
        }
    }
    /*LaunchedEffect(effects) {
        when (effects) {
            CreateAccountEffect.NavigateBack -> navToBack()
            CreateAccountEffect.NavigateToLogin -> navToLogin()
            else -> {
            }
        }
    }*/
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
                CreateAccountForm(
                    state.accountName,
                    state.balance,
                    { createAccountViewModel.onEvent(CreateAccountEvent.AccountNameChanged(it)) },
                    { createAccountViewModel.onEvent(CreateAccountEvent.BalanceChanged(it)) },
                    { createAccountViewModel.onEvent(CreateAccountEvent.AddAccount) },
                    isEnableFormButton
                )

                CurrencyAndActions(
                    { createAccountViewModel.onEvent(CreateAccountEvent.DropdownExpandedChange(it)) },
                    { createAccountViewModel.onEvent(CreateAccountEvent.CurrencySelected(it)) },
                    state.isDropdownExpanded,
                    state.selectedCurrency,
                    { createAccountViewModel.onEvent(CreateAccountEvent.Confirm) },
                    { { createAccountViewModel.onEvent(CreateAccountEvent.BackToCreateProfile) } }
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
                    CreateAccountForm(
                        state.accountName,
                        state.balance,
                        { createAccountViewModel.onEvent(CreateAccountEvent.AccountNameChanged(it)) },
                        { createAccountViewModel.onEvent(CreateAccountEvent.BalanceChanged(it)) },
                        { createAccountViewModel.onEvent(CreateAccountEvent.AddAccount) },
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
                                CreateAccountEvent.DropdownExpandedChange(
                                    it
                                )
                            )
                        },
                        { createAccountViewModel.onEvent(CreateAccountEvent.CurrencySelected(it)) },
                        state.isDropdownExpanded,
                        state.selectedCurrency,
                        { createAccountViewModel.onEvent(CreateAccountEvent.Confirm) },
                        { { createAccountViewModel.onEvent(CreateAccountEvent.BackToCreateProfile) } }
                    )
                }
            }
        }
    }
}
