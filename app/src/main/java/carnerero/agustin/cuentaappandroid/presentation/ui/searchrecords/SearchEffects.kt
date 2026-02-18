package carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords

sealed class SearchEffects {
    object Idle: SearchEffects()
    object NavToRecordsScreen: SearchEffects()
}