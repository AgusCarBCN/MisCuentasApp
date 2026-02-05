package carnerero.agustin.cuentaappandroid.presentation.ui.search.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import carnerero.agustin.cuentaappandroid.presentation.ui.search.TypeOfSearch
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.utils.navigateTopLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SearchButton(
    enableSearchButton: Boolean,
    modifier: Modifier,
    scope: CoroutineScope,
    searchViewModel: SearchViewModel,
    fromAmount: String,
    toAmount: String,
    messageAmountError: String,
    messageDateError: String,
    typeOfSearch: TypeOfSearch,
    navController: NavController
) {
    ModelButton(
        text = stringResource(R.string.search),
        MaterialTheme.typography.labelLarge,
        modifier,
        enableSearchButton
    ) {
        when {
            !searchViewModel.validateAmounts(fromAmount, toAmount) -> {
                scope.launch {
                    SnackBarController.sendEvent(
                        SnackBarEvent(messageAmountError)
                    )
                }
            }

            !searchViewModel.validateDates() -> {
                scope.launch {
                    SnackBarController.sendEvent(
                        SnackBarEvent(messageDateError)
                    )
                }
            }

            else -> {
                when (typeOfSearch) {
                    TypeOfSearch.SEARCH ->
                        navController.navigateTopLevel(Routes.Records.route)

                    TypeOfSearch.DELETE ->
                        navController.navigateTopLevel(Routes.RecordsToDelete.route)

                    TypeOfSearch.UPDATE ->
                        navController.navigateTopLevel(Routes.RecordsToModify.route)
                }
            }
        }
    }
}
