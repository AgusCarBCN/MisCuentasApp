package carnerero.agustin.cuentaappandroid.presentation.ui.main.view

import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import okhttp3.Route

sealed class MainUserEvents {

    object CloseExitDialog : MainUserEvents()
    object OpenExitDialog : MainUserEvents()
    data class UpdateTitle(val newTitle: Int) : MainUserEvents()
    data class OnClickOption(val route: Routes) : MainUserEvents()
}