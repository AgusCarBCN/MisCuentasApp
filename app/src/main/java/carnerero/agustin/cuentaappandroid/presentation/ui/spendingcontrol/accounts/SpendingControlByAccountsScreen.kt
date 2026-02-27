package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel

@Composable

fun SpendingControlByAccountsScreen(accountsViewModel: AccountsViewModel) {
    // Observa la lista de categorías desde el ViewModel
    val listOfAccountsChecked by accountsViewModel.listOfAccountsChecked.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        accountsViewModel.getAllAccountsChecked()
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
           /* items(listOfAccountsChecked) { account ->
               /* AccountBudgetItemControl(
                    account,
                    accountsViewModel
                )
                SpacerApp()*/

            }*/
        }
    }
}