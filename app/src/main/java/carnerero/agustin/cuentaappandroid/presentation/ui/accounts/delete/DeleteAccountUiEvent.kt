package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.delete

import carnerero.agustin.cuentaappandroid.data.db.entities.Account

sealed class DeleteAccountUiEvent {

    data class OnConfirm(val accountId:Int) : DeleteAccountUiEvent()

    data class OnOpenDialog(val accountId:Int) : DeleteAccountUiEvent()


    object OnCloseDialog : DeleteAccountUiEvent()

}