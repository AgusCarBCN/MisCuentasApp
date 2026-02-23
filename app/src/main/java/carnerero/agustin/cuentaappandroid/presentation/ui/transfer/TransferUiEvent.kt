package carnerero.agustin.cuentaappandroid.presentation.ui.transfer

sealed class TransferUiEvent {
    data class OnAmountChange(val amount:String): TransferUiEvent()
    data class OnAccountOriginChange(val accountId:Int): TransferUiEvent()
    data class OnAccountDestinationChange(val accountId:Int): TransferUiEvent()
    data class OnConfirm(val labelFrom:String,val labelTo:String) : TransferUiEvent()

    object NavToBack : TransferUiEvent()
}