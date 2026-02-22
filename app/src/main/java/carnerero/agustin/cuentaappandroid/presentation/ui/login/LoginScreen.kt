package carnerero.agustin.cuentaappandroid.presentation.ui.login

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.ContentSection
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.GreetingType
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.ImageSection
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.LoginEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.LoginUiEvent
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel(),
    modifier: Modifier,
    navToMainScreen: () -> Unit,
) {
    // Detectar orientacion

    val isLandscape = orientation== OrientationApp.Landscape
    val state by loginViewModel.uiState.collectAsStateWithLifecycle()
    val eventEffect by loginViewModel.effect.collectAsState(initial = LoginEffect.Idle)
    val messageInvalidLogin = message(resource = R.string.inValidLogin)
    val messageInvalidUserName = message(resource = R.string.inValidUserName)
    val greeting = when(loginViewModel.getGreetingHour()) {
        GreetingType.MORNING -> stringResource(R.string.goodmorning)+" ${state.name}"
        GreetingType.AFTERNOON -> stringResource(R.string.goodafternoon)+" ${state.name}"
        GreetingType.EVENING -> stringResource(R.string.goodevening)+" ${state.name}"
    }
    LaunchedEffect(Unit) {
        loginViewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToMainScreen -> navToMainScreen()
                is LoginEffect.ShowInvalidCredentialsMessage ->
                    SnackBarController.sendEvent(SnackBarEvent(messageInvalidLogin))
                is LoginEffect.ShowInvalidUserNameMessage ->
                    SnackBarController.sendEvent(SnackBarEvent(messageInvalidUserName))
            }
        }
    }
    /*LaunchedEffect(eventEffect) {
        when (eventEffect) {
            is LoginEffect.NavigateToMainScreen -> {
                navToMainScreen()
            }
            is LoginEffect.ShowInvalidCredentialsMessage -> {
                SnackBarController.sendEvent(SnackBarEvent(messageInvalidLogin))
            }
            is LoginEffect.ShowInvalidUserNameMessage -> (SnackBarController.sendEvent(
                SnackBarEvent(messageInvalidUserName)
            ))
        }

    }*/

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
                    state.selectedImageUri
                )
                ContentSection(
                    fieldModifier,
                    state,
                    onUserNameChange = {loginViewModel.onUserEvent(LoginUiEvent.OnUserNameChange(it))},
                    {loginViewModel.onUserEvent(LoginUiEvent.OnPasswordChange(it))},
                    {loginViewModel.onUserEvent(LoginUiEvent.OnForgotPasswordClick)},
                    {loginViewModel.onUserEvent(LoginUiEvent.OnNewPasswordChange(it))},
                    {loginViewModel.onUserEvent(LoginUiEvent.OnLoginClickOk)},
                    {loginViewModel.onUserEvent(LoginUiEvent.OnConfirmNewPasswordClick)},
                    {loginViewModel.onUserEvent(LoginUiEvent.OnBackToLoginClick)},
                    greeting
                )

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
                    state.selectedImageUri
                )
                ContentSection(
                    fieldModifier,
                    state,
                    onUserNameChange = {loginViewModel.onUserEvent(LoginUiEvent.OnUserNameChange(it))},
                    {loginViewModel.onUserEvent(LoginUiEvent.OnPasswordChange(it))},
                    {loginViewModel.onUserEvent(LoginUiEvent.OnForgotPasswordClick)},
                    {loginViewModel.onUserEvent(LoginUiEvent.OnNewPasswordChange(it))},
                    {loginViewModel.onUserEvent(LoginUiEvent.OnLoginClickOk)},
                    {loginViewModel.onUserEvent(LoginUiEvent.OnConfirmNewPasswordClick)},
                    {loginViewModel.onUserEvent(LoginUiEvent.OnBackToLoginClick)},
                    greeting
                )

            }
        }
    }
}

















