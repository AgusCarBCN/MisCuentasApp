package carnerero.agustin.cuentaappandroid.presentation.ui.records.get

sealed class RecordsEffect{
    object ShowMessageRecordNotFound: RecordsEffect()
    object ShowRecords: RecordsEffect()
}