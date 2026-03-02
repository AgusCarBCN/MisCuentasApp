package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.InsertAccountUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.model.AddAccountEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.model.AddAccountEvent
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.model.AddAccountUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.model.Currencies
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.model.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val setCurrencyCode: SetCurrencyCodeUseCase,
    private val addAccount: InsertAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddAccountUiState())
    val uiState: StateFlow<AddAccountUiState> = _uiState

    private val _effect = MutableSharedFlow<AddAccountEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val currencies = getListOfCurrencyCode()
        viewModelScope.launch {
            getCurrencyCode.invoke().collect { currencyCode ->
                _uiState.update {
                    it.copy(
                        currencies = currencies,
                        selectedCurrency = currencyCode
                    )
                }
            }
        }
    }


    fun onDropdownExpandedChange(expanded: Boolean) {
        _uiState.update { it.copy(isDropdownExpanded = expanded) }
    }

    fun onEvent(event: AddAccountEvent) {
        when (event) {
            is AddAccountEvent.AccountNameChanged -> {
                _uiState.update { it.copy(accountName = event.value) }
            }

            is AddAccountEvent.BalanceChanged -> {
                _uiState.update { it.copy(balance = event.value) }
            }

            is AddAccountEvent.CurrencySelected -> {
                _uiState.update { it.copy(selectedCurrency = event.currency)
                   }
                Log.d("DATASTORE", "Currency code change= ${_uiState.value.selectedCurrency}")
            }

            is AddAccountEvent.AddAccount -> addAccount()
            is AddAccountEvent.Confirm-> {
                confirm()
            }

            AddAccountEvent.BackToCreateProfile -> {
                back()
            }
            is AddAccountEvent.DropdownExpandedChange -> {
                onDropdownExpandedChange(event.value)
            }
        }
    }

    private fun addAccount() {
        val state = _uiState.value
        if (!state.isFormValid) return

        viewModelScope.launch {
            try {
                // Crear entidad de dominio
                val account = Account(
                    name = state.accountName,
                    balance = state.balance.toBigDecimal()
                )
                // Insertar en BD
                addAccount(account)
                _uiState.update {
                    it.copy(
                        accountName = "",
                        balance = ""
                    )
                }
                // Efectos
                _effect.emit(
                    AddAccountEffect.ShowMessage(R.string.addAccount)
                )

            } catch (e: Exception) {
                _effect.emit(
                    AddAccountEffect.ShowMessage(
                        R.string.erroraccountcreated
                    )
                )
            }
        }
    }


    private fun confirm() {
        val currencyCode = _uiState.value.selectedCurrency

        viewModelScope.launch {
            setCurrencyCode(currencyCode)
            // Confía en la escritura
            _effect.emit(AddAccountEffect.NavigateToLogin)
        }
    }


    fun back() {
        viewModelScope.launch {
            _effect.emit(AddAccountEffect.NavigateBack)
        }
    }

    private fun getListOfCurrencyCode(): List<Currency> {
        // Sort the list by currency description, defaulting to an empty list if `currencies` is null
        val sortedCurrencies = Currencies.currencies.sortedBy { it.currencyDescription }
        return sortedCurrencies
    }

    fun setCurrencyCode(currencyCode: String) {
        viewModelScope.launch {
               setCurrencyCode.invoke(currencyCode)
        }
    }


}