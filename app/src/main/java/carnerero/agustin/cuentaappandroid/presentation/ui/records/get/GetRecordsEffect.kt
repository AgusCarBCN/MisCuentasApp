package carnerero.agustin.cuentaappandroid.presentation.ui.records.get

sealed class GetRecordsEffect{
    object ShowMessageRecordNotFound: GetRecordsEffect()
    object ShowRecords: GetRecordsEffect()
}