package carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.utils.Utils
@Composable
fun AccountSelector(

    title: String,
    accountViewModel: AccountsViewModel,
    isAccountDestination: Boolean = false,
    modifier: Modifier
) {
    // Observa el estado de la lista de cuentas y la moneda
    val accounts by accountViewModel.listOfAccounts.observeAsState(emptyList())
    val currencyCodeSelected by accountViewModel.currencyCodeSelected.observeAsState("USD")
    val selected = stringResource(id = R.string.select)
    val destination = stringResource(id = R.string.destinationaccount)
    val origin = stringResource(id = R.string.originaccount)

    // Inicializamos el estado del VerticalPager basado en la cantidad de cuentas
    val pagerState = rememberPagerState(pageCount = { accounts.size })
    val isDraggingUp by remember { derivedStateOf { pagerState.currentPage == 0 || pagerState.targetPage > pagerState.currentPage } }
    accountViewModel.getAllAccounts()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = if (isDraggingUp) R.drawable.arrow_up else R.drawable.arrow_down),
                contentDescription = null,
                tint = colors.textColor,
                modifier = Modifier.size(24.dp)

            )
            Spacer(modifier = Modifier.width(dimens.small))
            Text(
                text = title,
                color = colors.textColor,
                //modifier = Modifier.padding(vertical = 10.dp),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(modifier = Modifier.width(dimens.small))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.backgroundSecondary)
                    .height(60.dp),
            ) { page ->
                // Actualiza la cuenta seleccionada en ViewModel
                if (isAccountDestination) {
                    accountViewModel.onDestinationAccountSelected(accounts[pagerState.targetPage])
                } else {
                    accountViewModel.onAccountSelected(accounts[pagerState.targetPage])
                }
                val balanceFormatted =
                    Utils.numberFormat(accounts[page].balance, currencyCodeSelected)
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.semantics {
                            contentDescription =
                                if (isAccountDestination) "$destination {$accounts[page].name} $selected"
                                else "$origin {$accounts[page].name} $selected"

                        },
                        text = accounts[page].name,
                        color = colors.textColor,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.width(dimens.medium))
                    Text(
                        modifier = Modifier.semantics {
                            contentDescription =
                                if (isAccountDestination) "$destination {$accounts[page].balance} $selected"
                                else "$origin {$accounts[page].balance} $selected"

                        },
                        text = balanceFormatted,
                        color = colors.incomeColor,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            }
        }
    }
}