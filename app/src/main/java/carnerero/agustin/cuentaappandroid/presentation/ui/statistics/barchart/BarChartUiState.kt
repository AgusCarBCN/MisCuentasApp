package carnerero.agustin.cuentaappandroid.presentation.ui.statistics.barchart

import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.ui.statistics.barchart.model.BarChartData
import java.time.LocalDate

data class BarChartUiState(

    val accounts:List<Account> =emptyList(),
    val currencyCode:String ="EUR",
    val accountSelected : Int = 1,
    val barchartData : List<BarChartData> = emptyList(),
    val yearSelected : String= LocalDate.now().year.toString(),
    val isDarkTheme:Boolean =false
)