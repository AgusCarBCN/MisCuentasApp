package carnerero.agustin.cuentaappandroid.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetSumTotalExpensesUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetSumTotalIncomesUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsFilter
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsFilter.*
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAccounts: GetAllAccountsUseCase,
    private val getTotalIncomes: GetSumTotalIncomesUseCase,
    private val getTotalExpenses: GetSumTotalExpensesUseCase,
    private val getCurrencyCode: GetCurrencyCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _effect = MutableSharedFlow<HomeEffects>()
    val effect = _effect.asSharedFlow()

    init {

        observeInitialData()
    }

    fun onEvent(event: HomeUiEvents) {
        when (event) {
            is HomeUiEvents.ShowExpenses -> {
                onFilterChange(Expenses)
            }
            is HomeUiEvents.ShowIncomes -> {
                onFilterChange(Incomes)
            }
            is HomeUiEvents.ShowAllRecordsByAccount -> {
                onFilterChange(RecordsByAccount(event.accountId))
            }
            else -> {

            }
        }
    }
    private fun onFilterChange(filter: RecordsFilter) {
        _uiState.update { it.copy(filter = filter) }
        navigateToRecords(filter)
    }


    private fun observeInitialData() {
        viewModelScope.launch {
            combine(
                getCurrencyCode.invoke(),
                getAccounts.invoke(),
                getTotalIncomes.invoke().map { it ?: BigDecimal.ZERO },
                getTotalExpenses.invoke().map { it ?: BigDecimal.ZERO }
            ) { currencyCode,accounts, totalIncomes, totalExpenses ->
                _uiState.value.copy(
                    accounts = accounts,
                    totalIncomes = totalIncomes,
                    totalExpenses = totalExpenses,
                    currencyCode = currencyCode
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
    private fun navigateToRecords(filter: RecordsFilter) {
        val route = when (filter) {
            Expenses ->
                Routes.GetRecords.createRoute(Expenses)

            Incomes ->
                Routes.GetRecords.createRoute(Incomes)

            is RecordsByAccount ->
                Routes.GetRecords.createRoute(filter)

            else-> ""
        }
        viewModelScope.launch {
            _uiState.update { it.copy(route = route) }
            _effect.emit(HomeEffects.NavToRecordsScreen)
        }
    }
}


