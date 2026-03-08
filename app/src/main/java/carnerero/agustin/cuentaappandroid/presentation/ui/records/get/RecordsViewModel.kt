package carnerero.agustin.cuentaappandroid.presentation.ui.records.get

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.DeleteRecordAndUpdateBalanceUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.DepositUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.WithDrawUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GeAllEntriesUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetAllEntriesByAccountUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetAllExpensesUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetAllIncomesUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetFilteredEntriesUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.RecordsUiEffects.*
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
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
    private val getFilteredEntries: GetFilteredEntriesUseCase,
    private val deleteRecord: DeleteRecordAndUpdateBalanceUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(RecordsUiState())
    val uiState: StateFlow<RecordsUiState> = _uiState

    private val _effect = MutableSharedFlow<RecordsUiEffects>()
    val effect = _effect.asSharedFlow()


    fun onEvent(event: RecordsUiEvents) {
        when (event) {
            is RecordsUiEvents.ShowEnableByDate -> switchEnableByDate(event.value)

            is RecordsUiEvents.OnDeleteRecord -> deleteRecord(event.record)
            is RecordsUiEvents.OnEditRecord -> {
                viewModelScope.launch {
                    _effect.emit(
                        NavigateToEdit(event.record)
                    )
                }
            }
            RecordsUiEvents.CloseDialogDelete -> {
                _uiState.update { current->
                    current.copy(
                        showInfoDeleteDialog = false
                    )
                }
            }
            RecordsUiEvents.OpenDialogDelete -> {
                _uiState.update { current->
                    current.copy(
                        showInfoDeleteDialog = true
                    )
                }
            }
            else->{
            }
        }
    }


    // Cambiar vista ByDate / ByCategory
    fun switchEnableByDate(value: Boolean) {
        _uiState.update { it.copy(enableByDate = value) }
    }
    fun deleteRecord(record: RecordDTO) {
        viewModelScope.launch {
             deleteRecord.invoke(record)
             delay(1000)
             _effect.emit(RecordsUiEffects.MessageDeleteRecords)
        }
    }

    fun loadInitialData(filter: RecordsFilter) {

        val recordsFlow: Flow<List<RecordDTO>> = when (filter) {
            RecordsFilter.Expenses -> getAllExpenses.invoke()
            RecordsFilter.Incomes -> getAllIncomes.invoke()
            is RecordsFilter.RecordsByAccount -> getAllRecordsByAccount.invoke(filter.accountId)
            is RecordsFilter.Search -> getFilteredEntries.invoke(filter.searchFilter)
            RecordsFilter.All -> getAllEntriesDTO.invoke()
        }

        viewModelScope.launch {

            combine(
                recordsFlow,
                getCurrencyCode.invoke()
            ) { records, currencyCode ->

                records to currencyCode

            }.collect { (records, currencyCode) ->

                _uiState.update {
                    it.copy(
                        listOfRecords = records,
                        currencyCode = currencyCode
                    )
                }

            }
        }
    }

}