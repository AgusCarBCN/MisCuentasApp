package carnerero.agustin.cuentaappandroid.presentation.ui.transfer

sealed class TransferEffects {

    object MessageSuccess : TransferEffects()
    object OverBalanceError: TransferEffects()
    object NavToHome: TransferEffects()
    object NavBack : TransferEffects()
}