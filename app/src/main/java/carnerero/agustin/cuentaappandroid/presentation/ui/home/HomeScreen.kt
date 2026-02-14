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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.AccountCard
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.HeadCard
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.ModifyEntry
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.components.EntryListV2
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.utils.Utils
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel
) {

    val state by homeViewModel.uiState.collectAsStateWithLifecycle()
    val effects by homeViewModel.effect.collectAsStateWithLifecycle(initialValue = HomeEffects.Idle)
    val totalIncomes = state.totalIncomes
    val totalExpenses = state.totalExpenses
    val accounts = state.accounts
    val currencyCodeSelected = state.currencyCode
    val entries = state.listOfRecords
    val message=stringResource(R.string.noentries)
    val isLandscape =
        orientation == OrientationApp.Landscape
    val showEntries = state.showEntries
    val enableByDate = state.enableByDate
    LaunchedEffect(effects) {

        if(effects== HomeEffects.ShowNoEntriesMessage){
            SnackBarController.sendEvent(SnackBarEvent(message))
        }
    }
    if (showEntries) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val maxWidthDp = maxWidth * 0.85f
            val maxHeightDp = maxHeight
            val fieldModifier = Modifier
                .fillMaxWidth(0.85f) // mismo ancho para TODOS
                .heightIn(min = 48.dp)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.backgroundPrimary),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EntryListV2(
                    entries,
                    currencyCodeSelected,
                    enableByDate,
                    {
                        homeViewModel.onEvent(HomeUiEvents.ShowEnableByDate(it))
                    },
                    Modifier.width(maxWidthDp)
                        .weight(1f)
                )
                ModelButton(
                    text = stringResource(id = R.string.backButton),
                    MaterialTheme.typography.labelLarge,
                    modifier = fieldModifier,
                    true,
                    onClickButton = {
                        homeViewModel.onEvent(HomeUiEvents.SHowEntries(false))
                    }
                )
            }
        }
    } else {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.backgroundPrimary)
        ) {
            val contentWidth =
                if (isLandscape) maxWidth * 0.55f else maxWidth * 0.85f

            if (state.accounts.isEmpty()) {
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
                                Utils.numberFormat(totalIncomes, state.currencyCode),
                                true
                            ) {
                                homeViewModel.onEvent(
                                    HomeUiEvents.ShowIncomes
                                )
                            }

                            HeadCard(
                                modifier = Modifier.weight(1f),
                                Utils.numberFormat(totalExpenses, currencyCodeSelected),
                                false
                            ) {
                                homeViewModel.onEvent(
                                    HomeUiEvents.ShowExpenses
                                )
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
                                            homeViewModel.onEvent(
                                                HomeUiEvents.ShowAllRecordsByAccount(
                                                    account.id
                                                )
                                            )
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
                                Utils.numberFormat(totalIncomes, currencyCodeSelected),
                                true
                            ) {
                                homeViewModel.onEvent(
                                    HomeUiEvents.ShowIncomes
                                )
                            }

                            HeadCard(
                                modifier = Modifier.weight(1f),
                                Utils.numberFormat(totalExpenses, currencyCodeSelected),
                                false
                            ) {
                                homeViewModel.onEvent(
                                    HomeUiEvents.ShowExpenses
                                )
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
                                        homeViewModel.onEvent(
                                            HomeUiEvents.ShowAllRecordsByAccount(account.id)
                                        )
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
}























