package carnerero.agustin.cuentaappandroid.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.text.font.Typeface
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.view.CreateAccountsComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.CreateProfileComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.login.LoginComponent
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import carnerero.agustin.cuentaappandroid.presentation.ui.home.HomeScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.ProfileScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.search.SearchScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.search.TypeOfSearch
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.view.Tutorial
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.view.TutorialViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavHost(navController: NavHostController,
               modifier: Modifier) {
    val tutorialViewModel: TutorialViewModel = hiltViewModel()
    val mainViewModel: MainViewModel =hiltViewModel()
    val toLogin by tutorialViewModel.toLogin.observeAsState(false) // Defaults to `false`
    val showTutorial by tutorialViewModel.showTutorial.observeAsState(true)
    NavHost(
        navController = navController,
        startDestination = if (showTutorial) Routes.Tutorial.route
        else Routes.Login.route
    ) {
        composable(Routes.Tutorial.route) {
            Tutorial(
                navToScreen = {
                    navController.navigate(
                        if (toLogin) Routes.Login.route
                        else Routes.CreateProfile.route
                    )
                },
                modifier = modifier
            )
        }

        composable(Routes.CreateProfile.route) {
            CreateProfileComponent(
                navToBackLogin = { navController.popBackStack() },
                navToCreateAccounts = { navController.navigate(Routes.CreateAccounts.route) })
        }

        composable(Routes.CreateAccounts.route) {
            CreateAccountsComponent(
                navToLogin = {navController.navigate(Routes.Login.route)},
                navToBack = { navController.popBackStack() }
            )
        }
        composable(Routes.Login.route) {
            LoginComponent(
                modifier = Modifier.fillMaxSize(),
                navToMain = {
                    navController.navigate(Routes.Main.route)
                }
            )
        }
        composable(Routes.Main.route) {
            MainScreen(
                mainViewModel,
              navController=navController
            )
        }
    }
}



