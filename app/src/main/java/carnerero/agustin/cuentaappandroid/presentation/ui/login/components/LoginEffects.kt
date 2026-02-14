package carnerero.agustin.cuentaappandroid.presentation.ui.login.components

sealed class LoginEffect {
    data object Idle: LoginUiEvent()
    data object NavigateToMainScreen : LoginEffect()
    data object ShowInvalidCredentialsMessage : LoginEffect()
    data object ShowInvalidUserNameMessage : LoginEffect()
}
