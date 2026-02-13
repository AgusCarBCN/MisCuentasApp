package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.CreateAccountViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.layouts.LandScapeLayout
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.layouts.PortraitLayout
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountEffect
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp


//Mapa de divisas y simbolos
@Composable
fun CreateAccountsScreen(
    createAccountViewModel: CreateAccountViewModel,
    enableSelector: Boolean,
    navToLogin: () -> Unit,
    navToBack: () -> Unit
) {
    val isPortrait = orientation == OrientationApp.Portrait

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
    ) {
        if (isPortrait) {
            PortraitLayout(
              createAccountViewModel,
                navToLogin,
                navToBack
            )
        } else {
            LandScapeLayout (
              createAccountViewModel,
                navToLogin,
                navToBack
            )
        }
    }
}
