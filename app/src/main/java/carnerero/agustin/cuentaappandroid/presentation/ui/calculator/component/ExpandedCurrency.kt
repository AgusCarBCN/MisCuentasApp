package carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component

import android.icu.number.Precision.currency
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.Currency

@Composable
fun CurrencyDialogConverter(
    onDismiss: () -> Unit,
    accountsViewModel: AccountsViewModel
) {
    val fromCurrency by accountsViewModel.fromCurrency.observeAsState("EUR")
    val toCurrency by accountsViewModel.toCurrency.observeAsState("USD")
    val currencies by accountsViewModel.currencyCodeList.observeAsState(emptyList())
    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = colors.backgroundPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
        ) {
            Column(Modifier.fillMaxWidth()) {
                DropdownMenuComponent("from Currency",
                    fromCurrency,
                    currencies) {
                        selected ->
                    accountsViewModel.onChangeCurrencyFrom(selected.currencyCode)
                }
                Spacer(modifier = Modifier.height(8.dp))
                DropdownMenuComponent("to Currency",
                    toCurrency,
                    currencies) {
                        selected ->
                    accountsViewModel.onChangeCurrencyTo(selected.currencyCode)
                }
            }
        }
    }
}

@Composable
fun DropdownMenuComponent(
    label: String,
    selectedOption: String,
    options: List<Currency>,
    onOptionSelected: (Currency) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                text = label,
                modifier = Modifier
                    .padding(top = dimens.smallMedium)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = colors.textColor,
                style = MaterialTheme.typography.titleSmall
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.backgroundPrimary)
                    .clickable { expanded = !expanded }
                    .padding(8.dp)
            ) {
                Text(
                    text = selectedOption,
                    modifier = Modifier
                        .padding(top = dimens.smallMedium)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = colors.textColor,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { currency ->
                    DropdownMenuItem(
                        onClick = {
                            onOptionSelected(currency)  // ✅ pasar la moneda seleccionada
                            expanded = false
                        },
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = currency.flag),
                                contentDescription = "${currency.currencyCode} flag",
                                modifier = Modifier.size(36.dp)
                            )
                        },
                        text = { Text(currency.currencyCode) }
                    )
                }
            }
        }
    }
}



