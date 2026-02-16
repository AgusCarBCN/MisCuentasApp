package carnerero.agustin.cuentaappandroid.presentation.ui.home

sealed class HomeEffects {

    object Idle: HomeEffects()
    object NavToRecordsScreen: HomeEffects()

}