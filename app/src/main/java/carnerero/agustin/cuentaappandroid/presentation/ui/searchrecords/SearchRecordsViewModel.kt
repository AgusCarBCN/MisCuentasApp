package carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsFilter
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.SearchFilter
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.TransactionType
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.TypeOfSearch
import carnerero.agustin.cuentaappandroid.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import javax.inject.Inject

@HiltViewModel
class SearchRecordsViewModel @Inject constructor(
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val getAccounts: GetAllAccountsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private val _effect = MutableSharedFlow<SearchEffects>()
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


    fun onEvent(event: SearchUiEvent) {
        when (event) {
           is SearchUiEvent.ConfirmSearch -> {
                navigateToRecords(event.type)
                resetFields()
            }

            is SearchUiEvent.OnAccountSelect -> {
                onAccountSelected(event.accountId)
            }

            is SearchUiEvent.OnAmountsChanges -> {
                onAmountsFieldsChange(event.amountFrom, event.amountTo)
            }

            is SearchUiEvent.OnRecordDescriptionChange -> {
                onEntryDescriptionChanged(event.newDescription)
            }

            is SearchUiEvent.OnSelectDate -> {
                onSelectedDate(event.field, event.date)
            }

            is SearchUiEvent.OnShowDatePicker -> {
                onShowDatePicker(event.field, event.visible)
            }

            is SearchUiEvent.OnTransactionSelect -> {
                onOptionSelected(event.type)
            }
        }
    }

    fun onAccountSelected(accountId: Int) {
        _uiState.update { current ->
            current.copy(
                searchFilter = current.searchFilter.copy(
                    accountId = accountId
                )
            )
        }
    }

    fun onOptionSelected(option: TransactionType) {
        _uiState.update { current ->
            current.copy(
                searchFilter = current.searchFilter.copy(
                    selectedOption = option
                )
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
            val updatedSearchFilter = (current.searchFilter).copy(
                dateFrom = if (field == DateField.FROM) date else current.searchFilter.dateFrom,
                dateTo = if (field == DateField.TO) date else current.searchFilter.dateTo
            )
            current.copy(searchFilter = updatedSearchFilter)
        }
    }

    fun onAmountsFieldsChange(fromAmount: String, toAmount: String) {
        _uiState.update { current ->
            val min = fromAmount.toBigDecimalOrNull() ?: current.searchFilter.amountMin

            val max = toAmount.toBigDecimalOrNull() ?: current.searchFilter.amountMax


            current.copy(
                searchFilter = current.searchFilter.copy(
                    amountMin = min,
                    amountMax = max
                )
            )
        }
    }

    fun onEntryDescriptionChanged(newDescription: String) {
        _uiState.update { current ->
            current.copy(
                searchFilter = current.searchFilter.copy(
                    description = newDescription
                )
            )
        }
    }

    fun resetFields() {
        _uiState.update { current ->
            current.copy(
                searchFilter = SearchFilter(),
                showDatePickerTo = false,
                showDatePickerFrom = false
                            )
        }
    }

    private fun navigateToRecords(type: TypeOfSearch) {
        val searchFilter = _uiState.value.searchFilter
        Log.d("Filter",searchFilter.toString())
        // Validación de montos
        if (searchFilter.amountMax < searchFilter.amountMin) {
            viewModelScope.launch {
                _effect.emit(SearchEffects.MessageInvalidAmounts)
            }
            return
        }

        // Validación de fechas
        val fromDate = Utils.convertStringToLocalDate(searchFilter.dateFrom)
        val toDate   = Utils.convertStringToLocalDate(searchFilter.dateTo)

        if (fromDate.isAfter(toDate)) {
            viewModelScope.launch {
                _effect.emit(SearchEffects.MessageInvalidDates)
            }
            return
        }

        // Construir filtro y ruta
        val recordsFilter = RecordsFilter.Search(searchFilter)
        //val route = Routes.GetRecords.createRoute(recordsFilter)
         val route = when(type){
             TypeOfSearch.GET ->Routes.GetRecords.createRoute(recordsFilter)
             TypeOfSearch.DELETE -> TODO()
             TypeOfSearch.MODIFY -> TODO()
         }
        // Actualizar estado y emitir efecto de navegación
        viewModelScope.launch {
            _uiState.update { it.copy(route =
                route) }
            _effect.emit(SearchEffects.NavToRecordsScreen)

        }
    }

}