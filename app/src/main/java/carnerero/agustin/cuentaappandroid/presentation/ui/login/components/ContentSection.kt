package carnerero.agustin.cuentaappandroid.presentation.ui.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.ui.login.LoginViewModel
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent

@Composable
fun ContentSection(
    loginViewModel: LoginViewModel,
    modifier: Modifier,
    navToHome: () -> Unit
) {
    val state by loginViewModel.uiState.collectAsStateWithLifecycle()
    val eventEffect by loginViewModel.effect.collectAsState(initial = LoginEffect.Idle)
    val messageInvalidLogin = message(resource = R.string.inValidLogin)
    val messageInvalidUserName = message(resource = R.string.inValidUserName)
    val greeting = when(loginViewModel.getGreetingHour()) {
        GreetingType.MORNING -> stringResource(R.string.goodmorning)+" ${state.name}"
        GreetingType.AFTERNOON -> stringResource(R.string.goodafternoon)+" ${state.name}"
        GreetingType.EVENING -> stringResource(R.string.goodevening)+" ${state.name}"
    }

    LaunchedEffect(eventEffect) {
        when (eventEffect) {
            is LoginEffect.NavigateToHome -> {
                navToHome()
            }
            is LoginEffect.ShowInvalidCredentialsMessage -> {
                SnackBarController.sendEvent(SnackBarEvent(messageInvalidLogin))
            }
            is LoginEffect.ShowInvalidUserNameMessage -> (SnackBarController.sendEvent(
                SnackBarEvent(messageInvalidUserName)
            ))
        }

    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .background(colors.backgroundPrimary),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (!state.showNewPasswordFields) {
            Text(
                modifier = Modifier.padding(top = dimens.extraLarge),
                text = greeting,
                fontSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_title_medium).toSp() },
                color = colors.textColor,
                fontWeight = FontWeight.Bold
            )
            TextFieldComponent(
                modifier = modifier,
                stringResource(id = R.string.enterUsername),
                state.userName,
                onTextChange = {loginViewModel.onUserEvent(LoginUiEvent.OnUserNameChange(it))},
                BoardType.TEXT
            )
            TextFieldComponent(
                modifier = modifier,
                stringResource(id = R.string.enterPassword),
                state.password,
                onTextChange = {loginViewModel.onUserEvent(LoginUiEvent.OnPasswordChange(it))},
                BoardType.PASSWORD,
                true
            )
            ModelButton(
                text = stringResource(id = R.string.loginButton),
                MaterialTheme.typography.labelLarge,
                modifier = modifier,
                state.enableLoginButton,
                onClickButton = {
                    loginViewModel.onUserEvent(LoginUiEvent.OnLoginClickOk)
                }
            )

            TextButton(
                onClick = {
                    loginViewModel.onUserEvent(LoginUiEvent.OnForgotPasswordClick)
                },
                content = {
                    Text(
                        text = stringResource(id = R.string.forgotpassword),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textColor
                    )
                }
            )
        }
        if (state.showNewPasswordFields) {
            TextFieldComponent(
                modifier = modifier,
                stringResource(id = R.string.requestuser),
                state.userNameNewPassword,
                onTextChange = { loginViewModel.onUserEvent(LoginUiEvent.OnNewPasswordUserNameChange(it)) },
                BoardType.TEXT
            )

            TextFieldComponent(
                modifier = modifier,
                stringResource(id = R.string.newpassword),
                state.newPassword,
                onTextChange = {
                    loginViewModel.onUserEvent(LoginUiEvent.OnNewPasswordChange(it))
                },
                BoardType.PASSWORD,
                true
            )
            ModelButton(
                text = stringResource(id = R.string.confirmButton),
                MaterialTheme.typography.labelLarge,
                modifier = modifier,
                state.enableConfirmButton,
                onClickButton = {
                    loginViewModel.onUserEvent(LoginUiEvent.OnConfirmNewPasswordClick)
                }
            )

            ModelButton(
                text = stringResource(id = R.string.backButton),
                MaterialTheme.typography.labelLarge,
                modifier = modifier,
                true,
                onClickButton = {
                    loginViewModel.onUserEvent(LoginUiEvent.OnBackToLoginClick)
                }
            )
        }
    }
}