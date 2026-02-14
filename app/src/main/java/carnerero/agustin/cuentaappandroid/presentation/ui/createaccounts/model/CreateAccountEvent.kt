package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model

sealed class CreateAccountEvent {

    data class AccountNameChanged(val value: String) : CreateAccountEvent()

    data class BalanceChanged(val value: String) : CreateAccountEvent()

    data class CurrencySelected(val currency: String) : CreateAccountEvent()

    data class DropdownExpandedChange(val value:Boolean): CreateAccountEvent()

    object AddAccount : CreateAccountEvent()

    object Confirm: CreateAccountEvent()

    object BackToCreateProfile: CreateAccountEvent()
}
