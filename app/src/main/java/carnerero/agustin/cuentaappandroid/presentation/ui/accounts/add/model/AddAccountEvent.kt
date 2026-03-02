package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.model

sealed class AddAccountEvent {

    data class AccountNameChanged(val value: String) : AddAccountEvent()

    data class BalanceChanged(val value: String) : AddAccountEvent()

    data class CurrencySelected(val currency: String) : AddAccountEvent()

    data class DropdownExpandedChange(val value:Boolean): AddAccountEvent()

    object AddAccount : AddAccountEvent()

    object Confirm: AddAccountEvent()

    object BackToCreateProfile: AddAccountEvent()
}
