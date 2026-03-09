package carnerero.agustin.cuentaappandroid.presentation.ui.records.get

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.components.DeleteRecords
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsFilter
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.components.GetRecords
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.components.ModifyRecords
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsMode
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import com.google.gson.Gson

@Composable
fun RecordScreen(
    navController: NavController,
    recordsViewModel: RecordsViewModel,
    filter: RecordsFilter,
    mode: RecordsMode)
    {
    val messageDeleteEntries = stringResource(id = R.string.deleteentries)

    LaunchedEffect(filter) {
        recordsViewModel.loadInitialData(filter)

    }
        LaunchedEffect(Unit) {

            recordsViewModel.effect.collect { effect ->

                when(effect) {

                    is RecordsUiEffects.NavigateToEdit ->
                        {
                            val entryJson =
                                Uri.encode(Gson().toJson(effect.record))
                            navController.navigate(
                                Routes.ModifyRecordItem.createRoute(entryJson)
                            )
                        }

                    RecordsUiEffects.MessageDeleteRecords -> {
                        SnackBarController.sendEvent(SnackBarEvent(messageDeleteEntries))
                    }
                }
            }
        }

        Log.d("MODES","$mode")

    val state by recordsViewModel.uiState.collectAsStateWithLifecycle()

    when(mode){
        RecordsMode.GET -> {
            GetRecords(state.listOfRecords,
                state.currencyCode,
                state.enableByDate
            ) { recordsViewModel.onEvent(RecordsUiEvents.ShowEnableByDate(it)) }
        }
        RecordsMode.DELETE -> {
            DeleteRecords(state.listOfRecords,
                state.currencyCode,
                state.showInfoDeleteDialog,
                {recordsViewModel.onEvent(RecordsUiEvents.OpenDialogDelete)},
                {recordsViewModel.onEvent(RecordsUiEvents.CloseDialogDelete)}
            ) { recordsViewModel.deleteRecord(it) }
        }
        RecordsMode.MODIFY -> {
            ModifyRecords(state.listOfRecords,
                state.currencyCode,
            ) {
                recordsViewModel.onEvent(
                    RecordsUiEvents.OnEditRecord(it)
                )
            }
        }
    }
}

