package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.CurrencySelector
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.CurrencySelectorV2
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.CreateAccountViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountEvent
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountUiState

@Composable
 fun CurrencyAndActions(
    onDropdownExpandedChange:(Boolean)->Unit,
    onCurrencySelectChange:(String)->Unit,
    isDropdownExpanded:Boolean,
    selectedCurrency:String,
    confirm: () -> Unit,
    navToBack: () -> Unit
) {

        CurrencySelectorV2(onDropdownExpandedChange,
            onCurrencySelectChange,
            isDropdownExpanded,
            selectedCurrency,
            )

            ModelButton(
                text = stringResource(R.string.confirmButton),
                MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth(0.85f),
                true
            ) { confirm() }

            ModelButton(
                text = stringResource(R.string.backButton),
                MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth(0.85f),
                true
            ) { navToBack()}
        }


