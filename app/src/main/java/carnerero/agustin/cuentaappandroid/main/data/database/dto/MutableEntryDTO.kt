package carnerero.agustin.cuentaappandroid.main.data.database.dto

import androidx.compose.runtime.MutableState
import carnerero.agustin.cuentaappandroid.main.data.database.entities.CategoryType

data class MutableEntryDTO(
    var id: Long,
    var description: MutableState<String>,
    var amount: MutableState<Double>,
    var date: MutableState<String>,
    val iconResource: Int,
    val nameResource: Int,
    val accountId: Int,
    val name: String,
    val categoryId: Int,
    val categoryType: CategoryType
)
