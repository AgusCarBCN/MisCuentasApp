package carnerero.agustin.cuentaappandroid.presentation.ui.main.view

import android.net.Uri
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes

data class MainUiState(
    val image: Uri?=null,
    val isGranted:Boolean=false,
    val showExitDialog:Boolean=false,
    val route: Routes=Routes.Home,
    val title:Int=R.string.home
)
