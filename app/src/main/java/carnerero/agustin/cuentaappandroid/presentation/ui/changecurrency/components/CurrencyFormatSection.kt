package carnerero.agustin.cuentaappandroid.presentation.ui.changecurrency.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.CurrencySelector
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CurrencyFormatSection(
    accountsViewModel: AccountsViewModel,
    scope: CoroutineScope,
    currencyCodeShowed: String,
    messageFormat: String
) {
    val fieldModifier = Modifier
        .fillMaxWidth(0.85f)
        .heightIn(min = 48.dp)

    HeadSetting(
        title = stringResource(R.string.changecurrency),
        MaterialTheme.typography.headlineMedium
    )

    CurrencySelector(accountsViewModel)

    HeadSetting(
        title = stringResource(R.string.changeformattext),
        MaterialTheme.typography.headlineSmall
    )

    ModelButton(
        text = stringResource(R.string.changeFormat),
        MaterialTheme.typography.labelLarge,
        modifier = fieldModifier,
        true
    ) {
        scope.launch {
            accountsViewModel.setCurrencyCode(currencyCodeShowed)
            SnackBarController.sendEvent(
                SnackBarEvent(messageFormat)
            )
        }
    }
}
