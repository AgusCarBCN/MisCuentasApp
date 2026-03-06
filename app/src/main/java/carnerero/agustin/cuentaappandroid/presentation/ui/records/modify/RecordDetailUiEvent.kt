package carnerero.agustin.cuentaappandroid.presentation.ui.records.modify

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO

sealed class RecordDetailUiEvent {
    data class OnAmountChange(val newAmount: String) : RecordDetailUiEvent()
    data class OnDateChange(val newDate: String) : RecordDetailUiEvent()

    data class OnShowDatePicker(val visible: Boolean) : RecordDetailUiEvent()
    data class OnDescriptionChange(val newDescription:String): RecordDetailUiEvent()
    data class Modify(val record: RecordDTO): RecordDetailUiEvent()
}