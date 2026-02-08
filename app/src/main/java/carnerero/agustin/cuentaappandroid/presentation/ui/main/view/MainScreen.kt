package carnerero.agustin.cuentaappandroid.presentation.ui.main.view


import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.admob.AdmobBanner
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
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.main.menu.components.DrawerMyAccountsContent
import carnerero.agustin.cuentaappandroid.presentation.ui.main.menu.components.TopMyAccountsBar
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp


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
    val isLanScape=orientation== OrientationApp.Landscape
    mainViewModel.isLandScape(isLanScape)
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


    val showExitDialog by mainViewModel.showExitDialog.collectAsState()
    val title by mainViewModel.title.observeAsState(R.string.home)
    val isPortrait=orientation== OrientationApp.Portrait
    // Usar LaunchedEffect para cerrar el drawer cuando cambia la pantalla seleccionada
    LaunchedEffect(key1 = navBackStackEntry,isPortrait) {
        if (drawerState.isOpen) {
            drawerState.close() // Cierra el drawer cuando se selecciona una opción
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState ,
        drawerContent = {
            if(isPortrait){
            DrawerMyAccountsContent(
                mainViewModel,
                profileViewModel,
                innerNavController
            )}
        },
        scrimColor = Color.Transparent,
        content = {
            // Main content goes here
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                {
                    if(isPortrait){TopMyAccountsBar(
                        scope,drawerState,title
                    )}
                },
                {
                    if(isPortrait){
                    BottomMyAccountsBar(
                        mainViewModel,
                        innerNavController
                    )}
                },
                containerColor = colors.backgroundPrimary
            ) { innerPadding ->
                RequestNotificationPermissionDialog(mainViewModel)
                Column(
                    Modifier.padding(innerPadding)
                )
                 {
                    //AdmobBanner()
                    MainNavHost(
                        innerNavController,
                        settingViewModel,
                        categoriesViewModel,
                        mainViewModel,
                        accountsViewModel,
                        profileViewModel
                    )
                }
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







