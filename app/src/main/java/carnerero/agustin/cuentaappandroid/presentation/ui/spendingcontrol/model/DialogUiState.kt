package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.model

import carnerero.agustin.cuentaappandroid.utils.dateFormat
import carnerero.agustin.cuentaappandroid.utils.dateFormatByLocale
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Date

data class DialogUiState(
    val categoryId: Int=0,
    val fromDate: String = LocalDate.now().dateFormatByLocale(),
    val toDate: String=LocalDate.now().dateFormatByLocale(),
    val showDialog:Boolean=false,
    val showFromDatePicker:Boolean=false,
    val showToDatePicker:Boolean=false,
    val spendLimit: String= ""
)
