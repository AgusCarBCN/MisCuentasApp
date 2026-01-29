package carnerero.agustin.cuentaappandroid.presentation.navigation

import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType

sealed class Routes(val route:String,
                    val labelResource:Int?=null,
                    val iconResource:Int?=null){

    data object Main : Routes("main")
    data object CreateProfile : Routes("create profile",R.string.profile,R.drawable.profile)
    data object CreateAccounts : Routes("create accounts",R.string.createAccount,R.drawable.add)
    data object Tutorial : Routes("tutorial")
    data object Login : Routes("login")  // Add more routes as needed

    // Bottom menu items

     data object Home : Routes("home",R.string.greeting,R.drawable.home)

    data object Search : Routes("search",R.string.search,R.drawable.search)

    data object Settings : Routes("settings",R.string.settings,R.drawable.settings)

    data object Profile: Routes("profile",R.string.profile,R.drawable.profile)

    data object Records:Routes("entries",R.string.yourentries)

    // Drawer content items

    data object NewIncome: Routes("new_income",R.string.newincome,R.drawable.ic_incomes)

    data object NewExpense: Routes("new_expense",R.string.newexpense,R.drawable.ic_expenses)



    data object Transfer: Routes("transfer",R.string.transfer,R.drawable.transferoption)

    data object Statistics: Routes("statistics",R.string.stadistics,R.drawable.ic_staditics)

    data object SpendingControl: Routes("spending_control",R.string.spendingcontrol,R.drawable.ic_expensecontrol)

    data object Calculator: Routes("calculator",R.string.calculator,R.drawable.ic_calculate)

    data object About: Routes("about",R.string.about,R.drawable.info)

    data object NewEntry:Routes(route="new_entry")

}