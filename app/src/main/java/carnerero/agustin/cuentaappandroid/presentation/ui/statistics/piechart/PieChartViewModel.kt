package carnerero.agustin.cuentaappandroid.presentation.ui.statistics.piechart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetAllEntriesByDateUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField
import carnerero.agustin.cuentaappandroid.presentation.ui.statistics.piechart.model.PieChartUiEvents
import carnerero.agustin.cuentaappandroid.presentation.ui.statistics.piechart.model.PieChartUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PieChartViewModel @Inject constructor(
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val getAccounts: GetAllAccountsUseCase,
    private val getAllEntriesByDate: GetAllEntriesByDateUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow(PieChartUiState())
    val uiState: StateFlow<PieChartUiState> = _uiState

    private val _effect = MutableSharedFlow<PieChartEffects>()
    val effect = _effect.asSharedFlow()

    init {
        observeInitialData()
    }
    private fun observeInitialData() {
        viewModelScope.launch {
            combine(
                getAccounts(),
                getCurrencyCode()
            ) { accounts, currencyCode ->
                _uiState.value.copy(
                    accounts = accounts,
                    currencyCode = currencyCode
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun getPieChartData(accountId:Int,fromDate:String,toDate:String){
        viewModelScope.launch {
           val data=getAllEntriesByDate.getEntriesByDate(accountId,fromDate,toDate)
           if(data.isEmpty()){
               _effect.emit(PieChartEffects.MessageNoRecords)
           }
            _uiState.update { current->
               current.copy(
                   records = data
               )
           }
        }
    }
    fun onEventUser(event: PieChartUiEvents){
        when(event){
            is PieChartUiEvents.OnAccountChange -> onAccountSelected(event.accountId)
            is PieChartUiEvents.OnSelectDate -> onSelectedDate(event.field,event.date)
            is PieChartUiEvents.OnShowDatePicker -> onShowDatePicker(event.field,event.visible)
        }
    }
    fun onAccountSelected(accountId: Int) {
        _uiState.update { current ->
            current.copy(
                accountSelected = accountId
            )
        }
    }
    fun onShowDatePicker(field: DateField, visible: Boolean) {
        _uiState.update { current ->
            when (field) {
                DateField.FROM -> current.copy(showDatePickerFrom = visible)
                DateField.TO -> current.copy(showDatePickerTo = visible)
            }
        }
    }


    fun onSelectedDate(field: DateField, date: String) {
        _uiState.update { current ->
           when(field){
               DateField.FROM -> current.copy(fromDate = date)
               DateField.TO -> current.copy(toDate = date)
           }
        }
    }
}