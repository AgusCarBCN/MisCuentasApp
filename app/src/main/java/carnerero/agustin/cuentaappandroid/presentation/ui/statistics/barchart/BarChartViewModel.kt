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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    //private val _effect = MutableSharedFlow<SearchEffects>()
    //val effect = _effect.asSharedFlow()

    // MutableStateFlow para la lista de entradas
    //private val _barChartData = MutableLiveData<MutableList<BarChartData>>(mutableListOf())
    //val barChartData: LiveData<MutableList<BarChartData>> = _barChartData

    //private val _showYearPicker = MutableLiveData<Boolean>()

    //private val _selectedYear = MutableLiveData<String>("2024")
    //val selectedYear: LiveData<String> = _selectedYear

    init {
        observeInitialData()
    }

    private fun observeInitialData() {
        viewModelScope.launch {
            val currencyCode = getCurrencyCode.invoke()
            val isDarkTheme=getSwitchDarkTheme.invoke()
            getAccounts()
                .collect { accounts ->
                    _uiState.update { current ->
                        current.copy(
                            accounts = accounts,
                            currencyCode = currencyCode,
                            isDarkTheme=isDarkTheme
                        )
                    }
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
    /*fun onSelectedYear(year: String) {
        _selectedYear.value = year
    }*/

    fun barChartDataByMonth(accountId: Int,year:String) {
        viewModelScope.launch(Dispatchers.IO) {

            val data: MutableList<BarChartData> = mutableListOf()

            listOfMonths.forEachIndexed { index, month ->
                // Asegúrate de que el mes esté en el formato correcto
                val monthValue:String = if(index<9){
                    "0${(index+1)}"
                }else{
                    "${index+1}"
                }
                try {
                    // Realiza llamadas asíncronas para ingresos y gastos
                    val incomeAmountDeferred = async { sumIncomesByMonth.invoke(accountId, monthValue,year) }
                    val expenseAmountDeferred = async { sumExpensesByMonth.invoke(accountId, monthValue,year) }

                    // Espera los resultados
                    val incomeAmount = incomeAmountDeferred.await().toFloat()
                    val expenseAmount = expenseAmountDeferred.await().toFloat()
                    val resultAmount = incomeAmount + expenseAmount
                    // Agrega los datos a la lista
                    data.add(
                        BarChartData(month, incomeAmount, expenseAmount,
                        resultAmount)
                    )
                     } catch (e: Exception) {
                    Log.e("ViewModel", "Error al obtener entradas para la cuenta $accountId para el mes $month: ${e.message}")
                }
            }
            _uiState.update { current ->
                current.copy(
                    barchartData = data
                )
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
