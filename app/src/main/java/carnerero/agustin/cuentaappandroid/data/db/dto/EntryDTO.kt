package carnerero.agustin.cuentaappandroid.data.db.dto

import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.math.BigDecimal
import java.util.Date

data class EntryDTO(
    val id:Long,
    val description: String,
    val amount: BigDecimal,
    val date: String = Date().dateFormat(),
    val iconResource: Int,
    val nameResource: Int,
    val accountId: Int,
    val name:String="",
    val categoryId:Int,
    val categoryType: CategoryType
)
