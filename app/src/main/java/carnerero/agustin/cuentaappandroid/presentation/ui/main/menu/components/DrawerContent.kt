package carnerero.agustin.cuentaappandroid.presentation.ui.main.menu.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.UserImage
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.model.OptionItem

@Composable
public fun DrawerMyAccountsContent(
    viewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
    navController: NavController
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(top = 62.dp)
            .background(color = Color.Transparent)

    ) {
        val drawerMenuManageItems=listOf(
            Routes.NewIncome,
            Routes.NewExpense,
            Routes.Transfer,
            Routes.Statistics,
            Routes.SpendingControl,
            Routes.Calculator
        )
        val about=Routes.About
        HeadDrawerMenu(profileViewModel)
        Column(
            modifier = Modifier
                .background(LocalCustomColorsPalette.current.drawerColor)
        ) {
            TitleOptions(R.string.management)

            drawerMenuManageItems.forEach { item ->
                ClickableRow(OptionItem(item.labelResource!!, item.iconResource!!), onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
            TitleOptions(R.string.aboutapp)
            ClickableRow(OptionItem(about.labelResource!!, about.iconResource!!), onClick = {
                navController.navigate(about.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })
            ClickableRow(
                OptionItem(R.string.exitapp, R.drawable.exitapp),
                onClick = {
                    viewModel.showExitDialog(true)
                })
        }
    }
}

//Implementacion de la cabecerera del menu desplegable izquierda
@Composable
fun HeadDrawerMenu(profileViewModel: ProfileViewModel) {

    val selectedImageUriSaved by profileViewModel.selectedImageUriSaved.observeAsState(null)

    profileViewModel.loadImageUri()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalCustomColorsPalette.current.headDrawerColor),
        Arrangement.SpaceEvenly,
        Alignment.CenterVertically


    ) {
        Box(modifier = Modifier.weight(0.4f)) {
            selectedImageUriSaved?.let { UserImage(it, 80) }
        }

    }

}


// Implementación de Row clickable para cada opción del menú de la izquierda
@Composable
private fun ClickableRow(
    option: OptionItem,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    // Detectar si la fila está presionada
    val isPressed by interactionSource.collectIsPressedAsState()

    // Definir el color de fondo dependiendo si está presionado o no
    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) LocalCustomColorsPalette.current.rowDrawerPressed
        else LocalCustomColorsPalette.current.drawerColor,
        label = "row clickable color"
    )
    val contentRowColor by animateColorAsState(
        targetValue = if (isPressed) LocalCustomColorsPalette.current.invertedTextColor
        else LocalCustomColorsPalette.current.contentBarColor,
        label = "row clickable color"
    )
    // Fila clickable con color de fondo animado
    Row(
        modifier = Modifier
            .clickable(
                onClick = { onClick() },
                interactionSource = interactionSource,
                indication = null // Esto elimina el efecto predeterminado de ripple
            )
            .background(backgroundColor) // Aplicar el color de fondo dinámico
            .padding(16.dp) // Agregar padding
            .fillMaxWidth(), // Ocupar todo el ancho disponible
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(id = option.resourceIconItem),
            contentDescription = "Side menu",
            modifier = Modifier.size(28.dp),
            tint = contentRowColor
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(id = option.resourceTitleItem),
            color = contentRowColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TitleOptions(title: Int) {

    Text(
        text = stringResource(id = title),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = LocalCustomColorsPalette.current.contentDrawerColor,
        //fontSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_body_large).toSp() },
        fontWeight = FontWeight.Bold,
        fontSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_title_medium).toSp() }

    )
}