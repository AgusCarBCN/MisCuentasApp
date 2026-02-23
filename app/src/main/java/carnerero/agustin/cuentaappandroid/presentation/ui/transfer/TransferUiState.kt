package carnerero.agustin.cuentaappandroid.presentation.ui.transfer

import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import java.math.BigDecimal

data class TransferUiState(
    val accounts: List<Account> = emptyList(),
    val currencyCode:String = "EUR",
    val accountOriginSelected:Int =1,
    val accountDestinationSelected:Int=1,
    val amount: BigDecimal = BigDecimal.ZERO,
    )
{
    val enableConfirm = accountOriginSelected!=accountDestinationSelected
            && (amount> BigDecimal.ZERO || amount.toString()!=".")



}

