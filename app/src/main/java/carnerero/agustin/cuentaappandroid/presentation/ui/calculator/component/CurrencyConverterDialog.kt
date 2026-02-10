package carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.Currency
/*
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
*/

@Composable
fun CurrencyDialogConverter(
    accountsViewModel: AccountsViewModel,
    amountValue: String,               // Valor actual del campo "Amount"
    onAmountChange: (String) -> Unit,  // Callback de cambio
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val fromCurrency by accountsViewModel.fromCurrency.observeAsState("EUR")
    val toCurrency by accountsViewModel.toCurrency.observeAsState("USD")
    val currencies by accountsViewModel.currencyCodeList.observeAsState(emptyList())

    if (!showDialog) return

    var showDescription by remember { mutableStateOf(false) }

    AlertDialog(
        containerColor = colors.backgroundPrimary,
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.currency_dialog_title),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = colors.textHeadColor
                )
                Spacer(modifier = Modifier.width(dimens.small))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = colors.textHeadColor,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { showDescription = true }
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showDescription) {
                    R.string.currency_dialog_des.let {
                        Text(
                            modifier = Modifier.clickable { showDescription = false },
                            text = stringResource(id = it),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textColor
                        )
                    }
                }
                // Dropdown para la moneda "From"
                DropdownMenuComponent(
                    label = "From Currency",
                    selectedOption = currencies.find { it.currencyCode == fromCurrency } ?: Currency("EUR", "euro currency",R.drawable.eu),
                    options = currencies
                ) { selected ->
                    accountsViewModel.onChangeCurrencyFrom(selected.currencyCode)
                }

                DropdownMenuComponent(
                    label = "To Currency",
                    selectedOption = currencies.find { it.currencyCode == toCurrency } ?: Currency("USD","usd currency", R.drawable.us),
                    options = currencies
                ) { selected ->
                    accountsViewModel.onChangeCurrencyTo(selected.currencyCode)
                }
                // Solo un campo: Amount
                TextFieldComponent(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(id = R.string.currency_dialog_input), // o la etiqueta que quieras
                    inputText = amountValue,
                    onTextChange = onAmountChange,
                    BoardType.DECIMAL,
                    false
                )
            }
        },
        confirmButton = {
            ModelButton(
                text = stringResource(id = R.string.confirmButton),
                textStyle = MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(130.dp),
                enabledButton = true,
                onClickButton = onConfirm
            )
        },
        dismissButton = {
            ModelButton(
                text = stringResource(id = R.string.cancelButton),
                textStyle = MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(130.dp),
                enabledButton = true,
                onClickButton = onDismiss
            )
        }
    )
}


/*@Composable
fun CurrencyDialogConverter(
    accountsViewModel: AccountsViewModel,
    onDismiss: () -> Unit,
    onAction:()->Unit

) {
    val fromCurrency by accountsViewModel.fromCurrency.observeAsState("EUR")
    val toCurrency by accountsViewModel.toCurrency.observeAsState("USD")
    val currencies by accountsViewModel.currencyCodeList.observeAsState(emptyList())

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = colors.backgroundPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Dropdown para la moneda "From"
                DropdownMenuComponent(
                    label = "From Currency",
                    selectedOption = currencies.find { it.currencyCode == fromCurrency } ?: Currency("EUR", "euro currency",R.drawable.eu),
                    options = currencies
                ) { selected ->
                    accountsViewModel.onChangeCurrencyFrom(selected.currencyCode)
                }

                DropdownMenuComponent(
                    label = "To Currency",
                    selectedOption = currencies.find { it.currencyCode == toCurrency } ?: Currency("USD","usd currency", R.drawable.us),
                    options = currencies
                ) { selected ->
                    accountsViewModel.onChangeCurrencyTo(selected.currencyCode)
                }
                ModelButton(text = stringResource(
                    id = R.string.confirmButton
                ),
                    MaterialTheme.typography.labelMedium,
                    modifier = Modifier.width(320.dp),
                    true,
                    onClickButton = {
                        onAction()
                    })

            }
        }
    }
}*/
@Composable
fun DropdownMenuComponent(
    label: String,
    selectedOption: Currency,
    options: List<Currency>,
    onOptionSelected: (Currency) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val buttonWidth = remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Label del dropdown
        Text(
            text = label,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = colors.textColor,
            style = MaterialTheme.typography.titleSmall
        )

        // Botón principal con bandera + código
        Box(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    buttonWidth.value = coordinates.size.width // Captura el ancho del botón
                }
                .background(colors.backgroundPrimary, shape = MaterialTheme.shapes.small)
                .clickable { expanded = !expanded }
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = selectedOption.flag),
                    contentDescription = "${selectedOption.currencyCode} flag",
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = selectedOption.currencyCode,
                    color = colors.textColor,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        // Dropdown menu con ancho limitado al botón
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { buttonWidth.value.toDp() })
        ) {
            options.forEach { currency ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(currency)
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
