package carnerero.agustin.cuentaappandroid.presentation.ui.records.modify

import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.math.BigDecimal
import java.util.Date

data class RecordDetailUiState(
    val amount: String= "",
    val date:String= Date().dateFormat(),
    val description:String="",
    val showDatePicker: Boolean = false,
)
