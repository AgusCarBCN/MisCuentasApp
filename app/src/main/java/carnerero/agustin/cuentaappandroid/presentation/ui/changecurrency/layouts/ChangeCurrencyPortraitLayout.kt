package carnerero.agustin.cuentaappandroid.presentation.ui.changecurrency.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BackButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.ui.changecurrency.components.CurrencyChangeSection
import carnerero.agustin.cuentaappandroid.presentation.ui.changecurrency.components.CurrencyFormatSection
import kotlinx.coroutines.CoroutineScope

@Composable
 fun ChangeCurrencyPortraitLayout(
    accountsViewModel: AccountsViewModel,
    entriesViewModel: EntriesViewModel,
    scope: CoroutineScope,
    currencyCodeShowed: String,
    currencyCodeSelected: String,
    messageFormat: String,
    messageChange: String,
    messageError: String,
    navToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimens.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CurrencyFormatSection(
            accountsViewModel,
            scope,
            currencyCodeShowed,
            messageFormat
        )

        Spacer(Modifier.height(24.dp))

        CurrencyChangeSection(
            accountsViewModel,
            entriesViewModel,
            scope,
            currencyCodeShowed,
            currencyCodeSelected,
            messageChange,
            messageError
        )

        Spacer(Modifier.height(24.dp))

        BackButton {
            accountsViewModel.onCurrencyShowedChange(currencyCodeSelected)
            navToHome()
        }
    }
}
