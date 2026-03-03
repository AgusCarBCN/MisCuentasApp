package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.modify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.IconAnimated
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting

@Composable
fun ModifyAccountDetailScreen (
    accountsViewModel: AccountsViewModel,
    accountId: Int?,
    navToHome:()->Unit

) {
    val scope = rememberCoroutineScope()
    val nameButtonChange by accountsViewModel.isEnableChangeNameButton.observeAsState(false)
    val balanceButtonChange by accountsViewModel.isEnableChangeBalanceButton.observeAsState(false)
    //al accountSelected by accountsViewModel.accountSelected.observeAsState()
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
            colors.imageTutorialInit,
            colors.imageTutorialTarget
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
            /*    try {
                    scope.launch(Dispatchers.IO) {
                        accountsViewModel.upDateAccountName(accountId!!, name)
                        SnackBarController.sendEvent(event = SnackBarEvent(nameChanged))
                    }
                } catch (e: Exception) {
                    Log.d("Cuenta", "Error: ${e.message}")
                    println("Error al cargar ${e.message}")
                }*/
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
               /* try {
                    scope.launch(Dispatchers.IO) {
                        val newBalance = balance.toBigDecimalOrNull() ?: BigDecimal.ZERO
                        accountsViewModel.upDateAccountBalance(accountId!!, newBalance)
                        SnackBarController.sendEvent(event = SnackBarEvent(balanceChanged))

                    }
                } catch (e: Exception) {
                    Log.d("Cuenta", "Error: ${e.message}")
                    println("Error al cargar ${e.message}")
                }
*/
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