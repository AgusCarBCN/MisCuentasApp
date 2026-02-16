package carnerero.agustin.cuentaappandroid.presentation.ui.records

sealed class RecordsEffect{
    object ShowMessageRecordNotFound: RecordsEffect()
    object ShowRecords: RecordsEffect()
}