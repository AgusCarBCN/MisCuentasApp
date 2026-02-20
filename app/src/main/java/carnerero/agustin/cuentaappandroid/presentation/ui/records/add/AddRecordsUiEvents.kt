package carnerero.agustin.cuentaappandroid.presentation.ui.records.add

import carnerero.agustin.cuentaappandroid.presentation.ui.profile.ProfileUiEvent
import java.math.BigDecimal

sealed class AddRecordsUiEvents {

    data class OnDescriptionRecordChange(val description: String) : AddRecordsUiEvents()
    data class OnAmountRecordChange(val amount: BigDecimal) : AddRecordsUiEvents()
    data class OnAccountSelectedChange(val accountId:Int) : AddRecordsUiEvents()
    object Confirm: AddRecordsUiEvents()
}