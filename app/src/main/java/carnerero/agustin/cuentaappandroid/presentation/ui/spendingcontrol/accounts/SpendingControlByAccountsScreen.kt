package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SpacerApp

@Composable

fun SpendingControlByAccountsScreen(accountsViewModel: AccountsSpendingControlViewModel) {
    // Observa la lista de categorías desde el ViewModel

    val state by accountsViewModel.uiState.collectAsStateWithLifecycle()
    val accountsChecked = state.accounts.filter { it.isChecked }

    // Solo observar cuando cambian las categorías seleccionadas
    LaunchedEffect(accountsChecked.map { it.id }) {
        accountsChecked.forEach { account ->
            accountsViewModel.observeCategorySpending(account)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Asegúrate de que la LazyColumn ocupa solo el espacio necesario
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Permite que la columna ocupe el espacio disponible
                .padding(bottom = 16.dp) // Espacio en la parte inferior
        ) {
            items(accountsChecked, key = { it.id }) { account ->

                val accountBudget = state.accountBudgetMap[account.id]

                AccountBudgetItemControl(
                    account = account,
                    currencyCode = state.currencyCode,
                    uiState = accountBudget
                )

                SpacerApp()
            }
        }
    }
}