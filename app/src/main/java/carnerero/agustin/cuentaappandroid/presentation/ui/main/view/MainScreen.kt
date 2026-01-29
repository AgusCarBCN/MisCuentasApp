package carnerero.agustin.cuentaappandroid.presentation.ui.main.view


import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.IconComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.UserImage
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.notification.NotificationAccountObserver
import carnerero.agustin.cuentaappandroid.notification.NotificationCategoriesObserver
import carnerero.agustin.cuentaappandroid.notification.NotificationService
import carnerero.agustin.cuentaappandroid.notification.RequestNotificationPermissionDialog
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelDialog
import carnerero.agustin.cuentaappandroid.presentation.ui.main.menu.components.BottomMyAccountsBar
import carnerero.agustin.cuentaappandroid.presentation.navigation.MainNavHost
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.model.OptionItem
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.presentation.ui.main.menu.components.DrawerMyAccountsContent
import carnerero.agustin.cuentaappandroid.presentation.ui.main.menu.components.TopMyAccountsBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    accountsViewModel: AccountsViewModel,
    categoriesViewModel: CategoriesViewModel,
    profileViewModel: ProfileViewModel,
    settingViewModel: SettingViewModel

) {

    // NavController para manejar la navegación entre pantallas
    val innerNavController = rememberNavController()
    // Observa la entrada actual del back stack (la pantalla activa) como un estado observable
    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val context = LocalContext.current
    // Verifica si el contexto es una actividad
    val activity = context as? Activity
    val notificationService = NotificationService(context)
    val enableNotifications by settingViewModel.switchNotifications.observeAsState(false)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedScreen by mainViewModel.selectedScreen.collectAsState()

    if (enableNotifications) {
        NotificationCategoriesObserver(
            categoriesViewModel,
            accountsViewModel,
            notificationService
        )
        NotificationAccountObserver(
            accountsViewModel,
            notificationService
        )
    }

    val userName by profileViewModel.name.observeAsState("")
    val showExitDialog by mainViewModel.showExitDialog.collectAsState()
    val title by mainViewModel.title.observeAsState(R.string.greeting)

    // Usar LaunchedEffect para cerrar el drawer cuando cambia la pantalla seleccionada
    LaunchedEffect(key1 = navBackStackEntry) {
        if (drawerState.isOpen) {
            drawerState.close() // Cierra el drawer cuando se selecciona una opción
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMyAccountsContent(
                mainViewModel,
                profileViewModel,
                innerNavController
            )
        },
        scrimColor = Color.Transparent,
        content = {
            // Main content goes here
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                {
                    TopMyAccountsBar(
                        scope, drawerState, title,
                        (if (selectedScreen == IconOptions.HOME) userName else "")
                    )
                },
                {
                    BottomMyAccountsBar(
                        mainViewModel,
                        innerNavController
                    )
                },
                containerColor = LocalCustomColorsPalette.current.backgroundPrimary
            ) { innerPadding ->
                RequestNotificationPermissionDialog(mainViewModel)
                MainNavHost(innerNavController, Modifier.padding(innerPadding))
            }
        }
    )
    ModelDialog(R.string.exitapp,
        R.string.exitinfo,
        showDialog = showExitDialog,
        onConfirm = {
            activity?.finish()
        },
        onDismiss = {
            mainViewModel.showExitDialog(false)
            mainViewModel.selectScreen(IconOptions.HOME)
        })
}







