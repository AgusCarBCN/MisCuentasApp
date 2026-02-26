package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.model

import carnerero.agustin.cuentaappandroid.utils.dateFormatByLocale
import java.time.LocalDate

data class DialogUiState(
    val id: Int=0,
    val fromDate: String = LocalDate.now().dateFormatByLocale(),
    val toDate: String=LocalDate.now().dateFormatByLocale(),
    val showDialog:Boolean=false,
    val showFromDatePicker:Boolean=false,
    val showToDatePicker:Boolean=false,
    val spendLimit: String= ""
)
