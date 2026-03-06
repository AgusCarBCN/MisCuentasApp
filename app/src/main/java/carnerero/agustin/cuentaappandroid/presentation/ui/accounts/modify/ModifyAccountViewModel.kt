package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.modify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAccountByIdUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.UpdateAccountBalanceUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.UpdateAccountNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ModifyAccountViewModel @Inject constructor(
    private val updateName: UpdateAccountNameUseCase,
    private val updateBalance: UpdateAccountBalanceUseCase,
    private val getAccountById: GetAccountByIdUseCase
): ViewModel(){
    private val _uiState = MutableStateFlow(ModifyAccountDetailUiState())
    val uiState: StateFlow<ModifyAccountDetailUiState> = _uiState

    private val _effect = MutableSharedFlow<ModifyAccountsEffects>()
    val effect = _effect.asSharedFlow()

    fun onEventUser(event: ModifyAccountUserEvent){
        when(event){
            is ModifyAccountUserEvent.OnChangeBalance -> onBalanceChange(event.newBalance)
            is ModifyAccountUserEvent.OnChangeName -> onNameChange(event.newName)
            is ModifyAccountUserEvent.UpdateBalance ->
                updateAccountBalance(event.accountId,event.updatedBalance.toString())
            is ModifyAccountUserEvent.UpdateName ->
                updateAccountName(event.accountId,event.updatedName)
        }
    }

    fun onBalanceChange(balance: String){
        _uiState.update { current->
            current.copy(
            balance=balance
            )
        }
    }
    fun onNameChange(newName: String){
        _uiState.update { current->
            current.copy(
                name = newName
            )
        }
    }
    fun updateAccountName(accountId:Int,newName:String){
        viewModelScope.launch {
            updateName.invoke(accountId,newName)
            _effect.emit(ModifyAccountsEffects.MessageNameChange)
        }
    }
    fun updateAccountBalance(accountId:Int,newBalance:String){
        viewModelScope.launch {
            updateBalance.invoke(accountId,newBalance.toBigDecimal())
            _effect.emit(ModifyAccountsEffects.MessageBalanceChange)
        }
    }
    fun getInitValues(accountId:Int){
        viewModelScope.launch {
            val account=getAccountById.invoke(accountId)
            _uiState.update { current->
                current.copy(
                    name= account?.name ?: "",
                    balance = account?.balance.toString()
                )

            }
        }
    }
}