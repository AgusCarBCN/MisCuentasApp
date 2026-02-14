package carnerero.agustin.cuentaappandroid.presentation.ui.home

import androidx.annotation.StringRes

sealed class HomeEffects {

    object Idle: HomeEffects()
    object NavToShowRecordsScreen: HomeEffects()

    object NavToBack: HomeEffects()

    object  ShowNoEntriesMessage: HomeEffects()
}