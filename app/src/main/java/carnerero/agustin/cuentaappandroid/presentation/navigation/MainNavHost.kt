package carnerero.agustin.cuentaappandroid.presentation.navigation


import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsFilter
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.about.AboutApp
import carnerero.agustin.cuentaappandroid.presentation.ui.about.AboutScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.about.SendEmail
import carnerero.agustin.cuentaappandroid.presentation.ui.statistics.barchart.BarChartScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.statistics.barchart.BarChartViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.CalculatorScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.CalculatorViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.changecurrency.ChangeCurrencyScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.AddAccountViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.AddAccountsScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.management.DeleteAccountsScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.management.AccountsManagementViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.management.ModifyAccountsScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.modify.ModifyAccountDetailScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.accounts.modify.ModifyAccountViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.records.categories.SelectCategoriesScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.records.modify.ModifyRecordScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.records.add.EntriesWithCheckBox
import carnerero.agustin.cuentaappandroid.presentation.ui.records.add.EntriesWithEditIcon
import carnerero.agustin.cuentaappandroid.presentation.ui.home.HomeScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.home.HomeViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.RecordScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.RecordsViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.statistics.piechart.PieChartScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.ProfileScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.records.add.AddRecordsScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.records.add.AddRecordsViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.records.categories.SelectCategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsMode
import carnerero.agustin.cuentaappandroid.presentation.ui.records.modify.RecordDetailViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.RecordSearchScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.SearchRecordsViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.SearchFilter
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SettingViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectcategories.SelectCategoriesScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.SpendingControlOptionsScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.accounts.AccountsSpendingControlViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.accounts.SpendingControlByAccountsScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.categories.CategoriesSpendingControlViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.categories.SpendingControlByCategoriesScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectaccounts.SelectAccountsScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectaccounts.SelectAccountsSpendingControlViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectcategories.SelectCategoriesSpendingControlViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.statistics.StatisticsScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.statistics.piechart.PieChartViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.transfer.TransferScreen
import carnerero.agustin.cuentaappandroid.presentation.ui.transfer.TransferViewModel
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
    profileViewModel: ProfileViewModel
) {

    val barChartViewModel: BarChartViewModel = hiltViewModel()
    val entriesViewModel: EntriesViewModel = hiltViewModel()
    val calculatorViewModel: CalculatorViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()
    val searchViewModel2: SearchRecordsViewModel = hiltViewModel()
    val createAccountViewModel: AddAccountViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val recordsViewModel: RecordsViewModel = hiltViewModel()
    val selectCategoriesViewModel: SelectCategoriesViewModel =hiltViewModel()
    val selectCategoriesSpendingControlViewModel : SelectCategoriesSpendingControlViewModel=hiltViewModel()
    val selectAccountsSpendingControlViewModel: SelectAccountsSpendingControlViewModel =hiltViewModel()
    val addRecordsViewModel: AddRecordsViewModel=hiltViewModel()
    val pieChartViewModel: PieChartViewModel =hiltViewModel()
    val transferViewModel : TransferViewModel =hiltViewModel()
    val categoriesViewModel: CategoriesSpendingControlViewModel=hiltViewModel()
    val accountsSpendingViewModelViewModel: AccountsSpendingControlViewModel=hiltViewModel()
    val managementAccountsViewModel : AccountsManagementViewModel = hiltViewModel()
    val modifyAccountDetailViewModel: ModifyAccountViewModel=hiltViewModel()
    val recordDetailViewModel : RecordDetailViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {
        // BottomAppBar menu

        composable(Routes.Home.route) {
            HomeScreen(
                homeViewModel,
                navController
            )


        }
        composable(Routes.Search.route) {
            RecordSearchScreen(
                searchViewModel2,
                RecordsMode.GET,
                navController
            )

        }
        composable(Routes.Settings.route) {
            SettingScreen(
                settingViewModel,
                mainViewModel,
                navController
            )

        }
        composable(Routes.Profile.route) {
            ProfileScreen(profileViewModel)
        }
        composable(Routes.Records.route) {
            /*EntryList(
                entriesViewModel,
                accountsViewModel
            )*/
        }
        composable(Routes.RecordsToDelete.route) {
            EntriesWithCheckBox(entriesViewModel, accountsViewModel)
        }
        composable(Routes.RecordsToModify.route) {
            EntriesWithEditIcon(entriesViewModel, accountsViewModel, navController)
        }

        // Drawer menu
        composable(Routes.NewIncome.route) {
            SelectCategoriesScreen(selectCategoriesViewModel, CategoryType.INCOME)
            {
                navController.navigate(Routes.NewEntry.route)
            }
        }
        composable(Routes.NewExpense.route) {
            SelectCategoriesScreen(selectCategoriesViewModel, CategoryType.EXPENSE) {
                navController.navigateTopLevel(Routes.NewEntry.route)
            }
        }
        composable(Routes.NewEntry.route,

        ) {
           AddRecordsScreen(selectCategoriesViewModel,
               addRecordsViewModel) {
               navController.popBackStack()
           }


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
            PieChartScreen(pieChartViewModel)
        }
        composable(Routes.BarChart.route) {
            BarChartScreen( barChartViewModel)
        }
        composable(Routes.Transfer.route) {
            TransferScreen(
                transferViewModel,
                {navController.navigate(Routes.Home.route)}

            )
            {navController.popBackStack()  }
        }
        composable(Routes.Calculator.route) {
            CalculatorScreen(calculatorViewModel, accountsViewModel)
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
            AddAccountsScreen(
                createAccountViewModel,
                navToLogin = { navController.navigate(Routes.Home.route) },
                navToBack = { navController.popBackStack() }
            )

        }
        composable(Routes.ModifyAccount.route) {
            //AccountList(mainViewModel, accountsViewModel, false, navController)
            ModifyAccountsScreen(managementAccountsViewModel,navController)
        }
        composable(Routes.DeleteAccount.route) {
            DeleteAccountsScreen(managementAccountsViewModel)
        }

        composable(Routes.DeleteRecords.route) {
            RecordSearchScreen(
                searchViewModel2,
                RecordsMode.DELETE,
                navController
            )
           /* SearchScreen(
                accountsViewModel,
                searchViewModel,
                entriesViewModel,
                TypeOfSearch.DELETE,
                navController
            )*/

        }
        composable(Routes.ModifyRecords.route) {
            RecordSearchScreen(
                searchViewModel2,
                RecordsMode.MODIFY,
                navController
            )
          /*  SearchScreen(
                accountsViewModel,
                searchViewModel,
                entriesViewModel,
                TypeOfSearch.UPDATE,
                navController
            )*/

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
            ModifyAccountDetailScreen(modifyAccountDetailViewModel,id)
           /* ModifyAccountsComponent(accountsViewModel, id)
            { navController.navigateTopLevel(Routes.Home.route) }*/
        }
        composable(
            Routes.ModifyRecordItem.route,
            arguments = listOf(navArgument("recordJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val recordJson = backStackEntry.arguments?.getString("recordJson")
            val entry = Gson().fromJson(recordJson, RecordDTO::class.java) // Deserialización
            // Obtener el parámetro
            ModifyRecordScreen(
                entry,
                recordDetailViewModel,

            )
        }
        composable(Routes.SpendingControl.route) {
            SpendingControlOptionsScreen(navController)
        }
        composable(Routes.SelectAccounts.route) {
            SelectAccountsScreen(selectAccountsSpendingControlViewModel)
        }
        composable(Routes.SpendingControlByAccount.route) {
          SpendingControlByAccountsScreen(accountsSpendingViewModelViewModel)

        }
        composable(Routes.SelectCategories.route) {
            SelectCategoriesScreen(selectCategoriesSpendingControlViewModel)

        }
        composable(Routes.SpendingControlByCategory.route) {
            //SpendingControlByCategoriesScreen(categoriesViewModel, accountsViewModel)
            SpendingControlByCategoriesScreen(categoriesViewModel)
        }

        composable(
            route = Routes.GetRecords.route,
            arguments = listOf(
                navArgument("filter") {
                    type = NavType.StringType
                },
                navArgument("accountId") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("filterJson") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("mode") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val filterName =
                backStackEntry.arguments?.getString("filter") ?: RecordsFilter.All.routeName

            val accountId =
                backStackEntry.arguments?.getInt("accountId") ?: -1

            val filterJson =
                backStackEntry.arguments?.getString("filterJson")

            val searchFilter = if (!filterJson.isNullOrEmpty()) {
                val decoded = Uri.decode(filterJson)
                Gson().fromJson(decoded, SearchFilter::class.java)
            } else null
            val modeName = backStackEntry.arguments?.getString("mode")
            val mode = RecordsMode.valueOf(modeName ?: "GET") // fallback a GET

            val filter = when (filterName) {

                RecordsFilter.Expenses.routeName ->
                    RecordsFilter.Expenses

                RecordsFilter.Incomes.routeName ->
                    RecordsFilter.Incomes

                RecordsFilter.All.routeName ->
                    RecordsFilter.All

                "RecordsByAccount" ->
                    RecordsFilter.RecordsByAccount(accountId)

                "Search" ->
                    RecordsFilter.Search(searchFilter!!)

                else ->
                    RecordsFilter.All
            }

            RecordScreen(navController,recordsViewModel, filter,mode)
        }

    }
}





