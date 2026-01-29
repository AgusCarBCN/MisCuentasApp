package carnerero.agustin.cuentaappandroid.presentation.ui.main.menu.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel


@Composable
fun BottomMyAccountsBar(mainViewModel: MainViewModel,
                 navController: NavHostController) {
    val bottomBarItems = listOf(
        Routes.Home,
        Routes.Search,
        Routes.Settings,
        Routes.Profile
    )
    // Observa la entrada actual del back stack (la pantalla activa) como un estado observable
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // Obtiene el destino actual de navegación (la ruta o identificador de la pantalla activa)
    navBackStackEntry?.destination

    // Inicializa con el primer ícono
    BottomAppBar(
        modifier = Modifier
            .height(80.dp)
            .background( LocalCustomColorsPalette.current.backgroundPrimary)
            .clip(RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp))
            ,
        containerColor = LocalCustomColorsPalette.current.barBackground,
        contentColor = LocalCustomColorsPalette.current.textColor,
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                bottomBarItems.forEach { item ->
                    IconBottomBarApp(item.labelResource!!, item.iconResource!!) {
                        mainViewModel.updateTitle(item.labelResource)
                        // Navega a la ruta especificada (por ejemplo, "home" o "settings")
                        navController.navigate(item.route) {
                            // Borra del stack de navegación todas las pantallas hasta el destino de inicio
                            // Esto evita que se apilen múltiples pantallas al navegar entre tabs o BottomBar
                            popUpTo(navController.graph.findStartDestination().id) {
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
                    }
                }
        },
        tonalElevation = 22.dp
    )
}


@Composable
private fun IconBottomBarApp(
    label: Int,
    resourceIcon: Int,
    onClickButton: () -> Unit
) {
// Creamos una fuente de interacciones para el IconButton
    val interactionSource = remember { MutableInteractionSource() }
    // Detectamos si el botón está presionado

    val isPressed by interactionSource.collectIsPressedAsState()

    IconButton(
        onClick = onClickButton,
        interactionSource = interactionSource
    ) {
        IconComponent(isPressed, label, resourceIcon)
    }

}


@Composable
private fun IconComponent(isPressed: Boolean, label: Int, iconResource: Int) {
    val labelString = stringResource(label)
    val scale = animateFloatAsState(
        targetValue = if (isPressed) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "icon"
    )
    val indicatorColor by animateColorAsState(
        targetValue = if (isPressed) {
            LocalCustomColorsPalette.current.iconColor
        } else {
            LocalCustomColorsPalette.current.iconColor
        },
        label = "indicator color",
        animationSpec = tween(
            durationMillis = 2000, // Duración de la animación
            easing = LinearOutSlowInEasing // Controla la velocidad de la transición
        )
    )

    Icon(
        painter = painterResource(id = iconResource),
        contentDescription = "indicator",
        tint = indicatorColor,
        modifier = Modifier
            .scale(scale.value)
            .size(28.dp)
            .semantics {
                // Agregamos un contentDescription dinámico
                contentDescription = labelString
            }
    )
}



