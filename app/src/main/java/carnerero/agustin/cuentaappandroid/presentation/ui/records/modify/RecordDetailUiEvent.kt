package carnerero.agustin.cuentaappandroid.presentation.ui.records.modify

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.RecordsUiEffects
import java.math.BigDecimal

sealed class RecordDetailUiEvent {
    data class OnAmountChange(val newAmount: String) : RecordDetailUiEvent()
    data class OnDateChange(val newDate: String) : RecordDetailUiEvent()

    data class OnShowDatePicker(val visible: Boolean) : RecordDetailUiEvent()
    data class OnDescriptionChange(val newDescription:String): RecordDetailUiEvent()
    data class Modify(val record: RecordDTO): RecordDetailUiEvent()
}