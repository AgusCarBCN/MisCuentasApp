package carnerero.agustin.cuentaappandroid.presentation.ui.records.get

sealed class  RecordsUiEvents {

    object Idle: RecordsUiEvents()

    data class ShowEnableByDate(val value:Boolean): RecordsUiEvents()
}