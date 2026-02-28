package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetSumTotalExpensesByAccountUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.model.BudgetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.roundToInt

@HiltViewModel
class AccountsSpendingControlViewModel @Inject constructor(
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val getAllAccounts: GetAllAccountsUseCase,
    private val getSumExpensesByAccount: GetSumTotalExpensesByAccountUseCase)
    : ViewModel() {
    private val _uiState = MutableStateFlow(AccountsUiState())
    val uiState: StateFlow<AccountsUiState> = _uiState


    init {
        observeInitialData()
    }

    private fun observeInitialData() {
        viewModelScope.launch {
            val currencyCode=getCurrencyCode.invoke()
            getAllAccounts.invoke()
                .collect { accounts ->
                    _uiState.update { current ->
                        current.copy(
                            accounts = accounts,
                            currencyCode = currencyCode,
                        )
                    }
                }
        }
    }
    fun observeCategorySpending(account: Account) {
        viewModelScope.launch {
            getSumExpensesByAccount
                .invoke(account.id,account.fromDate,account.toDate)
                .collect { expense ->

                    val safeExpense = expense?: BigDecimal.ZERO
                    val limit = account.spendingLimit

                    val percentage = if (limit > BigDecimal.ZERO) {
                        abs(safeExpense.toFloat() / limit.toFloat())
                            .coerceIn(0f, 1f)
                    } else {
                        0f
                    }

                    val percent = (percentage * 100).roundToInt()

                    // 1️⃣ Crear un mapa mutable temporal
                    val updatedMap = _uiState.value.accountBudgetMap.toMutableMap()

                    // 2️⃣ Añadir/actualizar la categoría
                    updatedMap[account.id] = BudgetUiState(
                        expenses = safeExpense,
                        spendingPercentage = percentage,
                        spendingPercent = percent
                    )

                    // 3️⃣ Actualizar el StateFlow
                    _uiState.update { current ->
                        current.copy(accountBudgetMap = updatedMap)
                    }
                }
        }
    }
}