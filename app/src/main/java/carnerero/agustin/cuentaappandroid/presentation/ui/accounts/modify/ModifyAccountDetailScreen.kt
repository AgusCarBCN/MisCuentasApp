package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.modify

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.IconAnimated
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent

@Composable
fun ModifyAccountDetailScreen (
    modifyAccountViewModel: ModifyAccountViewModel,
    accountId: Int?

) {
    val state by modifyAccountViewModel.uiState.collectAsStateWithLifecycle()
//SnackBarMessage
    Log.d("ACCOUNT","$accountId")
    val nameChanged = message(resource = R.string.namechanged)
    val balanceChanged = message(resource = R.string.amountchanged)
    LaunchedEffect(Unit) {
        modifyAccountViewModel.getInitValues(accountId?:1)
        modifyAccountViewModel.effect.collect { effect ->
            when (effect) {
                ModifyAccountsEffects.MessageBalanceChange ->
                    SnackBarController.sendEvent(SnackBarEvent(balanceChanged))
                ModifyAccountsEffects.MessageNameChange ->
                    SnackBarController.sendEvent(SnackBarEvent(nameChanged))
            }
        }
    }

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
            state.name,
            onTextChange = { modifyAccountViewModel.onEventUser(ModifyAccountUserEvent.OnChangeName(it)) },
            BoardType.TEXT,
            false
        )
        ModelButton(
            text = stringResource(id = R.string.change),
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(360.dp),
            state.enableChangeButton,
            onClickButton = {
                modifyAccountViewModel
                    .onEventUser(ModifyAccountUserEvent
                        .UpdateName(accountId?:1,state.name))
            }
        )


        TextFieldComponent(
            modifier = Modifier.width(360.dp),
            stringResource(id = R.string.enteramount),
            state.balance,
            onTextChange = {
                modifyAccountViewModel.onEventUser(ModifyAccountUserEvent.OnChangeBalance(it))

            },
            BoardType.DECIMAL,
            false
        )



        ModelButton(
            text = stringResource(id = R.string.change),
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(360.dp),
            state.enableChangeBalance,
            onClickButton = {
                modifyAccountViewModel
                    .onEventUser(ModifyAccountUserEvent
                        .UpdateBalance(accountId?:1,state.balance.toBigDecimal()))
            }
        )

       /* ModelButton(
            text = stringResource(id = R.string.backButton),
            MaterialTheme.typography.labelLarge,
            modifier = Modifier.width(360.dp),
            true,
            onClickButton = {
                navToHome()
            }
        )*/
    }
}