package carnerero.agustin.cuentaappandroid.changecurrency

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.SnackBarController
import carnerero.agustin.cuentaappandroid.SnackBarEvent
import carnerero.agustin.cuentaappandroid.components.CurrencySelector
import carnerero.agustin.cuentaappandroid.components.HeadSetting
import carnerero.agustin.cuentaappandroid.components.IconAnimated
import carnerero.agustin.cuentaappandroid.components.ModelButton
import carnerero.agustin.cuentaappandroid.createaccounts.view.AccountsViewModel
import carnerero.agustin.cuentaappandroid.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.newamount.view.EntriesViewModel
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
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
    val accounts by accountsViewModel.listOfAccounts.observeAsState()
    val entries by entriesViewModel.listOfEntriesDB.collectAsState()
    val messageFormatCurrencyChange = stringResource(id = R.string.currencyformatchange)
    val messageCurrencyChange = stringResource(id = R.string.currencychange)
    val messageErrorConnexionApi=stringResource(id = R.string.apierror)
    LaunchedEffect(Unit) {
        accountsViewModel.getListOfCurrencyCode()
    }
    Column(modifier=Modifier
        .verticalScroll(
            rememberScrollState()
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        IconAnimated(
            iconResource = R.drawable.ic_change_currency, sizeIcon = 140,
            LocalCustomColorsPalette.current.imageTutorialInit,
            LocalCustomColorsPalette.current.imageTutorialTarget
        )
        CurrencySelector(accountsViewModel)
        HeadSetting(title = stringResource(id = R.string.changeformattext), 16)
        ModelButton(text = stringResource(id = R.string.changeFormat),
            R.dimen.text_title_medium,
            modifier = Modifier.width(360.dp),
            true,
            onClickButton = {

                scope.launch(Dispatchers.Main) {
                    accountsViewModel.setCurrencyCode(currencyCodeShowed)
                    SnackBarController.sendEvent(event = SnackBarEvent(messageFormatCurrencyChange))
                }
            }
        )
        HeadSetting(title = stringResource(id = R.string.changecurrencytext), 16)
        ModelButton(text = stringResource(id = R.string.changeCurrency),
            R.dimen.text_title_medium,
            modifier = Modifier.width(360.dp),
            true,
            onClickButton = {
                scope.launch(Dispatchers.IO) {
                    accountsViewModel.setCurrencyCode(currencyCodeShowed)

                    // Llama a conversionCurrencyRate y espera el resultado
                    val ratio = accountsViewModel.conversionCurrencyRate(currencyCodeSelected, currencyCodeShowed)
                    Log.d("ratio",ratio.toString())
                    if(ratio!=null) {
                        entriesViewModel.getAllEntriesDataBase()
                        accountsViewModel.getAllAccounts()
                        accounts?.forEach { account ->
                            val newBalance = account.balance * (ratio )
                            val id = account.id
                            accountsViewModel.upDateAccountBalance(id, newBalance)

                        }
                        entries.forEach { entry ->
                            val newAmount = entry.amount * (ratio)
                            val id = entry.id
                            entriesViewModel.upDateAmountEntry(id, newAmount)
                        }
                        entriesViewModel.getTotal()

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
            R.dimen.text_title_medium,
            modifier = Modifier.width(360.dp),
            true,
            onClickButton = {
                accountsViewModel.onCurrencyShowedChange(currencyCodeSelected)
                mainViewModel.selectScreen(IconOptions.HOME)
            })
    }
}
