package carnerero.agustin.cuentaappandroid.presentation.ui.main.view

import android.net.Uri
import carnerero.agustin.cuentaappandroid.R

data class MainUiState(
    val image: Uri?=null,
    val isGranted:Boolean=false,
    val showExitDialog:Boolean=false,
    val resourceTitle:Int= R.string.home
)
