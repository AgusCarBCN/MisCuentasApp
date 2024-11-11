package carnerero.agustin.cuentaappandroid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.createaccounts.view.AccountsViewModel
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.utils.Utils


@Composable
fun AccountSelector(
    size:Int,
    spacerWidth: Int,
    title: String,
    accountViewModel: AccountsViewModel,
    isAccountDestination: Boolean = false
) {
    // Observa el estado de la lista de cuentas y la moneda
    val accounts by accountViewModel.listOfAccounts.observeAsState(emptyList())
    val currencyCodeSelected by accountViewModel.currencyCodeSelected.observeAsState("USD")

    // Inicializamos el estado del VerticalPager basado en la cantidad de cuentas
    val pagerState = rememberPagerState(pageCount = { accounts.size })
    val isDraggingUp by remember { derivedStateOf { pagerState.currentPage == 0 || pagerState.targetPage > pagerState.currentPage } }

        accountViewModel.getAllAccounts()


    Column(
        modifier = Modifier
            .width(size.dp)
            .background(LocalCustomColorsPalette.current.backgroundPrimary)
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .background(LocalCustomColorsPalette.current.backgroundPrimary),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = if (isDraggingUp) R.drawable.arrow_up else R.drawable.arrow_down),
                contentDescription = null,
                tint = LocalCustomColorsPalette.current.textColor,
                modifier = Modifier
                    .width(36.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = title,
                fontSize = 20.sp,
                color = LocalCustomColorsPalette.current.textColor,
                modifier = Modifier.padding(vertical = 10.dp),
                textAlign = TextAlign.Start
            )
        }

        Card(
            modifier = Modifier.width(size.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalCustomColorsPalette.current.backgroundSecondary)
                    .height(60.dp),
            ) { page ->
                // Actualiza la cuenta seleccionada en ViewModel
                if (isAccountDestination) {
                    accountViewModel.onDestinationAccountSelected(accounts[pagerState.targetPage])
                } else {
                    accountViewModel.onAccountSelected(accounts[pagerState.targetPage])
                }

                val balanceFormatted = Utils.numberFormat(accounts[page].balance, currencyCodeSelected)

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = accounts[page].name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalCustomColorsPalette.current.textColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(spacerWidth.dp))
                    Text(
                        text = balanceFormatted,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalCustomColorsPalette.current.incomeColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
