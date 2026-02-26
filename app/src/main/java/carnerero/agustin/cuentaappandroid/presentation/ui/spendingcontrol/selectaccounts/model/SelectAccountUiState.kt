package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectaccounts.model

import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.model.DialogUiState

data class SelectAccountUiState(
    val accounts:List<Account> = emptyList(),
    val currencyCode :String= "EUR",
    val dialogUiState: DialogUiState = DialogUiState()
)
