package carnerero.agustin.cuentaappandroid.presentation.ui.records.add


import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import java.math.BigDecimal

data class AddRecordsUiState(
    val categories:List<Category> = emptyList(),
    val accounts:List<Account> = emptyList(),
    val categorySelected: Int =0,
    val accountSelected:Int=0,
    val recordDescription:String="",
    val recordAmount: BigDecimal= BigDecimal.ZERO,
    val enableConfirmButton:Boolean=false
)
