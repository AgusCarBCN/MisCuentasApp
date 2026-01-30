package carnerero.agustin.cuentaappandroid.presentation.ui.setting

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.AccountCard
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.IconAnimated
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelDialog
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.utils.navigateTopLevel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Composable
fun AccountList(
    mainViewModel: MainViewModel,
    accountsViewModel: AccountsViewModel,
    option: Boolean,
    navController: NavController
) {
    val currencyCode by accountsViewModel.currencyCodeShowed.observeAsState("USD")
    accountsViewModel.getAllAccounts()
    // Observa el estado de la lista de cuentas
    val accounts by accountsViewModel.listOfAccounts.observeAsState(null)   // Observa el estado de la lista de cuentas
    val accountSelected by accountsViewModel.accountSelected.observeAsState(null)
    val showDeleteAccountDialog by mainViewModel.showDeleteAccountDialog.collectAsState(false)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColorsPalette.current.backgroundPrimary),
        verticalArrangement = Arrangement.Center,  // Centra los elementos verticalmente
        horizontalAlignment = Alignment.CenterHorizontally  // Centra los elementos horizontalmente
    ) {
        if (accounts.isNullOrEmpty()) {
            Text(
                text = stringResource(id = R.string.noaccounts),
                color = LocalCustomColorsPalette.current.textColor,
                fontSize = 18.sp
            )
        } else {

            HeadSetting(
                title = stringResource(id = R.string.youraccounts),
                MaterialTheme.typography.headlineSmall
            )
            // Mostrar las cuentas si están disponibles
            LazyColumn(
                modifier = Modifier.fillMaxSize(),

                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                items(accounts!!) { account -> // Solo utiliza accounts
                    AccountCard(
                        account,
                        currencyCode,
                        if (option) R.string.deleteaccount else R.string.modify,
                        onClickCard = {
                            accountsViewModel.onAccountSelected(account)

                            if (option) {
                                mainViewModel.showDeleteAccountDialog(true)
                            }else{
                                navController.navigateTopLevel(Routes.ModifyAccountItem.createRoute(
                                    accountSelected?.id ?: 0
                                ))
                            }
                        }
                    )  // Crea un card para cada cuenta en la lista
                    Spacer(modifier = Modifier.height(20.dp))  // Espacio entre cada card (separación)
                }
            }
        }
    }
    ModelDialog(
        R.string.titledelete,
        R.string.deleteinfo,
        showDialog = showDeleteAccountDialog,
        onConfirm = {
            accountSelected?.let { accountsViewModel.deleteAccount(it) }
            mainViewModel.showDeleteAccountDialog(false)
           },
        onDismiss = {
            mainViewModel.showDeleteAccountDialog(false)
        })

}

@Composable
fun ModifyAccountsComponent(
    accountsViewModel: AccountsViewModel,
    accountId: Int?,
    navToHome:()->Unit

    ) {
    val scope = rememberCoroutineScope()
    val nameButtonChange by accountsViewModel.isEnableChangeNameButton.observeAsState(false)
    val balanceButtonChange by accountsViewModel.isEnableChangeBalanceButton.observeAsState(false)
    //val accountSelected by accountsViewModel.accountSelected.observeAsState()

    //val accountId = accountSelected?.id ?: 0

    val name by accountsViewModel.newName.observeAsState("")
    val balance by accountsViewModel.newAmount.observeAsState("")


    //SnackBarMessage
    val nameChanged = message(resource = R.string.namechanged)
    val balanceChanged = message(resource = R.string.amountchanged)
    Column(

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        HeadSetting(
            title = stringResource(id = R.string.edit_account),
            MaterialTheme.typography.headlineSmall
        )

        IconAnimated(
            iconResource = R.drawable.configaccountoption, sizeIcon = 120,
            LocalCustomColorsPalette.current.imageTutorialInit,
            LocalCustomColorsPalette.current.imageTutorialTarget
        )


        TextFieldComponent(
            modifier = Modifier.width(360.dp),
            stringResource(id = R.string.amountName),
            name,
            onTextChange = { accountsViewModel.onTextNameChanged(it) },
            BoardType.TEXT,
            false
        )
        ModelButton(
            text = stringResource(id = R.string.change),
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(360.dp),
            nameButtonChange,
            onClickButton = {
                try {
                    scope.launch(Dispatchers.IO) {
                        accountsViewModel.upDateAccountName(accountId!!, name)
                        SnackBarController.sendEvent(event = SnackBarEvent(nameChanged))
                    }
                } catch (e: Exception) {
                    Log.d("Cuenta", "Error: ${e.message}")
                    println("Error al cargar ${e.message}")
                }
            }
        )


        TextFieldComponent(
            modifier = Modifier.width(360.dp),
            stringResource(id = R.string.enteramount),
            balance,
            onTextChange = {
                accountsViewModel.onTextBalanceChanged(it)
            },
            BoardType.DECIMAL,
            false
        )



        ModelButton(
            text = stringResource(id = R.string.change),
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(360.dp),
            balanceButtonChange,
            onClickButton = {
                try {
                    scope.launch(Dispatchers.IO) {
                        val newBalance = balance.toBigDecimalOrNull() ?: BigDecimal.ZERO
                        accountsViewModel.upDateAccountBalance(accountId!!, newBalance)
                        SnackBarController.sendEvent(event = SnackBarEvent(balanceChanged))

                    }
                } catch (e: Exception) {
                    Log.d("Cuenta", "Error: ${e.message}")
                    println("Error al cargar ${e.message}")
                }

            }
        )

        ModelButton(
            text = stringResource(id = R.string.backButton),
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(360.dp),
            true,
            onClickButton = {
               navToHome()
            }
        )
    }
}


