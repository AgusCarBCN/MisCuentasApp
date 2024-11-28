package carnerero.agustin.cuentaappandroid.presentation.ui.main.model

sealed class Routes(val route:String){
    data object Home : Routes("home")
    data object CreateProfile : Routes("create profile")
    data object CreateAccounts : Routes("create accounts")
    data object Tutorial : Routes("tutorial")
    data object Login : Routes("login")  // Add more routes as needed

}