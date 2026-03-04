package carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords

import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsMode
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.TransactionType
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.TypeOfSearch

sealed class SearchUiEvent {
    data class ConfirmSearch(val recordMode: RecordsMode): SearchUiEvent()
    data class OnAccountSelect(val accountId: Int) : SearchUiEvent()
    data class OnTransactionSelect(val type: TransactionType) : SearchUiEvent()
    data class OnRecordDescriptionChange(val newDescription:String) : SearchUiEvent()
    data class OnShowDatePicker(
        val field: DateField,
        val visible: Boolean
    ) : SearchUiEvent()
    data class OnSelectDate(
        val field: DateField,
        val date: String
    ) : SearchUiEvent()
    data class OnAmountsChanges(
        val amountFrom: String,
        val amountTo: String
    ) : SearchUiEvent()

}