package carnerero.agustin.cuentaappandroid.presentation.ui.records.get

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO

sealed class  RecordsUiEvents {

    object Idle: RecordsUiEvents()

    object CloseDialogDelete: RecordsUiEvents()

    object OpenDialogDelete: RecordsUiEvents()

    data class ShowEnableByDate(val value:Boolean): RecordsUiEvents()

    data class OnEditRecord(val record: RecordDTO) : RecordsUiEvents()

    data class OnDeleteRecord(val record: RecordDTO) : RecordsUiEvents()

}