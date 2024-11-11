package com.blogspot.agusticar.miscuentasv2.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.components.AccountCardWithCheckbox
import carnerero.agustin.cuentaappandroid.components.HeadSetting
import carnerero.agustin.cuentaappandroid.createaccounts.view.AccountsViewModel
import carnerero.agustin.cuentaappandroid.search.SearchViewModel

@Composable
fun EntryAccountList(
    accountsViewModel: AccountsViewModel,
    searchViewModel: SearchViewModel
) {
    // Observa la lista de categorías desde el ViewModel
    val listOfAccounts by accountsViewModel.listOfAccounts.observeAsState(emptyList())
    val currencyCode by accountsViewModel.currencyCodeShowed.observeAsState("USD")
    LaunchedEffect(Unit) {
        accountsViewModel.getAllAccounts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadSetting(title = stringResource(id = R.string.selectaccounts),
            size = 22)
        // Asegúrate de que la LazyColumn ocupa solo el espacio necesario
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Permite que la columna ocupe el espacio disponible
                .padding(bottom = 16.dp), // Espacio en la parte inferior
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(listOfAccounts) { account ->
                AccountCardWithCheckbox(
                    account,
                    currencyCode,
                    accountsViewModel,
                    searchViewModel,
                    onCheckBoxChange = { checked ->
                        accountsViewModel.updateCheckedAccount(
                            account.id,
                            checked
                        )
                        if (!account.isChecked) {
                            accountsViewModel.onEnableDialogChange(true)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))  // Espacio entre cada card (separación)
            }
        }
    }
}

