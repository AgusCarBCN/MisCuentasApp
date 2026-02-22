package carnerero.agustin.cuentaappandroid.presentation.ui.records.add


import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import java.math.BigDecimal

data class AddRecordsUiState(
    val accounts:List<Account> = emptyList(),
    val accountSelected:Int=1,
    val currencyCode:String="EUR",
    val recordDescription:String="",
    val recordAmount: BigDecimal= BigDecimal.ZERO
){
    val enableConfirmButton= accounts.isNotEmpty()
            && recordDescription.isNotEmpty()
            && recordAmount.toString().isNotEmpty()
}
