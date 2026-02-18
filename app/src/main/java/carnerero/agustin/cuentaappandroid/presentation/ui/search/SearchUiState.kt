package carnerero.agustin.cuentaappandroid.presentation.ui.search

import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.ui.search.model.SearchFilter
import carnerero.agustin.cuentaappandroid.utils.Utils


data class SearchUiState(

    val showDatePickerFrom: Boolean = false, // controla el date picker "from"
    val showDatePickerTo: Boolean = false,
    val searchFilter: SearchFilter? = null,
    val accounts:List<Account> = emptyList(),
    val currencyCode: String ="EUR",
    val route:String=""
) {
    val enableSearchButton=validateAmounts() && validateDates()
    private fun validateAmounts(): Boolean {
        return if (searchFilter != null) {
            searchFilter.amountMax >= searchFilter.amountMin
        } else false

    }
    private fun validateDates():Boolean {
        if (searchFilter != null) {
            val fromDate = Utils.Companion.convertStringToLocalDate(searchFilter.dateFrom)
            val toDate = Utils.Companion.convertStringToLocalDate(searchFilter.dateTo)
            return fromDate.isBefore(toDate) || fromDate.isEqual(toDate)
        }else return false
    }
}
