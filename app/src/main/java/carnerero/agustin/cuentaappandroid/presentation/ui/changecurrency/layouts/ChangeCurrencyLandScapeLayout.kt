package carnerero.agustin.cuentaappandroid.presentation.ui.changecurrency.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
 fun ChangeCurrencyLandScapeLayout(
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
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimens.extraLarge)
    ) {
        // IZQUIERDA
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CurrencyFormatSection(
                accountsViewModel,
                scope,
                currencyCodeShowed,
                messageFormat
            )
        }

        Spacer(modifier = Modifier.width(32.dp))

        // DERECHA
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
}
