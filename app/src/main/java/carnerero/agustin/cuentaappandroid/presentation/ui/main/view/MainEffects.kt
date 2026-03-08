package carnerero.agustin.cuentaappandroid.presentation.ui.main.view

import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes

sealed class MainEffects {
    data class NavToScreen(val route: String): MainEffects()
}