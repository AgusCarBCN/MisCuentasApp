package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.modify

import java.math.BigDecimal

sealed class ModifyAccountUserEvent {

    data class OnChangeName(val newName:String) : ModifyAccountUserEvent()
    data class OnChangeBalance(val newBalance: String) : ModifyAccountUserEvent()
    data class UpdateName(val accountId:Int,val updatedName:String): ModifyAccountUserEvent()
    data class UpdateBalance(val accountId:Int,val updatedBalance: BigDecimal): ModifyAccountUserEvent()
}