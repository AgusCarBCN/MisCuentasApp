package carnerero.agustin.cuentaappandroid.presentation.ui.records.components

sealed class RecordsFilter(val routeName: String) {
    object Expenses : RecordsFilter("Expenses")
    object Incomes : RecordsFilter("Incomes")
    data class RecordsByAccount(val accountId: Int) : RecordsFilter("RecordsByAccount")
}