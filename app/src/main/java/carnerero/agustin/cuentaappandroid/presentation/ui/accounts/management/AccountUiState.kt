package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.management

import carnerero.agustin.cuentaappandroid.data.db.entities.Account

data class AccountUiState(
    val accountId: Int=0,
    val showDialog:Boolean=false,
    val currencyCode:String="EUR",
    val accounts: List<Account> =emptyList()
)
