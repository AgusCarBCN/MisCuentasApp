package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.modify

import java.math.BigDecimal

data class ModifyAccountDetailUiState(
    val name:String="",
    val balance: BigDecimal= BigDecimal.ZERO,

){
    val enableChangeButton=name.isNotBlank()
    val enableChangeBalance=balance.toString().isNotBlank()
}
