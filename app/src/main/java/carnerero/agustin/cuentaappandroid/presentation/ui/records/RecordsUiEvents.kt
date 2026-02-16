package carnerero.agustin.cuentaappandroid.presentation.ui.records

import carnerero.agustin.cuentaappandroid.presentation.ui.home.HomeUiEvents

sealed class  RecordsUiEvents {

    object Idle: RecordsUiEvents()

    data class ShowEnableByDate(val value:Boolean): RecordsUiEvents()
}