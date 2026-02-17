package carnerero.agustin.cuentaappandroid.presentation.ui.records.get

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.presentation.ui.records.RecordsViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.records.components.RecordsFilter
import carnerero.agustin.cuentaappandroid.presentation.ui.records.components.RecordsList

@Composable
fun RecordScreen(
    recordsViewModel: RecordsViewModel,
    filter: RecordsFilter)
    {
    LaunchedEffect(filter) {
        recordsViewModel.getRecords(filter)
        Log.d("NAVIGATION", "Filter:${filter}")
    }
    val state by recordsViewModel.uiState.collectAsStateWithLifecycle()
    recordsViewModel.getRecords(filter)

    RecordsList(state.listOfRecords,
        state.currencyCode,
        state.enableByDate,
        {recordsViewModel.switchEnableByDate(it)})

}

