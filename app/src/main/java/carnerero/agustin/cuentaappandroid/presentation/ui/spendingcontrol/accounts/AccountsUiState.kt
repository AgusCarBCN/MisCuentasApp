package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.accounts

import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.model.BudgetUiState

data class AccountsUiState(
    val accounts:List<Account> =emptyList(),
    val currencyCode:String="EUR",
    val accountBudgetMap: Map<Int, BudgetUiState> = emptyMap()
)
