package carnerero.agustin.cuentaappandroid.presentation.ui.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.entities.Records
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.TransferUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.InsertRecordUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
class TransferViewModel @Inject constructor(
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val getAccounts: GetAllAccountsUseCase,
    private val insertRecord: InsertRecordUseCase,
    private val transferAmount: TransferUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState

    private val _effect = MutableSharedFlow<TransferEffects>()
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


    fun onUserEvent(event: TransferUiEvent){
        when(event){
            is TransferUiEvent.OnAccountDestinationChange -> onAccountDestinationSelected(event.accountId)
            is TransferUiEvent.OnAccountOriginChange -> onAccountOriginSelected(event.accountId)
            is TransferUiEvent.OnAmountChange -> onAmountSelected(event.amount)
            is TransferUiEvent.OnConfirm -> onConfirm(event.labelFrom,event.labelTo)
            TransferUiEvent.NavToBack ->  onNavBack()
        }
    }
    fun onAccountOriginSelected(accountId: Int) {
        _uiState.update { current ->
            current.copy(
               accountOriginSelected = accountId
            )
        }
    }
    fun onAccountDestinationSelected(accountId: Int) {
        _uiState.update { current ->
            current.copy(
                accountDestinationSelected = accountId
            )
        }
    }
    fun onAmountSelected(amount:String) {
        _uiState.update { current ->
            current.copy(
                amount = amount.toBigDecimalOrNull()?: BigDecimal.ZERO
            )
        }
    }

    fun onNavBack(){
        viewModelScope.launch {
            _effect.emit(TransferEffects.NavBack)
        }
    }
    fun onConfirm(fromLabel:String,toLabel:String){
        viewModelScope.launch {
            val state = _uiState.value
            val originAccount=state.accountOriginSelected
            val destinationAccount=state.accountDestinationSelected
            val amount=state.amount

            try {
                insertRecord(
                    Records(
                        description = fromLabel,
                        amount = amount.negate(),
                        date = Date().dateFormat(),
                        categoryId = 53,
                        accountId = originAccount
                    )
                )
                insertRecord(
                    Records(
                        description = toLabel,
                        amount = amount,
                        date = Date().dateFormat(),
                        categoryId = 53,
                        accountId = destinationAccount
                    )
                )
                transferAmount.invoke(originAccount, destinationAccount, amount)
                _effect.emit(TransferEffects.MessageSuccess)
                delay(1000)
                _effect.emit(TransferEffects.NavToHome)
            }catch (_: Exception){
                _effect.emit(TransferEffects.OverBalanceError)

            }
        }
    }

}