package carnerero.agustin.cuentaappandroid.presentation.ui.main.view

import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes

sealed class MainUserEvents {

    object CloseExitDialog : MainUserEvents()
    object OpenExitDialog : MainUserEvents()
    data class OnClickOption(val route: Routes) : MainUserEvents()
}