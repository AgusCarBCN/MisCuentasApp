package carnerero.agustin.cuentaappandroid.presentation.ui.records

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.presentation.common.model.RecordsFilter
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.RecordsList
import carnerero.agustin.cuentaappandroid.presentation.ui.login.LoginScreen
import com.google.gson.Gson

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

