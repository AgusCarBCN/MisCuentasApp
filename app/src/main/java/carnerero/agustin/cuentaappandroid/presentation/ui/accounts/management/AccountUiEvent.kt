package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.management

sealed class AccountUiEvent {

    data class OnConfirm(val accountId:Int) : AccountUiEvent()

    data class OnOpenDialog(val accountId:Int) : AccountUiEvent()


    object OnCloseDialog : AccountUiEvent()

    data class OnNavigateToAccountDetail(val accountId:Int): AccountUiEvent()

}