package carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords

import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.SearchFilter
import carnerero.agustin.cuentaappandroid.utils.Utils


data class SearchUiState(

    val showDatePickerFrom: Boolean = false, // controla el date picker "from"
    val showDatePickerTo: Boolean = false,
    val searchFilter: SearchFilter = SearchFilter(),
    val accounts:List<Account> = emptyList(),
    val currencyCode: String ="EUR",
    val route:String="",
){
    // Propiedad derivada para habilitar el botón
    val enableSearchButton: Boolean
        get() = searchFilter.let { validateAmounts(it) && validateDates(it) }

    // Validación de montos
    private fun validateAmounts(filter: SearchFilter): Boolean =
        filter.amountMax >= filter.amountMin

    // Validación de fechas
    private fun validateDates(filter: SearchFilter): Boolean {
        val from = Utils.convertStringToLocalDate(filter.dateFrom)
        val to = Utils.convertStringToLocalDate(filter.dateTo)
        return from.isBefore(to) || from.isEqual(to)
    }
}
