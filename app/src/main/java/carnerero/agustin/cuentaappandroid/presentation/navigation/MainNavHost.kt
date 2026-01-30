package carnerero.agustin.cuentaappandroid.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.about.AboutApp
import carnerero.agustin.cuentaappandroid.presentation.ui.about.AboutScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.about.SendEmail
import carnerero.agustin.cuentaappandroid.presentation.ui.barchart.BarChartScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.barchart.BarChartViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.CalculatorUI
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.CalculatorViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.CategorySelector
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.NewEntry
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.components.EntryList
import carnerero.agustin.cuentaappandroid.presentation.ui.home.HomeScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.piechart.PieChartScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.ProfileScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.search.SearchScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.search.TypeOfSearch
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.stadistics.StatisticsScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.transfer.Transfer
import carnerero.agustin.cuentaappandroid.utils.navigateTopLevel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainNavHost(
    navController: NavHostController,
    settingViewModel: SettingViewModel,
    categoriesViewModel: CategoriesViewModel,
    mainViewModel: MainViewModel,
    accountsViewModel: AccountsViewModel,
    profileViewModel: ProfileViewModel,
    modifier: Modifier
) {

    val barChartViewModel: BarChartViewModel = hiltViewModel()
    val entriesViewModel: EntriesViewModel = hiltViewModel()
    val calculatorViewModel: CalculatorViewModel=hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Home.route,
        modifier = modifier
    ) {
        // BottomAppBar menu

        composable(Routes.Home.route) {
            HomeScreen(
                accountsViewModel,
                entriesViewModel
            )
            { navController.navigate(Routes.Records.route) }
        }
        composable(Routes.Search.route) {
            SearchScreen(
                accountsViewModel,
                searchViewModel,
                entriesViewModel,
                mainViewModel,
                TypeOfSearch.SEARCH,
                { navController.navigate(Routes.Records.route) })
        }
        composable(Routes.Settings.route) {
            SettingScreen(
                settingViewModel,
                mainViewModel,
                accountsViewModel,
                entriesViewModel
            )
            { navController.navigate(Routes.CreateAccounts.route) }
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
                navController.navigate(Routes.NewEntry.route)
            }
        }
        composable(Routes.NewExpense.route) {
            CategorySelector(categoriesViewModel, CategoryType.EXPENSE) {
                navController.navigate(Routes.NewEntry.route)
            }
        }
        composable(Routes.NewEntry.route) {
            NewEntry(
                entriesViewModel,
                categoriesViewModel,
                accountsViewModel
            )
            { navController.popBackStack() }
        }
        composable(Routes.Statistics.route) {
            StatisticsScreen(
                navToBarChart = {
                    navController.navigateTopLevel(Routes.BarChart.route)

                }) {
                navController.navigateTopLevel(Routes.PieChart.route)
            }
        }
        composable(Routes.PieChart.route) {
            PieChartScreen(entriesViewModel, accountsViewModel, searchViewModel)
        }
        composable(Routes.BarChart.route) {
            BarChartScreen(accountsViewModel, barChartViewModel, settingViewModel)
        }
        composable(Routes.Transfer.route){
            Transfer(accountsViewModel,
                entriesViewModel)
            {navController.navigate(Routes.Home.route)}
        }
        composable(Routes.Calculator.route){
            CalculatorUI(calculatorViewModel)
        }
        composable(Routes.About.route){
            AboutScreen({navController.navigate(Routes.AboutDescription.route)})
            {navController.navigate(Routes.Email.route)
            }
        }
        composable(Routes.AboutDescription.route) {
            AboutApp()
        }
        composable(Routes.Email.route){
            SendEmail()
        }
    }
}