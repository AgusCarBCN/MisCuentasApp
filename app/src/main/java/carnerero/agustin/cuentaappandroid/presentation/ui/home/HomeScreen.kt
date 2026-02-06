package carnerero.agustin.cuentaappandroid.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.AccountCard
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.HeadCard
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.utils.Utils
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp
import java.math.BigDecimal

@Composable
fun HomeScreen(
    accountsViewModel: AccountsViewModel,
    entriesViewModel: EntriesViewModel,
    navToEntries: () -> Unit
) {
    val incomes by entriesViewModel.totalIncomes.observeAsState(BigDecimal.ZERO)
    val expenses by entriesViewModel.totalExpenses.observeAsState(BigDecimal.ZERO)
    val currencyCodeSelected by accountsViewModel.currencyCodeSelected.observeAsState("EUR")
    val accounts by accountsViewModel.listOfAccounts.observeAsState(emptyList())


    val isLandscape =
        orientation == OrientationApp.Landscape

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
    ) {
        val contentWidth =
            if (isLandscape) maxWidth * 0.55f else maxWidth * 0.85f

        if (accounts.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.noaccounts),
                    color = colors.textColor,
                    fontSize = with(LocalDensity.current) {
                        dimensionResource(id = R.dimen.text_body_extra_large).toSp()
                    }
                )
            }
        } else {

            if (isLandscape) {
                /** ---------------- LANDSCAPE ---------------- */
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier
                            .weight(0.4f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        HeadSetting(
                            title = stringResource(id = R.string.seeall),
                            MaterialTheme.typography.headlineMedium
                        )
                        HeadCard(
                            modifier = Modifier.weight(1f),
                            Utils.numberFormat(incomes, currencyCodeSelected),
                            true
                        ) {
                            entriesViewModel.getAllIncomes()
                            navToEntries()
                        }

                        HeadCard(
                            modifier = Modifier.weight(1f),
                            Utils.numberFormat(expenses, currencyCodeSelected),
                            false
                        ) {
                            entriesViewModel.getAllExpenses()
                            navToEntries()
                        }


                    }
                    Column(
                        modifier = Modifier
                            .weight(0.6f)
                            .width(contentWidth),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        HeadSetting(
                            title = stringResource(id = R.string.youraccounts),
                            MaterialTheme.typography.headlineMedium
                        )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(accounts) { account ->
                                AccountCard(
                                    account,
                                    currencyCodeSelected,
                                    R.string.seeall,
                                    onClickCard = {
                                        entriesViewModel.getAllEntriesByAccount(account.id)
                                        navToEntries()
                                    }, modifier = Modifier
                                        .fillMaxWidth()

                                )
                            }
                        }
                    }
                }

            } else {
                /** ---------------- PORTRAIT ---------------- */
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HeadCard(
                            modifier = Modifier.weight(1f),
                            Utils.numberFormat(incomes, currencyCodeSelected),
                            true
                        ) {
                            entriesViewModel.getAllIncomes()
                            navToEntries()
                        }

                        HeadCard(
                            modifier = Modifier.weight(1f),
                            Utils.numberFormat(expenses, currencyCodeSelected),
                            false
                        ) {
                            entriesViewModel.getAllExpenses()
                            navToEntries()
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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
                                R.string.seeall,
                                onClickCard = {
                                    entriesViewModel.getAllEntriesByAccount(account.id)
                                    navToEntries()
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}


/*
@Composable
fun HomeScreen(
    accountsViewModel: AccountsViewModel,
    entriesViewModel: EntriesViewModel,
    navToEntries: ()->Unit
) {
    val incomes by entriesViewModel.totalIncomes.observeAsState(BigDecimal.ZERO)
    val expenses by entriesViewModel.totalExpenses.observeAsState(BigDecimal.ZERO)
    val currencyCodeSelected by accountsViewModel.currencyCodeSelected.observeAsState("EUR")
    val accounts by accountsViewModel.listOfAccounts.observeAsState(emptyList())

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxWidthDp = maxWidth
        val maxHeightDp = maxHeight
        val fieldModifier = Modifier
            .fillMaxWidth(0.85f) // mismo ancho para TODOS
            .heightIn(min = 48.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.backgroundPrimary),
            verticalArrangement = Arrangement.Center,  // Centra los elementos verticalmente
            horizontalAlignment = Alignment.CenterHorizontally  // Centra los elementos horizontalmente
        ) {
            if (accounts.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.noaccounts),
                    color = colors.textColor,
                    fontSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_body_extra_large).toSp() })
            } else {
                Spacer(modifier = Modifier.height(20.dp))
                Row(fieldModifier,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    HeadCard(
                        modifier = Modifier.weight(1f),
                        Utils.numberFormat(incomes, currencyCodeSelected),
                        true,
                        onClickCard = {
                            entriesViewModel.getAllIncomes()
                            navToEntries()
                        })

                    HeadCard(
                        modifier = Modifier.weight(1f),
                        Utils.numberFormat(expenses, currencyCodeSelected),
                        false,
                        onClickCard = {
                            entriesViewModel.getAllExpenses()
                            navToEntries()
                        })
                }
                Spacer(modifier = Modifier.width(16.dp))
                HeadSetting(
                    title = stringResource(id = R.string.youraccounts),
                    MaterialTheme.typography.headlineMedium
                )

                // Mostrar las cuentas si están disponibles
                LazyColumn(
                    modifier = fieldModifier,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                    items(accounts) { account -> // Solo utiliza accounts
                        AccountCard(
                            account,
                            currencyCodeSelected,
                            R.string.seeall,
                            onClickCard = {
                                entriesViewModel.getAllEntriesByAccount(account.id)
                                navToEntries()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = maxWidthDp * 0.9f)
                        )  // Crea un card para cada cuenta en la lista
                         // Espacio entre cada card (separación)
                    }
                }
            }
        }
    }
}*/























