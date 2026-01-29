package carnerero.agustin.cuentaappandroid.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.components.EntryList
import carnerero.agustin.cuentaappandroid.presentation.ui.home.HomeScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.ProfileScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.search.SearchScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.search.TypeOfSearch
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainNavHost(innerNavController: NavHostController,
               modifier: Modifier) {
    val entriesViewModel : EntriesViewModel = hiltViewModel()
    val accountsViewModel: AccountsViewModel = hiltViewModel()
    val searchViewModel : SearchViewModel = hiltViewModel()
    NavHost(
        navController = innerNavController,
        startDestination = Routes.Home.route,
        modifier = modifier
    ) {
       // BottomAppBar menu

        composable(Routes.Home.route) {
            HomeScreen(
                accountsViewModel,
                entriesViewModel,
                 {innerNavController.navigate(Routes.Records.route)})
        }
        composable(Routes.Search.route) {
            SearchScreen(typeOfSearch = TypeOfSearch.SEARCH)
        }
        composable(Routes.Settings.route) {
            SettingScreen { innerNavController.navigate(Routes.CreateAccounts.route) }
        }
        composable(Routes.Profile.route) {
            ProfileScreen()
        }
        composable(Routes.Records.route) {
            EntryList(entriesViewModel)

        }
        // Drawer menu

    }
}