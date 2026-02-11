package carnerero.agustin.cuentaappandroid.presentation.ui.login.components

sealed class LoginEvent {
    object NavigateToMain : LoginEvent()
    object ShowInvalidLogin : LoginEvent()
    object ShowInvalidUserName : LoginEvent()
}
