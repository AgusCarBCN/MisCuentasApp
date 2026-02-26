package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectaccounts.model

import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField

sealed class SelectAccountsUiEvent {

    object OnConfirm : SelectAccountsUiEvent()

    data class OnSpendLimitChange(val newSpendLimit:String) : SelectAccountsUiEvent()

    data class OnCheckedChange(val accountId:Int,val newValue:Boolean) : SelectAccountsUiEvent()

    data class OnOpenDialog(val category: Account) : SelectAccountsUiEvent()

    object OnCloseDialog : SelectAccountsUiEvent()

    data class OnShowDatePicker(
        val field: DateField,
        val visible: Boolean
    ) : SelectAccountsUiEvent()

    data class OnSelectDate(
        val field: DateField,
        val date: String
    ) : SelectAccountsUiEvent()
}