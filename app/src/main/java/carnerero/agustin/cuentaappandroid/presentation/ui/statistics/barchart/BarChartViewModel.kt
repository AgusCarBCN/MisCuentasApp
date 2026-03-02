package carnerero.agustin.cuentaappandroid.presentation.ui.statistics.barchart


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetSumExpensesByMonthUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetSumIncomesByMonthUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetEnableDarkThemUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.SearchEffects
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.SearchUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.statistics.barchart.model.BarChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
class BarChartViewModel @Inject constructor(
    private val sumIncomesByMonth: GetSumIncomesByMonthUseCase,
    private val sumExpensesByMonth: GetSumExpensesByMonthUseCase,
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val getAccounts: GetAllAccountsUseCase,
    private val getSwitchDarkTheme: GetEnableDarkThemUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BarChartUiState())
    val uiState: StateFlow<BarChartUiState> = _uiState

    init {
        observeInitialData()
    }

    private fun observeInitialData() {
        viewModelScope.launch {
            val currencyCode = getCurrencyCode.invoke()
            combine(
                getCurrencyCode.invoke(),
                getAccounts.invoke(),
                getSwitchDarkTheme.invoke()

            ) {currencyCode, accounts, switchDarkTheme ->
                _uiState.value.copy(
                    accounts = accounts,
                    isDarkTheme = switchDarkTheme,
                    currencyCode = currencyCode
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun onUserEvent (event : BarChartUiEvent){
        when(event){
            is BarChartUiEvent.OnAccountSelected -> onAccountSelected(event.accountId)
            is BarChartUiEvent.OnYearSelected -> onSelectedYear(event.year)
        }
    }

    fun onSelectedYear(year: String){
        _uiState.update { current ->
            current.copy(
                yearSelected = year
            )
        }
    }
    fun onAccountSelected(accountId:Int){
        _uiState.update { current ->
            current.copy(
                accountSelected = accountId
            )
        }
    }


    fun barChartDataByMonth(accountId: Int, year: String) {
        viewModelScope.launch {
            // Creamos una lista de Flows para cada mes
            val monthlyFlows = listOfMonths.mapIndexed { index, month ->
                val monthValue = if (index < 9) "0${index + 1}" else "${index + 1}"

                // Combinamos los Flows de ingreso y gasto por mes
                combine(
                    sumIncomesByMonth.invoke(accountId, monthValue, year)
                        .map { it ?: BigDecimal.ZERO },
                    sumExpensesByMonth.invoke(accountId, monthValue, year)
                        .map { it ?: BigDecimal.ZERO }
                ) { income, expense ->
                    BarChartData(
                        month = month,
                        incomes = income.toFloat(),
                        expenses = expense.toFloat(),
                        result = income.add(expense).toFloat()
                    )
                }
            }

            // Combinamos todos los meses en una lista completa
            combine(monthlyFlows) { arrayOfBarChartData ->
                arrayOfBarChartData.toList()
            }.collect { data ->
                _uiState.update { current ->
                    current.copy(
                        barchartData = data
                    )
                }
            }
        }
    }


    private val listOfMonths = listOf(
        R.string.january,
        R.string.february,
        R.string.march,
        R.string.april,
        R.string.may,
        R.string.june,
        R.string.july,
        R.string.august,
        R.string.september,
        R.string.october,
        R.string.november,
        R.string.december
    )



}
