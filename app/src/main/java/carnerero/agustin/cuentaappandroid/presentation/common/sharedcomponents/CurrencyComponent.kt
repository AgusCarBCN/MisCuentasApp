package carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.wear.compose.material3.Text
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.Currency
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens

@Composable
fun CurrencySelector(
    accountsViewModel: AccountsViewModel,
    modifier: Modifier = Modifier
) {
    val currencyCodeShowed by accountsViewModel.currencyCodeShowed.observeAsState("USD")
    val isExpanded by accountsViewModel.isCurrencyExpanded.observeAsState(false)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.backgroundPrimary)
            .padding(dimens.smallMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ModelButton(
            text = stringResource(id = R.string.selectcurrencyoption),
            textStyle = MaterialTheme.typography.labelLarge,
            modifier = Modifier.fillMaxWidth(),
            onClickButton = {
                accountsViewModel.onExpandedChange(true)
            }
        )

        Spacer(modifier = Modifier.height(dimens.medium))

        Text(
            text = "${stringResource(id = R.string.selectedcurrency)} $currencyCodeShowed",
            color = colors.textColor,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }

    if (isExpanded) {
        CurrencyDialog(accountsViewModel)
    }
}



@Composable
private fun CurrencyDialog(accountsViewModel: AccountsViewModel) {

    val currencies by accountsViewModel.currencyCodeList.observeAsState(emptyList())

    Dialog(
        onDismissRequest = {
            accountsViewModel.onExpandedChange(false)
        }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = colors.backgroundPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(dimens.medium),
                verticalArrangement = Arrangement.spacedBy(dimens.small)
            ) {
                items(currencies) { currency ->
                    CurrencyListItem(currency) {
                        accountsViewModel.onCurrencyShowedChange(currency.currencyCode)
                        accountsViewModel.onExpandedChange(false)
                    }
                }
            }
        }
    }
}

/*@Composable
fun CurrencySelector(
    accountsViewModel: AccountsViewModel,
    modifier: Modifier = Modifier
) {
    val currencyCodeShowed by accountsViewModel.currencyCodeShowed.observeAsState("USD")
    val isExpanded by accountsViewModel.isCurrencyExpanded.observeAsState(false)
    val currencies by accountsViewModel.currencyCodeList.observeAsState(emptyList())

    BoxWithConstraints(modifier = modifier) {
        val contentWidth = maxWidth * 0.9f

        Column(
            modifier = Modifier
                .width(contentWidth)
                .background(colors.backgroundPrimary)
                .padding(dimens.smallMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isExpanded) {
                ModelButton(
                    text = stringResource(id = R.string.selectcurrencyoption),
                    MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth(),
                    onClickButton = {
                        accountsViewModel.onExpandedChange(true)
                    }
                )

                Spacer(modifier = Modifier.height(dimens.medium))

                Text(
                    text = "${stringResource(id = R.string.selectedcurrency)} $currencyCodeShowed",
                    color = colors.textColor,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )

            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    items(currencies) { currency ->
                        CurrencyListItem(currency) {
                            accountsViewModel.onCurrencyShowedChange(currency.currencyCode)
                            accountsViewModel.onExpandedChange(false)
                        }
                    }
                }
            }
        }
    }
}*/




@Composable
private fun CurrencyListItem(currency: Currency, onCurrencySelected: () -> Unit) {
    // Elemento de lista para mostrar cada moneda
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCurrencySelected) // Acción al hacer clic
            .padding(dimens.mediumLarge),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = currency.flag),
            contentDescription = "${currency.currencyCode} flag",
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.width(dimens.mediumLarge)) // Espaciador entre la imagen y el texto
        Text(
            text = currency.currencyDescription,
            color = colors.textColor,
            style=MaterialTheme.typography.bodyMedium
        )
    }
}


