package carnerero.agustin.cuentaappandroid.presentation.ui.records.get

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsFilter
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsList

@Composable
fun RecordScreen(
    recordsViewModel: GetRecordsViewModel,
    filter: RecordsFilter)
    {
    LaunchedEffect(filter) {
        recordsViewModel.getRecords(filter)
        Log.d("NAVIGATION", "Filter:${filter.routeName}")
        Log.d("NAVIGATION", "Search:${filter}")

    }
    val state by recordsViewModel.uiState.collectAsStateWithLifecycle()
    recordsViewModel.getRecords(filter)

    RecordsList(state.listOfRecords,
        state.currencyCode,
        state.enableByDate,
        {recordsViewModel.onEvent(GetRecordsUiEvents.ShowEnableByDate(it))})
}

