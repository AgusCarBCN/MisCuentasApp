package carnerero.agustin.cuentaappandroid.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import carnerero.agustin.cuentaappandroid.R

import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.AccountCard
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.HeadCard
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.math.BigDecimal


@Composable
fun HomeScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    accountsViewModel: AccountsViewModel = hiltViewModel(),
    entriesViewModel: EntriesViewModel = hiltViewModel(),
    navToEntries: ()->Unit
) {
    val incomes by entriesViewModel.totalIncomes.observeAsState(BigDecimal.ZERO)
    val expenses by entriesViewModel.totalExpenses.observeAsState(BigDecimal.ZERO)
    val currencyCodeSelected by accountsViewModel.currencyCodeSelected.observeAsState("EUR")
    val accounts by accountsViewModel.listOfAccounts.observeAsState(emptyList())
    // Observa el estado de la lista de cuentas

    LaunchedEffect(Unit) {
        entriesViewModel.getTotal()
        accountsViewModel.getAllAccounts()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColorsPalette.current.backgroundPrimary),
        verticalArrangement = Arrangement.Center,  // Centra los elementos verticalmente
        horizontalAlignment = Alignment.CenterHorizontally  // Centra los elementos horizontalmente
    ) {
        if (accounts.isEmpty()) {
            Text(text = stringResource(id = R.string.noaccounts),
                color = LocalCustomColorsPalette.current.textColor,
                fontSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_body_extra_large).toSp() })
        }
        else{
            Row(modifier = Modifier.padding(top = 20.dp)) {
                HeadCard(modifier = Modifier.weight(0.5f),
                    Utils.numberFormat(incomes,currencyCodeSelected),
                    true,
                    onClickCard={
                    //mainViewModel.selectScreen(IconOptions.ENTRIES)
                    entriesViewModel.getAllIncomes()
                    navToEntries()
                    })
                Spacer(modifier = Modifier.width(5.dp))  // Espacio entre los dos cards
                HeadCard(modifier = Modifier.weight(0.5f),
                    Utils.numberFormat(expenses,currencyCodeSelected),
                    false,
                    onClickCard={
                        //mainViewModel.selectScreen(IconOptions.ENTRIES)
                    entriesViewModel.getAllExpenses()
                    navToEntries()
                    })
            }

            Spacer(modifier = Modifier.width(5.dp))
            HeadSetting(title = stringResource(id = R.string.youraccounts),
                MaterialTheme.typography.headlineMedium)

            // Mostrar las cuentas si están disponibles
            LazyColumn(
                modifier = Modifier.fillMaxSize(),

                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                items(accounts) { account -> // Solo utiliza accounts
                    AccountCard(
                        account,
                        currencyCodeSelected,
                        R.string.seeall,
                        onClickCard = { //mainViewModel.selectScreen(IconOptions.ENTRIES)
                            entriesViewModel.getAllEntriesByAccount(account.id)
                                navToEntries()
                        }
                    )  // Crea un card para cada cuenta en la lista
                    Spacer(modifier = Modifier.height(20.dp))  // Espacio entre cada card (separación)
                }
            }
        }
    }
 }
























