package carnerero.agustin.cuentaappandroid.presentation.ui.changecurrency.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CurrencyChangeSection(
    accountsViewModel: AccountsViewModel,
    entriesViewModel: EntriesViewModel,
    scope: CoroutineScope,
    currencyCodeShowed: String,
    currencyCodeSelected: String,
    messageChange: String,
    messageError: String
) {
    val fieldModifier = Modifier
        .fillMaxWidth(0.85f)
        .heightIn(min = 48.dp)

    HeadSetting(
        title = stringResource(R.string.changecurrencytext),
        MaterialTheme.typography.headlineSmall
    )

    ModelButton(
        text = stringResource(R.string.changeCurrency),
        MaterialTheme.typography.labelLarge,
        modifier = fieldModifier,
        true
    ) {
        scope.launch(Dispatchers.IO) {
            accountsViewModel.setCurrencyCode(currencyCodeShowed)

            val ratio = accountsViewModel.conversionCurrencyRate(
                currencyCodeSelected,
                currencyCodeShowed
            )

            if (ratio != null) {
                entriesViewModel.updateEntriesAmountByExchangeRate(ratio)
                entriesViewModel.getTotal()
                entriesViewModel.getAllEntriesDTO()
                accountsViewModel.updateAccountsBalancesByExchangeRates(ratio)
                accountsViewModel.getAllAccounts()

                withContext(Dispatchers.Main) {
                    SnackBarController.sendEvent(
                        SnackBarEvent(messageChange)
                    )
                }
            } else {
                SnackBarController.sendEvent(
                    SnackBarEvent(messageError)
                )
            }
        }
    }
}
