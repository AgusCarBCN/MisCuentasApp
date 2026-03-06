package carnerero.agustin.cuentaappandroid.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.AddAccountViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.AddAccountsScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.createprofile.CreateProfileScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.login.LoginScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.splash.SplashScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.view.Tutorial
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.view.TutorialViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    settingViewModel: SettingViewModel,
    tutorialViewModel: TutorialViewModel,
    profileViewModel: ProfileViewModel,
    modifier: Modifier
) {
    val createAccountViewModel: AddAccountViewModel = androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(tutorialViewModel,
                {navController.navigate(Routes.Tutorial.route)},
                {navController.navigate(Routes.Login.route)})
        }
        composable(Routes.Tutorial.route) {
            Tutorial(
                tutorialViewModel,
                navToLogin = {navController.navigate(Routes.Login.route)},
            {navController.navigate(Routes.CreateProfile.route)})
        }

        composable(Routes.CreateProfile.route) {
            CreateProfileScreen(
                profileViewModel,
                navToCreateAccounts = { navController.navigate(Routes.CreateAccounts.route) })
        }

        composable(Routes.CreateAccounts.route) {
            AddAccountsScreen(
                createAccountViewModel,
                navToLogin = {navController.navigate(Routes.Login.route)},
                navToBack = { navController.popBackStack() }
            )
        }
        composable(Routes.Login.route) {
            LoginScreen(
                modifier = Modifier.fillMaxSize(),
                navToMainScreen = {
                    navController.navigate(Routes.Main.route)
                }
            )
        }
        composable(Routes.Main.route) {
            MainScreen(
                mainViewModel,
                profileViewModel ,
                settingViewModel
            )
        }
    }
}



