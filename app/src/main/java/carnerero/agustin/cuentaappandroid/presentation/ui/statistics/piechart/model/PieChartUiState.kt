package carnerero.agustin.cuentaappandroid.presentation.ui.statistics.piechart.model

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.util.Date

data class PieChartUiState (
    val accounts: List<Account> =emptyList(),
    val records:List<RecordDTO> = emptyList(),
    val accountSelected: Int=1,
    val currencyCode: String ="EUR",
    val fromDate: String= Date().dateFormat(),
    val toDate: String=Date().dateFormat(),
    val showDatePickerFrom: Boolean = false,
    val showDatePickerTo: Boolean = false,
)
