package carnerero.agustin.cuentaappandroid

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import carnerero.agustin.cuentaappandroid.presentation.ui.barchart.BarChartViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.CalculatorViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.view.CreateAccountsComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.CreateProfileComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.login.LoginComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.login.LoginViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.Routes
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.view.Tutorial
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.view.TutorialViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.MisCuentasTheme
import carnerero.agustin.cuentaappandroid.utils.ObserveAsEvents
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import com.google.android.gms.ads.MobileAds

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val CHANEL_NOTIFICATION = "NotificationChannel"

    }

    private val tutorialViewModel: TutorialViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val accountViewModel: AccountsViewModel by viewModels()
    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private val entriesViewModel: EntriesViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val settingViewModel: SettingViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val barChartViewModel: BarChartViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val calculatorViewModel: CalculatorViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //Crea canal para las notificaciones
        createChannel()
        enableEdgeToEdge()

        setContent {

            val navigationController = rememberNavController()
            val toLogin by tutorialViewModel.toLogin.observeAsState(false) // Defaults to `false`
            val showTutorial by tutorialViewModel.showTutorial.observeAsState(true)
            val switchDarkTheme by settingViewModel.switchDarkTheme.observeAsState(false)

            MisCuentasTheme(darkTheme = switchDarkTheme) {
                MobileAds.initialize(this){
                    Log.d(TAG, "onCreate: initAds")
                }
                val snackbarHostState = remember {
                    SnackbarHostState()
                }

                val scope = rememberCoroutineScope()


                ObserveAsEvents(
                    flow = SnackBarController.events,
                    snackbarHostState
                ) { event ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()

                        val result = snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action?.name,
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
                        }
                    }
                }
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navigationController,
                        startDestination = if (showTutorial) Routes.Tutorial.route
                        else Routes.Login.route

                    ) {
                        composable(Routes.Tutorial.route) {
                            Tutorial(
                                tutorialViewModel,
                                navToScreen = {
                                    navigationController.navigate(
                                        if (toLogin) Routes.Login.route
                                        else Routes.CreateProfile.route
                                    )
                                },
                                modifier = Modifier.padding(innerPadding),
                            )
                        }

                        composable(Routes.CreateProfile.route) {
                            CreateProfileComponent(profileViewModel,
                                navToBackLogin = { navigationController.popBackStack() },
                                navToCreateAccounts = { navigationController.navigate(Routes.CreateAccounts.route) })
                        }

                        composable(Routes.CreateAccounts.route) {
                            CreateAccountsComponent(accountViewModel,categoriesViewModel, navToLogin = {
                                navigationController.navigate(Routes.Login.route)
                            },
                                navToBack = { navigationController.popBackStack() }
                            )

                        }
                        composable(Routes.Login.route) {
                            LoginComponent(
                                loginViewModel,
                                modifier = Modifier.fillMaxSize(),
                                navToMain = {
                                    navigationController.navigate(Routes.Home.route)
                                }
                            )
                        }
                        composable(Routes.Home.route) {
                            MainScreen(
                                mainViewModel,
                                accountViewModel,
                                categoriesViewModel,
                                profileViewModel,
                                settingViewModel,
                                entriesViewModel,
                                searchViewModel,
                                calculatorViewModel,
                                barChartViewModel,
                                navToCreateAccounts = {
                                    navigationController.navigate(Routes.CreateAccounts.route)
                                }

                            )

                        }


                    }

                }
            }
        }
    }
    private fun createChannel() {
        val channel = NotificationChannel(
            CHANEL_NOTIFICATION,
            "channelAlert",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
    private fun enableEdgeToEdge() {
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        // Ocultar la barra de estado y la barra de navegación
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
        // Dejar visible la barra de navegación
        windowInsetsController.show(WindowInsetsCompat.Type.navigationBars())
    }
}


