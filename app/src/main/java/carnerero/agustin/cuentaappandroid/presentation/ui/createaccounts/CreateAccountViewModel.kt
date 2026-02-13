package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.InsertAccountUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountEvent
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.Currencies.currencies
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val setCurrencyCode: SetCurrencyCodeUseCase,
    private val addAccount: InsertAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateAccountUiState())
    val uiState: StateFlow<CreateAccountUiState> = _uiState

    private val _effect = MutableSharedFlow<CreateAccountEffect>()
    val effect = _effect.asSharedFlow()
    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val currencies = getListOfCurrencyCode()
        viewModelScope.launch {
            val savedCurrencyCode = getCurrencyCode()
            val selectedCurrency = currencies.firstOrNull {
                it.currencyCode == savedCurrencyCode
            } ?: currencies.first()
            _uiState.update {
                it.copy(
                    currencies = currencies,
                    selectedCurrency = selectedCurrency
                )
            }
        }
    }

    fun onEvent(event: CreateAccountEvent) {
        when (event) {
            is CreateAccountEvent.AccountNameChanged -> {
                _uiState.update { it.copy(accountName = event.value) }
            }
            is CreateAccountEvent.BalanceChanged -> {
                _uiState.update { it.copy(balance = event.value) }
            }
            is CreateAccountEvent.CurrencySelected -> {
                _uiState.update { it.copy(selectedCurrency = event.currency) }
            }
            CreateAccountEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val state = _uiState.value
        if (!state.isFormValid) return

        viewModelScope.launch {
            try {
                val currency = state.selectedCurrency!!
                // Guardar moneda seleccionada
                setCurrencyCode(currency.currencyCode)
                // Crear entidad de dominio
                val account = Account(
                    name = state.accountName,
                    balance = state.balance.toBigDecimal()
                )                // Insertar en BD
                addAccount(account)
                // Efectos
                _effect.emit(
                    CreateAccountEffect.ShowMessage(R.string.addAccount))

            } catch (e: Exception) {
                _effect.emit(
                    CreateAccountEffect.ShowMessage(
                        R.string.erroraccountcreated
                    )
                )
            }
        }
    }
    fun confirm(){
        viewModelScope.launch {
            _effect.emit(CreateAccountEffect.NavigateToLogin)
        }
    }
    fun back(){
        viewModelScope.launch {
            _effect.emit(CreateAccountEffect.NavigateBack)
        }
    }
    private fun getListOfCurrencyCode(): List<Currency> {
        // Sort the list by currency description, defaulting to an empty list if `currencies` is null
        val sortedCurrencies = currencies.sortedBy { it.currencyDescription }
        return sortedCurrencies
    }
}
