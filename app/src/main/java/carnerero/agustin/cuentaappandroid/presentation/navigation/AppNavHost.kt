package carnerero.agustin.cuentaappandroid.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.barchart.BarChartViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.CalculatorViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.view.CreateAccountsComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.CreateProfileComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.ModifyEntry
import carnerero.agustin.cuentaappandroid.presentation.ui.login.LoginComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.login.LoginViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.Routes
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.view.Tutorial
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.view.TutorialViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavHost(navController: NavHostController,
               modifier: Modifier) {

    val mainViewModel : MainViewModel=hiltViewModel()
    val tutorialViewModel: TutorialViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel=hiltViewModel()
    val accountViewModel : AccountsViewModel=hiltViewModel()
    val categoriesViewModel : CategoriesViewModel=hiltViewModel()
    val settingViewModel : SettingViewModel=hiltViewModel()
    val loginViewModel : LoginViewModel=hiltViewModel()
    val entriesViewModel : EntriesViewModel = hiltViewModel()
    val calculatorViewModel : CalculatorViewModel=hiltViewModel()
    val searchViewModel : SearchViewModel=hiltViewModel()
    val barChartViewModel : BarChartViewModel=hiltViewModel()

    val toLogin by tutorialViewModel.toLogin.observeAsState(false) // Defaults to `false`
    val showTutorial by tutorialViewModel.showTutorial.observeAsState(true)
    NavHost(
        navController = navController,
        startDestination = if (showTutorial) Routes.Tutorial.route
        else Routes.Login.route

    ) {
        composable(Routes.Tutorial.route) {
            Tutorial(
                tutorialViewModel,
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
            CreateProfileComponent(profileViewModel,
                navToBackLogin = { navController.popBackStack() },
                navToCreateAccounts = { navController.navigate(Routes.CreateAccounts.route) })
        }

        composable(Routes.CreateAccounts.route) {
            CreateAccountsComponent(accountViewModel,categoriesViewModel, navToLogin = {
                navController.navigate(Routes.Login.route)
            },
                navToBack = { navController.popBackStack() }
            )

        }
        composable(Routes.Login.route) {
            LoginComponent(
                loginViewModel,
                modifier = Modifier.fillMaxSize(),
                navToMain = {
                    navController.navigate(Routes.Home.route)
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
                    navController.navigate(Routes.CreateAccounts.route)
                }
            )
        }
    }
}



