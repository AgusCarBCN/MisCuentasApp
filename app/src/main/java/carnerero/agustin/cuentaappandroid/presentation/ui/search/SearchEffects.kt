package carnerero.agustin.cuentaappandroid.presentation.ui.search

import carnerero.agustin.cuentaappandroid.presentation.ui.home.HomeEffects

sealed class SearchEffects {
    object Idle: SearchEffects()
    object NavToRecordsScreen: SearchEffects()
}