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
import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
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
import carnerero.agustin.cuentaappandroid.presentation.ui.changecurrency.ChangeCurrencyScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.view.CreateAccountsComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.CategorySelector
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.ModifyEntry
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.NewEntry
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.components.EntriesWithCheckBox
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.components.EntriesWithEditIcon
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.components.EntryList
import carnerero.agustin.cuentaappandroid.presentation.ui.home.HomeScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.piechart.PieChartScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.ProfileScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.search.SearchScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.search.TypeOfSearch
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.AccountList
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.ModifyAccountsComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.SelectAccountScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.SelectCategoriesScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.SpendingControlByAccountsScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.SpendingControlByCategoriesScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.SpendingControlOptionsScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.stadistics.StatisticsScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.transfer.Transfer
import carnerero.agustin.cuentaappandroid.utils.navigateTopLevel
import com.google.gson.Gson

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
    val calculatorViewModel: CalculatorViewModel = hiltViewModel()
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
                TypeOfSearch.SEARCH,
                navController
            )
        }
        composable(Routes.Settings.route) {
            SettingScreen(
                settingViewModel,
                mainViewModel,
                accountsViewModel,
                entriesViewModel,
                navController
            )

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
        composable(Routes.RecordsToDelete.route) {
            EntriesWithCheckBox(entriesViewModel,accountsViewModel)
        }
        composable(Routes.RecordsToModify.route) {
            EntriesWithEditIcon(entriesViewModel,accountsViewModel,navController)
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
        composable(Routes.Transfer.route) {
            Transfer(
                accountsViewModel,
                entriesViewModel
            )
            { navController.navigate(Routes.Home.route) }
        }
        composable(Routes.Calculator.route) {
            CalculatorUI(calculatorViewModel)
        }
        composable(Routes.About.route) {
            AboutScreen({ navController.navigate(Routes.AboutDescription.route) })
            {
                navController.navigate(Routes.Email.route)
            }
        }
        composable(Routes.AboutDescription.route) {
            AboutApp()
        }
        composable(Routes.Email.route) {
            SendEmail()
        }


        composable(Routes.AddAccount.route) {
            CreateAccountsComponent(
                accountsViewModel,
                categoriesViewModel,
                navToLogin = { navController.navigate(Routes.Login.route) },
                navToBack = { navController.popBackStack() }
            )
        }
        composable(Routes.ModifyAccount.route) {
            AccountList(mainViewModel, accountsViewModel, false, navController)
        }
        composable(Routes.DeleteAccount.route) {
            AccountList(mainViewModel, accountsViewModel, true, navController)
        }

        composable(Routes.DeleteRecords.route) {
            SearchScreen(
                accountsViewModel,
                searchViewModel,
                entriesViewModel,
                TypeOfSearch.DELETE,
                navController
            )

        }
        composable(Routes.ModifyRecords.route) {
            SearchScreen(
                accountsViewModel,
                searchViewModel,
                entriesViewModel,
                TypeOfSearch.UPDATE,
                navController
            )

        }
        composable(Routes.ChangeCurrency.route) {
            ChangeCurrencyScreen(accountsViewModel, entriesViewModel)
            { navController.navigate(Routes.Home.route) }
        }

        composable(
            Routes.ModifyAccountItem.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            ModifyAccountsComponent(accountsViewModel, id)
            { navController.navigateTopLevel(Routes.Home.route) }
        }
        composable(
            Routes.ModifyRecordItem.route,
            arguments = listOf(navArgument("recordJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val recordJson = backStackEntry.arguments?.getString("recordJson")
            val entry = Gson().fromJson(recordJson, EntryDTO::class.java) // Deserialización
            // Obtener el parámetro
            ModifyEntry(entry,entriesViewModel, searchViewModel,accountsViewModel ) {navController.navigateTopLevel(Routes.Home.route) }
        }
        composable(Routes.SpendingControl.route) {
            SpendingControlOptionsScreen(navController)
        }
        composable(Routes.SelectAccounts.route) {
            SelectAccountScreen(accountsViewModel,searchViewModel)
        }
        composable(Routes.SpendingControlByAccount.route){
            SpendingControlByAccountsScreen(accountsViewModel)
        }
        composable(Routes.SelectCategories.route) {
            SelectCategoriesScreen(categoriesViewModel, searchViewModel)
        }
        composable(Routes.SpendingControlByCategory.route){
            SpendingControlByCategoriesScreen(categoriesViewModel,accountsViewModel)
        }
    }
}



