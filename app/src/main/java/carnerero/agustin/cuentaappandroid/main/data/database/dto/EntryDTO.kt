package carnerero.agustin.cuentaappandroid.main.data.database.dto

import carnerero.agustin.cuentaappandroid.main.data.database.entities.CategoryType
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.util.Date

data class EntryDTO(
    val id:Long,
    val description: String,
    val amount: Double,
    val date: String = Date().dateFormat(),
    val iconResource: Int,
    val nameResource: Int,
    val accountId: Int,
    val name:String="",
    val categoryId:Int,
    val categoryType:CategoryType
)
