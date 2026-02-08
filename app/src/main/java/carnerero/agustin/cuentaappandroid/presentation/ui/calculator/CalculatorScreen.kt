package carnerero.agustin.cuentaappandroid.presentation.ui.calculator

import androidx.compose.runtime.Composable
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp

@Composable
fun CalculatorScreen(calculatorViewModel: CalculatorViewModel,
                     accountsViewModel: AccountsViewModel){


    val isLandScape=orientation == OrientationApp.Landscape

    if(isLandScape){
        CalculatorLandscapeUI(calculatorViewModel,accountsViewModel)
    }else{
        CalculatorPortraitUI(calculatorViewModel)
    }

}