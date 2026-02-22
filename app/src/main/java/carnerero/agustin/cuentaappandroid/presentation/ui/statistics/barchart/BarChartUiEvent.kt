package carnerero.agustin.cuentaappandroid.presentation.ui.statistics.barchart

sealed class BarChartUiEvent {

    data class OnYearSelected(val year: String) : BarChartUiEvent()
    data class OnAccountSelected(val accountId: Int): BarChartUiEvent()
}