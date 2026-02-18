package carnerero.agustin.cuentaappandroid.presentation.ui.search

import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.RecordsUiEvents
import carnerero.agustin.cuentaappandroid.presentation.ui.search.model.TransactionType

sealed class SearchUiEvent {
    object ConfirmSearch: SearchUiEvent()
    data class OnAccountSelect(val accountId: Int) : SearchUiEvent()
    data class OnTransactionSelect(val type: TransactionType) : SearchUiEvent()
    data class OnRecordDescriptionChange(val newDescription:String) : SearchUiEvent()
    data class OnShowDatePicker(
        val show: Boolean,
        val isFromDate: Boolean
    ) : SearchUiEvent()
    data class OnSelectDate(
        val date: String,
        val isFromDate: Boolean
    ) : SearchUiEvent()
    data class OnAmountChanges(
        val amountFrom: String,
        val amountTo: String
    ) : SearchUiEvent()

}