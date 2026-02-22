package carnerero.agustin.cuentaappandroid.presentation.ui.records.add

import android.R.attr.type
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.data.db.entities.Records
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.UpdateAccountBalanceUseCase
import carnerero.agustin.cuentaappandroid.domain.database.categoryusecase.GetAllCategoriesByType
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.InsertRecordUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.records.categories.SelectCategoriesEffects
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddRecordsViewModel @Inject constructor(
    private val getAccounts: GetAllAccountsUseCase,
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val addRecord: InsertRecordUseCase,
    private val updateBalance: UpdateAccountBalanceUseCase,
    ): ViewModel(){

    private val _uiState = MutableStateFlow(AddRecordsUiState())
    val uiState: StateFlow<AddRecordsUiState> = _uiState

    private val _effect = MutableSharedFlow<AddRecordsEffects>()
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

    fun onEventUser(event: AddRecordsUiEvents){
        when(event){
            is AddRecordsUiEvents.AddRecord -> addRecord(event.category)
            is AddRecordsUiEvents.OnAccountSelectedChange -> onAccountSelectedChange(event.account)
            is AddRecordsUiEvents.OnAmountRecordChange -> onAmountRecordChange(event.amount)
            is AddRecordsUiEvents.OnDescriptionRecordChange -> onDescriptionRecordChanged(event.description)
        }
    }

    fun onDescriptionRecordChanged(description: String) {
        _uiState.update { current ->
            current.copy(recordDescription = description )
        }
    }
    fun onAmountRecordChange(amount: BigDecimal){
        _uiState.update { current ->
            current.copy(recordAmount = amount )
        }
    }
    fun onAccountSelectedChange(account: Account){
        _uiState.update { current ->
            current.copy(accountSelected =account)
        }
    }
    fun addRecord(category: Category){
        viewModelScope.launch {
            val state=_uiState.value
            val accountsSize=_uiState.value.accounts.size
            val recordAmount=if (category.type == CategoryType.INCOME)
                state.recordAmount  else state.recordAmount.negate()
            val balance=state.accountSelected.balance
            val account=state.accountSelected
            val record=Records(
                description = state.recordDescription,
                amount = if (category.type == CategoryType.INCOME)
                   state.recordAmount  else state.recordAmount.negate(),
                date = Date().dateFormat(),
                categoryId = category.id,
                accountId = state.accountSelected.id
            )
            val newBalance=balance-recordAmount
            addRecord.invoke(record)
            updateBalance.invoke(account.id,newBalance)
            if(category.type== CategoryType.INCOME){
                _effect.emit(AddRecordsEffects.NewIncomeMessage)
            }else if(accountsSize==0){
                _effect.emit(AddRecordsEffects.AccountsNotFoundMessage)
            }else if(recordAmount>balance){
                _effect.emit(AddRecordsEffects.AmountOverBalanceMessage)
            }
            else{
                _effect.emit(AddRecordsEffects.NewExpenseMessage)
            }
        }
    }
}