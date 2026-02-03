package carnerero.agustin.cuentaappandroid.presentation.ui.login

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.ContentSection
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.ImageSection

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel(),
    modifier: Modifier,
    navToMain: () -> Unit,
) {
    LaunchedEffect(Unit) {
        loginViewModel.getLoginImage()
    }
    // Detectar orientacion
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val imageHeight = maxHeight * 0.5f
        val imageWidth=maxWidth*0.5f
        val fieldModifier = Modifier
            .fillMaxWidth(0.85f)
            .heightIn(min = 48.dp)
        if (isLandscape) {
            Row(modifier = Modifier
                .fillMaxSize()
                .background(colors.backgroundPrimary)) {
                ImageSection(
                    Modifier
                        .fillMaxHeight()
                        .width(imageWidth)
                        .background(colors.imageBackground),
                    loginViewModel
                )
                ContentSection(
                    loginViewModel,
                    fieldModifier
                )
                { navToMain() }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.backgroundPrimary)
            ) {
                ImageSection(
                    Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .background(colors.imageBackground),
                    loginViewModel
                )
                ContentSection(
                    loginViewModel,
                    fieldModifier
                )
                { navToMain() }

            }
        }
    }
}

















