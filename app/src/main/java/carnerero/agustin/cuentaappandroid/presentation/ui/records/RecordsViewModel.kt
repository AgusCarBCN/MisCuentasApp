package carnerero.agustin.cuentaappandroid.presentation.ui.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GeAllEntriesUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetAllEntriesByAccountUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetAllExpensesUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetAllIncomesUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetFilteredEntriesUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.home.HomeUiEvents
import carnerero.agustin.cuentaappandroid.presentation.ui.records.components.RecordsFilter
import carnerero.agustin.cuentaappandroid.presentation.ui.records.components.RecordsFilter.Expenses
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.RecordsEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.RecordsUiEvents
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.RecordsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val getAllEntriesDTO: GeAllEntriesUseCase,
    private val getAllIncomes: GetAllIncomesUseCase,
    private val getAllExpenses: GetAllExpensesUseCase,
    private val getAllRecordsByAccount: GetAllEntriesByAccountUseCase,
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val getFilteredEntries: GetFilteredEntriesUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(RecordsUiState())
    val uiState: StateFlow<RecordsUiState> = _uiState

    private val _effect = MutableSharedFlow<RecordsEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadCurrencyCode()
    }
    fun onEvent(event: RecordsUiEvents) {
        when (event) {
            is RecordsUiEvents.ShowEnableByDate -> switchEnableByDate(event.value)
            else ->{}
        }
    }
    // Carga los registros según el filtro
    fun getRecords(filter: RecordsFilter) {
        val recordsFlow: Flow<List<EntryDTO>> = when (filter) {
            Expenses -> getAllExpenses.invoke()
            RecordsFilter.Incomes -> getAllIncomes.invoke()
            is RecordsFilter.RecordsByAccount -> getAllRecordsByAccount.invoke(filter.accountId)
            is RecordsFilter.Search -> getFilteredEntries.invoke(filter.searchFilter)
            RecordsFilter.All -> getAllEntriesDTO.invoke()
        }

        viewModelScope.launch {
            recordsFlow.collect { list ->
                _uiState.update { it.copy(listOfRecords = list) }
                // Emitimos efecto si quieres notificar que hay registros
                if (list.isNotEmpty()) _effect.emit(RecordsEffect.ShowRecords)
            }
        }
    }

    // Cambiar vista ByDate / ByCategory
    fun switchEnableByDate(value: Boolean) {
        _uiState.update { it.copy(enableByDate = value) }
    }

    // Cargar currencyCode al iniciar
    private fun loadCurrencyCode() {
        viewModelScope.launch {
            val currencyCode = getCurrencyCode.invoke()
            _uiState.update { it.copy(currencyCode = currencyCode) }
        }
    }
}
