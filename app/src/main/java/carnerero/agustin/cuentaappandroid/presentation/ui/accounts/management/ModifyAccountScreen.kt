package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.management

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.AccountCard
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting

@Composable

fun ModifyAccountsScreen(
    accountsViewModel: AccountsManagementViewModel
) {
    val state by accountsViewModel.uiState.collectAsStateWithLifecycle()
    val accounts = state.accounts
    val currencyCodeSelected = state.currencyCode
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadSetting(
            title = stringResource(id = R.string.youraccounts),
            MaterialTheme.typography.headlineMedium
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(0.85f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(accounts) { account ->
                AccountCard(
                    account,
                    currencyCodeSelected,
                    R.string.modify,
                    onClickCard = {
                        //deleteAccountsViewModel.onEventUser(AccountUiEvent.OnOpenDialog(account.id))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}