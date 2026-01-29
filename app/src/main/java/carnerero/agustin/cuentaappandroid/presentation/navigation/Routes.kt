package carnerero.agustin.cuentaappandroid.presentation.navigation

import carnerero.agustin.cuentaappandroid.R

sealed class Routes(val route:String,
                    val labelResource:Int?=null,
                    val iconResource:Int?=null){

    data object Main : Routes("main")
    data object CreateProfile : Routes("create profile",R.string.profile,R.drawable.profile)
    data object CreateAccounts : Routes("create accounts",R.string.createAccount,R.drawable.add)
    data object Tutorial : Routes("tutorial")
    data object Login : Routes("login")  // Add more routes as needed

    // AppBottonBar menu

     data object Home : Routes("home",R.string.greeting,R.drawable.home)

    data object Search : Routes("search",R.string.search,R.drawable.search)

    data object Settings : Routes("settings",R.string.settings,R.drawable.settings)

    data object Profile: Routes("profile",R.string.profile,R.drawable.profile)

    data object Records:Routes("entries",R.string.yourentries)

}