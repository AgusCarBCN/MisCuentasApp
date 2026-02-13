package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model

sealed class CreateAccountEvent {

    data class AccountNameChanged(val value: String) : CreateAccountEvent()

    data class BalanceChanged(val value: String) : CreateAccountEvent()

    data class CurrencySelected(val currency: String) : CreateAccountEvent()

    object AddAccount : CreateAccountEvent()

    object Confirm: CreateAccountEvent()
}
