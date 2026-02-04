package carnerero.agustin.cuentaappandroid.presentation.ui.calculator

import androidx.compose.runtime.Composable
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp

@Composable
fun CalculatorScreen( viewModel: CalculatorViewModel){

    val isLandScape=orientation == OrientationApp.Landscape

    if(isLandScape){
        CalculatorLandscapeUI(viewModel)
    }else{
        CalculatorPortraitUI(viewModel)
    }

}