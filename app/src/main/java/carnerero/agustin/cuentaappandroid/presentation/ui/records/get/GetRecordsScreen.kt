package carnerero.agustin.cuentaappandroid.presentation.ui.records.get

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.components.DeleteRecords
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsFilter
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.components.GetRecords
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsMode

@Composable
fun RecordScreen(
    recordsViewModel: GetRecordsViewModel,
    filter: RecordsFilter,
    mode: RecordsMode)
    {
    LaunchedEffect(filter) {
        recordsViewModel.getRecords(filter)

    }
        Log.d("MODES","$mode")

    val state by recordsViewModel.uiState.collectAsStateWithLifecycle()
    recordsViewModel.getRecords(filter)
    when(mode){
        RecordsMode.GET -> {
            GetRecords(state.listOfRecords,
                state.currencyCode,
                state.enableByDate,
                {recordsViewModel.onEvent(GetRecordsUiEvents.ShowEnableByDate(it))})

        }
        RecordsMode.DELETE -> {
            DeleteRecords(state.listOfRecords,state.currencyCode)
        }
        RecordsMode.MODIFY -> TODO()
    }

}

