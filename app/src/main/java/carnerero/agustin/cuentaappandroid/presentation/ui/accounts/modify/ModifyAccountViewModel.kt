package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.modify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.UpdateAccountBalanceUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.UpdateAccountNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ModifyAccountViewModel @Inject constructor(
    private val updateName: UpdateAccountNameUseCase,
    private val updateBalance: UpdateAccountBalanceUseCase,
): ViewModel(){
    private val _uiState = MutableStateFlow(ModifyAccountDetailUiState())
    val uiState: StateFlow<ModifyAccountDetailUiState> = _uiState

    //private val _effect = MutableSharedFlow<AddAccountEffect>()
    //val effect = _effect.asSharedFlow()

    fun updateName(accountId:Int,newName:String){
        viewModelScope.launch {
            updateName.invoke(accountId,newName)
        }
    }
    fun updateBalance(accountId:Int,newBalance:String){
        viewModelScope.launch {
            updateBalance.invoke(accountId,newBalance.toBigDecimal())
        }
    }
}