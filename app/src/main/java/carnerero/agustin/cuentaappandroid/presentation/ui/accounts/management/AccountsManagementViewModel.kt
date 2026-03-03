package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.DeleteAccountByIdUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
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
class AccountsManagementViewModel @Inject constructor(
    private val getAccounts: GetAllAccountsUseCase,
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val deleteAccount: DeleteAccountByIdUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState

    private val _effect = MutableSharedFlow< AccountsEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeInitialData()
    }
    private fun observeInitialData() {
        viewModelScope.launch {
            combine(
                getCurrencyCode.invoke(),
                getAccounts.invoke()

            ) { currencyCode,accounts ->
                _uiState.value.copy(
                    accounts = accounts,
                    currencyCode = currencyCode
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
    fun onEventUser(event: AccountUiEvent){
        when(event){
            AccountUiEvent.OnCloseDialog -> closeDialog()
            is AccountUiEvent.OnConfirm -> confirm(event.accountId)
            is AccountUiEvent.OnOpenDialog -> openDialog(event.accountId)
            is AccountUiEvent.OnNavigateToAccountDetail -> navigateToAccountDetail(event.accountId)
        }
    }
    fun navigateToAccountDetail(accountId: Int){
        viewModelScope.launch {
             _uiState.update { current->
                 current.copy(
                     accountId=accountId
                 )
             }
            _effect.emit(AccountsEffect.NavToAccountDetail)
        }
    }

    fun openDialog(accountId:Int) {
        _uiState.update { current ->
            current.copy(
               showDialog = true,
                accountId = accountId
                )
        }
    }
    fun closeDialog() {
        _uiState.update { current ->
            current.copy(
              showDialog = false
            )
        }
    }
    fun confirm(accountId:Int){
        viewModelScope.launch {
            deleteAccount.invoke(accountId)
            _effect.emit(AccountsEffect.MessageDeleteAccount)
            closeDialog()
        }
    }
}