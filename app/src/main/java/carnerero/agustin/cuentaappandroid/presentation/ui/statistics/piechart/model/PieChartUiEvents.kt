package carnerero.agustin.cuentaappandroid.presentation.ui.statistics.piechart.model

import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField

sealed class PieChartUiEvents {

    data class OnAccountChange(val accountId: Int): PieChartUiEvents()
    data class OnShowDatePicker(
        val field: DateField,
        val visible: Boolean
    ) : PieChartUiEvents()
    data class OnSelectDate(
        val field: DateField,
        val date: String
    ) : PieChartUiEvents()

}