package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.CurrencySelector
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel

@Composable
public fun CurrencyAndActions(
    viewModel: AccountsViewModel,
    enableCurrencySelector: Boolean,
    isCurrencyExpanded: Boolean,
    currencyShowedCode: String,
    navToLogin: () -> Unit,
    navToBack: () -> Unit
) {
    if (enableCurrencySelector) {
        CurrencySelector(viewModel)

        if (!isCurrencyExpanded) {
            ModelButton(
                text = stringResource(R.string.confirmButton),
                MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth(0.85f),
                true
            ) {
                viewModel.setCurrencyCode(currencyShowedCode)
                navToLogin()
            }

            ModelButton(
                text = stringResource(R.string.backButton),
                MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth(0.85f),
                true
            ) {
                viewModel.resetFields()
                navToBack()
            }
        }
    }
}
