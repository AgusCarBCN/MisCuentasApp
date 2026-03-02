package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.delete

import carnerero.agustin.cuentaappandroid.data.db.entities.Account

data class DeleteAccountUiState(
    val accountId: Int=0,
    val showDialog:Boolean=false,
    val currencyCode:String="EUR",
    val accounts: List<Account> =emptyList()
)
