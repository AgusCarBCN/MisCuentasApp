package carnerero.agustin.cuentaappandroid.presentation.ui.records.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.data.db.entities.Records
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.DepositUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.WithDrawUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.InsertRecordUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddRecordsViewModel @Inject constructor(
    private val getAccounts: GetAllAccountsUseCase,
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val insertRecord: InsertRecordUseCase,
    private val withDrawUseCase: WithDrawUseCase,
    private val depositUseCase: DepositUseCase
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

    fun onEventUser(event: AddRecordsUiEvents){
        when(event){
            is AddRecordsUiEvents.AddRecord -> addRecord(event.category,event.accountId)
            is AddRecordsUiEvents.OnAccountSelectedChange -> onAccountSelectedChange(event.accountId)
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
    fun onAccountSelectedChange(accountId:Int){
        _uiState.update { current ->
            current.copy(accountSelected =accountId)
        }
    }
    fun addRecord(category:Category,accountId:Int) {
        viewModelScope.launch {
            val state = _uiState.value
            val recordAmount = state.recordAmount


            // Construimos el record con la cantidad positiva o negativa según tipo
            val amountForRecord = if (category.type == CategoryType.INCOME) {
                recordAmount
            } else {
                recordAmount.negate()
            }

            val record = Records(
                description = state.recordDescription,
                amount = amountForRecord,
                date = Date().dateFormat(),
                categoryId = category.id,
                accountId = accountId
            )
            Log.d("RECORDS","record from :$record")
            try {
                // Actualizamos el balance según tipo de categoría
                if (category.type == CategoryType.INCOME) {
                    depositUseCase(accountId, recordAmount)
                    _effect.emit(AddRecordsEffects.NewIncomeMessage)
                } else {
                    // Retiro: manejar saldo insuficiente
                    withDrawUseCase(accountId, recordAmount)
                    _effect.emit(AddRecordsEffects.NewExpenseMessage)
                }

                // Finalmente insertamos el record en la DB
                insertRecord.invoke(record)

            } catch (_: Exception) {
                // Emitir efecto de saldo insuficiente
                _effect.emit(AddRecordsEffects.AmountOverBalanceMessage)
            }
        }
    }
}