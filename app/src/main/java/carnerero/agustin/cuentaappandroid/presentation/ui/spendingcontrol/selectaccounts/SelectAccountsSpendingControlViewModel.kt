package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectaccounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.UpdateAccountDateFromUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.UpdateAccountDateToUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.UpdateCheckedAccountUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.UpdateSpendingLimitAccountUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.model.DialogUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectaccounts.model.SelectAccountUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectaccounts.model.SelectAccountsUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class SelectAccountsSpendingControlViewModel @Inject constructor(
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val getAccounts: GetAllAccountsUseCase,
    private val upDateChecked: UpdateCheckedAccountUseCase,
    private val upDateSpendingLimit: UpdateSpendingLimitAccountUseCase,
    private val upDateFromDate: UpdateAccountDateFromUseCase,
    private val upDateToDate: UpdateAccountDateToUseCase
)
    : ViewModel(){

    private val _uiState = MutableStateFlow(SelectAccountUiState())
    val uiState: StateFlow<SelectAccountUiState> = _uiState

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

    fun onUserEvent(event: SelectAccountsUiEvent){
        when(event){
            is SelectAccountsUiEvent.OnConfirm -> onConfirm()
            is SelectAccountsUiEvent.OnSelectDate -> onSelectedDate(event.field,event.date)
            is SelectAccountsUiEvent.OnShowDatePicker -> onShowDatePicker(event.field,event.visible)
            is SelectAccountsUiEvent.OnSpendLimitChange -> onSpendingLimitChange(event.newSpendLimit)
            is SelectAccountsUiEvent.OnCheckedChange -> onCheckBoxChange(event.accountId,event.newValue)
            is SelectAccountsUiEvent.OnOpenDialog -> openDialog(event.category)
            is SelectAccountsUiEvent.OnCloseDialog -> closeDialog()
        }
    }

    fun onCheckBoxChange(accountId: Int, newValue: Boolean) {
        viewModelScope.launch {
             upDateChecked(accountId, newValue)
            if(!newValue){
                upDateSpendingLimit(accountId, BigDecimal.ZERO)
            }
        }
    }
    fun onShowDatePicker(field: DateField, visible: Boolean) {
        _uiState.update { current ->
            val updatedDates = (current.dialogUiState).copy(
                showFromDatePicker = if (field == DateField.FROM) visible else current.dialogUiState.showFromDatePicker,
                showToDatePicker = if (field == DateField.TO) visible else current.dialogUiState.showToDatePicker
            )
            current.copy(dialogUiState = updatedDates)
        }
    }

    fun onSpendingLimitChange(newValue:String){
        _uiState.update { current ->
            current.copy(
                dialogUiState = current.dialogUiState.copy(
                    spendLimit =newValue
                )
            )
        }
    }


    fun onSelectedDate(field: DateField, date: String) {
        _uiState.update { current ->
            val updatedDates = (current.dialogUiState).copy(
                fromDate = if (field == DateField.FROM) date else current.dialogUiState.fromDate,
                toDate = if (field == DateField.TO) date else current.dialogUiState.toDate
            )
            current.copy(dialogUiState = updatedDates)
        }
    }

    fun onConfirm() {
        val dialog = _uiState.value.dialogUiState
        val categoryId = dialog.id

        viewModelScope.launch {
            upDateSpendingLimit(categoryId, dialog.spendLimit.toBigDecimalOrNull() ?: BigDecimal.ZERO)
            upDateFromDate(categoryId, dialog.fromDate)
            upDateToDate(categoryId, dialog.toDate)
            //onShowDialog(categoryId, false)
            closeDialog()
        }
    }
    fun openDialog(account: Account) {
        _uiState.update { current ->
            current.copy(
                dialogUiState = current.dialogUiState.copy(
                    id = account.id,
                    showDialog = true,
                    spendLimit = account.spendingLimit.toPlainString(),
                    fromDate =account.fromDate,
                    toDate = account.toDate
                )
            )
        }
    }
    fun closeDialog() {
        _uiState.update { current ->
            current.copy(
                dialogUiState = DialogUiState()
            )
        }
    }

}