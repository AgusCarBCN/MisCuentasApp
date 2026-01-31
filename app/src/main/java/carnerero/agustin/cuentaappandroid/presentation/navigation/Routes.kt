package carnerero.agustin.cuentaappandroid.presentation.navigation

import carnerero.agustin.cuentaappandroid.R

sealed class Routes(
    val route: String,
    val labelResource: Int? = null,
    val iconResource: Int? = null
) {

    data object Splash:Routes(route="gate")

    data object Main : Routes("main")
    data object CreateProfile : Routes("create profile", R.string.profile, R.drawable.profile)
    data object CreateAccounts : Routes("create accounts", R.string.createAccount, R.drawable.add)
    data object Tutorial : Routes("tutorial")
    data object Login : Routes("login")  // Add more routes as needed

    // Bottom menu items

    data object Home : Routes("home", R.string.home, R.drawable.home)

    // data object Search : Routes("search",R.string.search,R.drawable.search)

    data object Search: Routes("search",R.string.search,R.drawable.search)
    data object Settings : Routes("settings", R.string.settings, R.drawable.settings)

    data object Profile : Routes("profile", R.string.profile, R.drawable.profile)

    data object Records : Routes("entries", R.string.yourentries)

    data object RecordsToDelete : Routes("entries_delete", R.string.deleteentries)

    data object RecordsToModify : Routes("entries_modify", R.string.modify)

    // Drawer content items

    data object NewIncome : Routes("new_income", R.string.newincome, R.drawable.ic_incomes)

    data object NewExpense : Routes("new_expense", R.string.newexpense, R.drawable.ic_expenses)


    data object Transfer : Routes("transfer", R.string.transfer, R.drawable.transferoption)

    data object Statistics : Routes("statistics", R.string.stadistics, R.drawable.ic_staditics)

    data object BarChart : Routes("bar_chart", R.string.barchart, R.drawable.barchartoption)

    data object PieChart : Routes("pie_chart", R.string.piechart, R.drawable.ic_piechart)



    data object Calculator : Routes("calculator", R.string.calculator, R.drawable.ic_calculate)

    data object About : Routes("about", R.string.about, R.drawable.info)

    data object AboutDescription : Routes("about_app", R.string.aboutapp, R.drawable.info)

    data object Email : Routes("email", R.string.email, R.drawable.email)

    data object NewEntry : Routes(route = "new_entry")

    data object AddAccount : Routes("add_account", R.string.add_an_account, R.drawable.add)

    data object ModifyAccount : Routes("modify_account",  R.string.edit_account, R.drawable.edit)

    data object DeleteAccount :
        Routes("delete_account", R.string.delete_an_account, R.drawable.baseline_delete_24)

    data object DeleteRecords :
        Routes("delete_records", R.string.searchtitledelete, R.drawable.ic_databasedelete)

    data object ModifyRecords :
        Routes("modify_records", R.string.searchtitlemodify, R.drawable.ic_databaseupdate)

    data object ChangeCurrency :
        Routes("change_currency", R.string.changeCurrency, R.drawable.exchange)


    data object ModifyAccountItem: Routes("modify_account/{id}",R.string.modify,R.drawable.configaccountoption){
        fun createRoute(id:Int):String="modify_account/$id"
    }
    data object ModifyRecordItem: Routes("modify_record/{recordJson}",R.string.modifyentry,R.drawable.ic_databaseupdate){
        fun createRoute(recordJson:String):String="modify_record/$recordJson"
    }

    data object SpendingControl :
        Routes("spending_control", R.string.spendingcontrol, R.drawable.ic_expensecontrol)

    data object SelectAccounts:Routes("select_accounts",
        R.string.accountscontrol,
        R.drawable.ic_selectaccounts)

    data object SpendingControlByAccount:Routes("total_expense",
        R.string.expensecontrol,
        R.drawable.ic_expensetotal)

    data object SelectCategories:Routes("select_categories",
        R.string.selectcategories,
        R.drawable.ic_selectcategories)

    data object SpendingControlByCategory:Routes("select_categories",
        R.string.categorycontrol,
        R.drawable.ic_categorycontrol)


}