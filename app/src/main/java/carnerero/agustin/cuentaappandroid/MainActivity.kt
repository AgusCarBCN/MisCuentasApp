package carnerero.agustin.cuentaappandroid

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
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
import androidx.navigation.compose.rememberNavController
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingViewModel
import carnerero.agustin.cuentaappandroid.presentation.navigation.AppNavHost
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

    private val settingViewModel: SettingViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val accountsViewModel: AccountsViewModel by viewModels()
    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val tutorialViewModel: TutorialViewModel by viewModels ()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //Crea canal para las notificaciones
        createChannel()
        enableEdgeToEdge()

        setContent {

            val navigationController = rememberNavController()

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
                    AppNavHost(navigationController,
                        mainViewModel,
                        accountsViewModel,
                        settingViewModel,
                        tutorialViewModel,
                        categoriesViewModel,
                        profileViewModel,
                        modifier = Modifier.padding(innerPadding))

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


