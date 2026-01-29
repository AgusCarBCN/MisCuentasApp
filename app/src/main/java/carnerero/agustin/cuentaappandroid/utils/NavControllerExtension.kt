package carnerero.agustin.cuentaappandroid.utils

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun NavController.navigateSingleTop(route: String) {
    // Navega a la ruta especificada (por ejemplo, "home" o "settings")
    navigate(route) {
        // Borra del stack de navegación todas las pantallas hasta el destino de inicio
        // Esto evita que se apilen múltiples pantallas al navegar entre tabs o BottomBar
        popUpTo(graph.findStartDestination().id) {
            // Guarda el estado de la pantalla que se está quitando del stack
            // Así, si volvemos a esa pantalla, se restaurará el scroll, inputs, etc.
            saveState = true
        }
        // Evita crear múltiples instancias de la misma pantalla en el stack
        // Por ejemplo, si ya estamos en Home y navegamos a Home de nuevo, no se duplicará
        launchSingleTop = true
        // Restaura el estado de la pantalla si existía en el stack y se había guardado con saveState
        // Esto permite que los usuarios vean la pantalla tal como la dejaron
        restoreState = true
    }
}
