package carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import carnerero.agustin.cuentaappandroid.presentation.ui.records.components.RecordsFilter
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.SearchFilter
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.TransactionType
import carnerero.agustin.cuentaappandroid.utils.Utils
import com.google.gson.Gson
import com.google.type.Date
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class SearchViewModelV2 @Inject constructor(
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val getAccounts: GetAllAccountsUseCase
    ) : ViewModel()
{

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private val _effect = MutableSharedFlow<SearchEffects>()
    val effect = _effect.asSharedFlow()

    init {
        observeInitialData()
    }
    private fun observeInitialData() {
        viewModelScope.launch {
            val currencyCode = getCurrencyCode()
            getAccounts()
                .collect { accounts ->
                    _uiState.update { current ->
                        current.copy(
                            accounts = accounts,
                            currencyCode = currencyCode
                        )
                    }
                }
        }
    }

    fun onEvent(event: SearchUiEvent){
        when(event){
            SearchUiEvent.ConfirmSearch -> {
                navigateToRecords()
            }
            is SearchUiEvent.OnAccountSelect -> {
                onAccountSelected(event.accountId)
            }
            is SearchUiEvent.OnAmountChanges -> {
                onAmountsFieldsChange(event.amountFrom,event.amountTo)
            }
            is SearchUiEvent.OnRecordDescriptionChange -> {
                onEntryDescriptionChanged(event.newDescription)
            }
            is SearchUiEvent.OnSelectDate -> {
                onSelectedDate(event.date,event.isFromDate)
            }
            is SearchUiEvent.OnShowDatePicker -> {
                _uiState.update { current ->
                    current.copy(
                        showDatePickerFrom = if (event.isFromDate) event.show else current.showDatePickerFrom,
                        showDatePickerTo   = if (!event.isFromDate) event.show else current.showDatePickerTo
                    )
                }
            //onShowDatePicker(event.show,event.isFromDate)
            }
            is SearchUiEvent.OnTransactionSelect -> {
                onOptionSelected(event.type)
            }
        }
    }
    fun onAccountSelected(accountId:Int) {
        _uiState.update { current ->
            current.copy(
                searchFilter = current.searchFilter?.copy(
                    accountId=accountId
                )
            )
        }
    }

    fun onOptionSelected(option: TransactionType) {
        _uiState.update { current ->
            current.copy(
                searchFilter = current.searchFilter?.copy(
                    selectedOption = option
                )
            )
        }
    }


    fun onShowDatePicker(newValue: Boolean, isDateFrom: Boolean) {
        if (isDateFrom) {
            _uiState.update { current ->
                current.copy(showDatePickerFrom = newValue)
            }
        } else {
            _uiState.update { current ->
                current.copy(showDatePickerTo = newValue)
            }
        }
    }


    fun onSelectedDate(date: String, isDateFrom: Boolean) {
        if (isDateFrom) {
            _uiState.update { current ->
                current.copy(
                    searchFilter = current.searchFilter?.copy(
                        dateFrom = date
                    )
                )
            }
        } else {
            _uiState.update { current ->
                current.copy(
                    searchFilter = current.searchFilter?.copy(
                        dateTo = date
                    )
                )
            }
        }
    }

    fun onAmountsFieldsChange(fromAmount: String, toAmount: String) {
        // Validar y actualizar el valor de amount
        if (Utils.Companion.isValidDecimal(fromAmount)) {
            _uiState.update { current ->
                current.copy(
                    searchFilter = current.searchFilter?.copy(
                        amountMin = fromAmount.toBigDecimalOrNull()?: BigDecimal.ZERO
                    )
                )
            }
        }
        if (Utils.Companion.isValidDecimal(toAmount)) {
            _uiState.update { current ->
                current.copy(
                    searchFilter = current.searchFilter?.copy(
                        amountMin = toAmount.toBigDecimalOrNull()?: BigDecimal.ZERO
                    )
                )
            }
        }
    }
    fun onEntryDescriptionChanged(newDescription: String) {
        _uiState.update { current ->
            current.copy(
                searchFilter = current.searchFilter?.copy(
                    description = newDescription
                )
            )
        }
    }
    fun resetFields(){
        _uiState.update { current ->
            current.copy(searchFilter = null,
                showDatePickerTo = false,
                showDatePickerFrom = false)
        }
    }
    private fun navigateToRecords() {
        val searchFilter= SearchFilter(
           1,
            "Prueba"
        )

            val recordsFilter = RecordsFilter.Search(searchFilter)

            val route = Routes.SearchRecords.createRoute(recordsFilter)
            viewModelScope.launch {
                _uiState.update { it.copy(route = route) }
                _effect.emit(SearchEffects.NavToRecordsScreen)
            }
        //}
    }
}