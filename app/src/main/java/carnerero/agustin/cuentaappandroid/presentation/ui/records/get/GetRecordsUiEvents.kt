package carnerero.agustin.cuentaappandroid.presentation.ui.records.get

sealed class  GetRecordsUiEvents {

    object Idle: GetRecordsUiEvents()

    data class ShowEnableByDate(val value:Boolean): GetRecordsUiEvents()
}