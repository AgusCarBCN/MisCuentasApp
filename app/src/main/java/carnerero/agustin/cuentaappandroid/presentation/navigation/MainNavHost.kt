package carnerero.agustin.cuentaappandroid.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.CategorySelector
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.NewEntry
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.components.EntryList
import carnerero.agustin.cuentaappandroid.presentation.ui.home.HomeScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.ProfileScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.search.SearchScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.search.TypeOfSearch
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainNavHost(
    innerNavController: NavHostController,
    modifier: Modifier
) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val settingViewModel: SettingViewModel = hiltViewModel()
    val categoriesViewModel : CategoriesViewModel=hiltViewModel()
    val entriesViewModel: EntriesViewModel = hiltViewModel()
    val accountsViewModel: AccountsViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()

    NavHost(
        navController = innerNavController,
        startDestination = Routes.Home.route,
        modifier = modifier
    ) {
        // BottomAppBar menu

        composable(Routes.Home.route) {
            HomeScreen(
                accountsViewModel,
                entriesViewModel
            )
            { innerNavController.navigate(Routes.Records.route) }
        }
        composable(Routes.Search.route) {
            SearchScreen(
                accountsViewModel,
                searchViewModel,
                entriesViewModel,
                mainViewModel,
                TypeOfSearch.SEARCH,
                { innerNavController.navigate(Routes.Records.route) })
        }
        composable(Routes.Settings.route) {
            SettingScreen(
                settingViewModel,
                mainViewModel,
                accountsViewModel,
                entriesViewModel
            )
            { innerNavController.navigate(Routes.CreateAccounts.route) }
        }
        composable(Routes.Profile.route) {
            ProfileScreen(profileViewModel)
        }
        composable(Routes.Records.route) {
            EntryList(
                entriesViewModel,
                accountsViewModel
            )

        }
        // Drawer menu
        composable(Routes.NewIncome.route) {
            CategorySelector(categoriesViewModel, CategoryType.INCOME) {
                innerNavController.navigate(Routes.NewEntry.route)
            }
        }
        composable(Routes.NewExpense.route) {
            CategorySelector(categoriesViewModel, CategoryType.EXPENSE) {
                innerNavController.navigate(Routes.NewEntry.route)
            }
        }
        composable(Routes.NewEntry.route) {
            NewEntry(mainViewModel,entriesViewModel,categoriesViewModel,accountsViewModel)
        }

    }
}