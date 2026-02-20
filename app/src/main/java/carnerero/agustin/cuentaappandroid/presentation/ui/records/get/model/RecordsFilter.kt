package carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model

import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.SearchFilter

sealed class RecordsFilter(val routeName: String) {

    object All: RecordsFilter("All")
    object Expenses : RecordsFilter("Expenses")
    object Incomes : RecordsFilter("Incomes")

    data class Search(val searchFilter: SearchFilter): RecordsFilter ("Search")
    data class RecordsByAccount(val accountId: Int) : RecordsFilter("RecordsByAccount")
}