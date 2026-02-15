package carnerero.agustin.cuentaappandroid.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetAllEntriesByAccountUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetAllExpensesUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetAllIncomesUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetSumTotalExpensesUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetSumTotalIncomesUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAccounts: GetAllAccountsUseCase,
    private val getTotalIncomes: GetSumTotalIncomesUseCase,
    private val getTotalExpenses: GetSumTotalExpensesUseCase,
    private val getAllIncomes: GetAllIncomesUseCase,
    private val getAllExpenses: GetAllExpensesUseCase,
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val getAllRecordsByAccount: GetAllEntriesByAccountUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _effect = MutableSharedFlow<HomeEffects>()
    val effect = _effect.asSharedFlow()

    init {
        loadInitialData()
    }

    fun onEvent(event: HomeUiEvents) {
        when (event) {
            is HomeUiEvents.ShowExpenses -> {
                getAllExpensesDTO()

            }
            is HomeUiEvents.ShowIncomes ->{
                getAllIncomesDTO()

            }
            is HomeUiEvents.ShowAllRecordsByAccount ->
            {
                getAllRecordsByAccountDTO(event.accountId)
            }
            is HomeUiEvents.ShowEnableByDate->{
                switchEnableByDate(event.value)
            }

            is HomeUiEvents.SHowEntries ->{
                switchShowEntries(event.value)
            }
            else ->{

            }
        }
    }

    private fun switchEnableByDate(value:Boolean){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    enableByDate = value
                )
            }
        }
    }
    private fun loadInitialData() {

        viewModelScope.launch {
            val currencyCode = getCurrencyCode.invoke()
            val accounts = getAccounts.invoke()
            val totalIncomes = getTotalIncomes.invoke()
            val totalExpenses = getTotalExpenses.invoke()
            _uiState.update {
                it.copy(
                    currencyCode = currencyCode,
                    accounts = accounts,
                    totalIncomes = totalIncomes,
                    totalExpenses = totalExpenses
                )
            }
        }
    }

    private fun getAllExpensesDTO() {
        viewModelScope.launch {
            val entries = getAllExpenses.invoke()
            if (entries.isEmpty()) {
                _effect.emit(HomeEffects.ShowNoEntriesMessage)
            } else {
                _uiState.update {
                    it.copy(
                        showEntries = true,
                        listOfRecords = entries
                    )
                }
                _effect.emit(HomeEffects.NavToShowRecordsScreen)
            }
        }
    }
    private fun getAllIncomesDTO() {
        viewModelScope.launch {
            val entries = getAllIncomes.invoke()
            if (entries.isEmpty()) {
                _effect.emit(HomeEffects.ShowNoEntriesMessage)
            } else {
                _uiState.update {
                    it.copy(
                        showEntries = true,
                        listOfRecords = entries
                    )
                }
                _effect.emit(HomeEffects.NavToShowRecordsScreen)
            }
        }

    }

    // Método para obtener todas las entradas por cuenta
    private fun getAllRecordsByAccountDTO(accountId: Int) {
        viewModelScope.launch {
            val entries = getAllRecordsByAccount.invoke(accountId)
            if(entries.isEmpty()){
                _effect.emit(HomeEffects.ShowNoEntriesMessage)
            }else {
                _uiState.update {
                    it.copy(
                        showEntries = true,
                        listOfRecords = entries
                    )
                }
                _effect.emit(HomeEffects.NavToShowRecordsScreen)
            }
        }
    }
    fun switchShowEntries(value:Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    showEntries = value
                )
            }
        }
    }
}


