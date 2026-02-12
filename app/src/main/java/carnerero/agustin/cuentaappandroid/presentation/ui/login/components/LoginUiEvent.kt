package carnerero.agustin.cuentaappandroid.presentation.ui.login.components


sealed class LoginUiEvent {
    data object Idle: LoginUiEvent()
    data class OnUserNameChange(val value: String) : LoginUiEvent()
    data class OnPasswordChange(val value: String) : LoginUiEvent()
    data class OnNewPasswordUserNameChange(val value: String) : LoginUiEvent()
    data class OnNewPasswordChange(val value: String) : LoginUiEvent()

    data object OnBackToLoginClick : LoginUiEvent()
    data object OnLoginClickOk : LoginUiEvent()
    data object OnForgotPasswordClick : LoginUiEvent()
    data object OnConfirmNewPasswordClick : LoginUiEvent()

}
