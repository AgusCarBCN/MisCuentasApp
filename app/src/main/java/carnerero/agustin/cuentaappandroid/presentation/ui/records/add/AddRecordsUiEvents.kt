package carnerero.agustin.cuentaappandroid.presentation.ui.records.add

import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import java.math.BigDecimal

sealed class AddRecordsUiEvents {

    data class OnDescriptionRecordChange(val description: String) : AddRecordsUiEvents()
    data class OnAmountRecordChange(val amount: BigDecimal) : AddRecordsUiEvents()
    data class OnAccountSelectedChange(val accountId: Int) : AddRecordsUiEvents()
    data class AddRecord(val category: Category,val accountId:Int): AddRecordsUiEvents()
}