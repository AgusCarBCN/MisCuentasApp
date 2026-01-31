package carnerero.agustin.cuentaappandroid.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.view.CreateAccountsComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.CreateProfileComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.login.LoginComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.view.Tutorial
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.view.TutorialViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavHost(navController: NavHostController,
               mainViewModel: MainViewModel,
               accountsViewModel: AccountsViewModel,
               settingViewModel: SettingViewModel,
               tutorialViewModel: TutorialViewModel,
               categoriesViewModel: CategoriesViewModel,
               profileViewModel: ProfileViewModel
               ) {

    val toLogin by tutorialViewModel.toLogin.observeAsState(false) // Defaults to `false`
    val showTutorial by tutorialViewModel.showTutorial.observeAsState(true)
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(navController,showTutorial)
        }
        composable(Routes.Tutorial.route) {
            Tutorial(
                tutorialViewModel,
                navToScreen = {
                    navController.navigate(
                        if (toLogin) Routes.Login.route
                        else Routes.CreateProfile.route
                    ) {
                        popUpTo(Routes.Tutorial.route) {
                            inclusive = true
                        }
                    }
                })
        }

        composable(Routes.CreateProfile.route) {
            CreateProfileComponent(
                profileViewModel,
                navToBackLogin = { navController.popBackStack() },
                navToCreateAccounts = { navController.navigate(Routes.CreateAccounts.route) })
        }

        composable(Routes.CreateAccounts.route) {
            CreateAccountsComponent(
                accountsViewModel,
                categoriesViewModel,
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
                accountsViewModel,
                categoriesViewModel,
                profileViewModel ,
                settingViewModel
            )
        }
    }
}



