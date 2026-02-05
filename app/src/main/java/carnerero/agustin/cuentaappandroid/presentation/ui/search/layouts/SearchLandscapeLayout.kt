package carnerero.agustin.cuentaappandroid.presentation.ui.search.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.search.TypeOfSearch
import carnerero.agustin.cuentaappandroid.presentation.ui.search.components.SearchAmountFields
import carnerero.agustin.cuentaappandroid.presentation.ui.search.components.SearchButton
import carnerero.agustin.cuentaappandroid.presentation.ui.search.components.SearchPrimaryFields
import kotlinx.coroutines.CoroutineScope

@Composable
fun SearchLandscapeLayout(
    accountViewModel: AccountsViewModel,
    searchViewModel: SearchViewModel,
    fromAmount: String,
    toAmount: String,
    entryDescription: String,
    enableSearchButton: Boolean,
    scope: CoroutineScope,
    messageAmountError: String,
    messageDateError: String,
    typeOfSearch: TypeOfSearch,
    navController: NavController
) {
    val fieldModifier = Modifier
        .fillMaxWidth(0.85f)
        .heightIn(min = 48.dp)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // IZQUIERDA → CRITERIOS
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchPrimaryFields(
                accountViewModel,
                searchViewModel,
                entryDescription,
                fieldModifier
            )
        }

        Spacer(Modifier.width(32.dp))

        // DERECHA → IMPORTES + ACCIÓN
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SearchAmountFields(
                searchViewModel,
                fromAmount,
                toAmount,
                fieldModifier
            )

            SearchButton(
                enableSearchButton,
                fieldModifier,
                scope,
                searchViewModel,
                fromAmount,
                toAmount,
                messageAmountError,
                messageDateError,
                typeOfSearch,
                navController
            )
        }
    }
}
