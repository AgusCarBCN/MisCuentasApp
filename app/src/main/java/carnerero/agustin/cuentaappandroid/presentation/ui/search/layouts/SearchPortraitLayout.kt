package carnerero.agustin.cuentaappandroid.presentation.ui.search.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.search.TypeOfSearch
import carnerero.agustin.cuentaappandroid.presentation.ui.search.components.SearchAmountFields
import carnerero.agustin.cuentaappandroid.presentation.ui.search.components.SearchButton
import carnerero.agustin.cuentaappandroid.presentation.ui.search.components.SearchPrimaryFields
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import kotlinx.coroutines.CoroutineScope
import java.util.Date

@Composable
fun SearchPortraitLayout(
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchPrimaryFields(
            accountViewModel,
            searchViewModel,
            entryDescription,
            fieldModifier
        )

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
