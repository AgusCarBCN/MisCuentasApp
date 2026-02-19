package carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords

import android.util.Log
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import carnerero.agustin.cuentaappandroid.presentation.ui.records.components.RecordsFilter
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.utils.navigateTopLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun RecordSearchScreen(
    searchViewModel: SearchViewModelV2,
    navController: NavController
) {
    val state by searchViewModel.uiState.collectAsStateWithLifecycle()
    val effects by searchViewModel.effect.collectAsStateWithLifecycle(initialValue = SearchEffects.Idle)
    LaunchedEffect(effects) {
        when(effects){
            SearchEffects.NavToRecordsScreen -> {
                navController.navigate(state.route)
                Log.d("NAVIGATION", "Route from Search:${state.route}")
            }
            else ->{

            }
        }
    }

        ModelButton(
            text = stringResource(R.string.search),
            MaterialTheme.typography.labelLarge,
            Modifier.width(200.dp),
            true,
            {searchViewModel.onEvent(SearchUiEvent.ConfirmSearch)}
        )

    }


