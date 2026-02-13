package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model

sealed class CreateAccountEvent {

    data class AccountNameChanged(val value: String) : CreateAccountEvent()

    data class BalanceChanged(val value: String) : CreateAccountEvent()

    data class CurrencySelected(val currency: Currency) : CreateAccountEvent()

    object Submit : CreateAccountEvent()
}
