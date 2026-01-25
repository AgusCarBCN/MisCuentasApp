package carnerero.agustin.cuentaappandroid.presentation.ui.changecurrency

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.CurrencySelector
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable


fun ChangeCurrencyScreen(mainViewModel: MainViewModel,
                         accountsViewModel: AccountsViewModel,
                         entriesViewModel: EntriesViewModel
) {

    val scope = rememberCoroutineScope()
    val currencyCodeShowed by accountsViewModel.currencyCodeShowed.observeAsState("EUR")
    val currencyCodeSelected by accountsViewModel.currencyCodeSelected.observeAsState("EUR")
    val messageFormatCurrencyChange = stringResource(id = R.string.currencyformatchange)
    val messageCurrencyChange = stringResource(id = R.string.currencychange)
    val messageErrorConnexionApi=stringResource(id = R.string.apierror)


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        HeadSetting(title = stringResource(id = R.string.changecurrency),MaterialTheme.typography.headlineMedium )
        CurrencySelector(accountsViewModel)
        HeadSetting(title = stringResource(id = R.string.changeformattext),MaterialTheme.typography.headlineSmall )
        ModelButton(text = stringResource(id = R.string.changeFormat),
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(360.dp),
            true,
            onClickButton = {
                scope.launch(Dispatchers.Main) {
                    accountsViewModel.setCurrencyCode(currencyCodeShowed)
                    SnackBarController.sendEvent(event = SnackBarEvent(messageFormatCurrencyChange))
                }
            }
        )
        HeadSetting(title = stringResource(id = R.string.changecurrencytext), MaterialTheme.typography.headlineSmall)
        ModelButton(text = stringResource(id = R.string.changeCurrency),
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(360.dp),
            true,
            onClickButton = {
                scope.launch(Dispatchers.IO) {
                    accountsViewModel.setCurrencyCode(currencyCodeShowed)
                    // Llama a conversionCurrencyRate y espera el resultado
                    val ratio = accountsViewModel.conversionCurrencyRate(currencyCodeSelected, currencyCodeShowed)
                    Log.d("ratio",ratio.toString())
                    if(ratio!=null) {
                        entriesViewModel.updateEntriesAmountByExchangeRate(ratio)
                        entriesViewModel.getTotal()
                        entriesViewModel.getAllEntriesDTO()
                        accountsViewModel.updateAccountsBalancesByExchangeRates(ratio)
                        accountsViewModel.getAllAccounts()
                        withContext(Dispatchers.Main) {
                            SnackBarController.sendEvent(event = SnackBarEvent(messageCurrencyChange))
                        }
                    }else{
                        SnackBarController.sendEvent(event = SnackBarEvent(messageErrorConnexionApi))
                    }
                }
            }
        )
        ModelButton(text = stringResource(id = R.string.backButton),
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(360.dp),
            true,
            onClickButton = {
                accountsViewModel.onCurrencyShowedChange(currencyCodeSelected)
                mainViewModel.selectScreen(IconOptions.HOME)
            })
    }
}
