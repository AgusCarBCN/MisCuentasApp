package carnerero.agustin.cuentaappandroid.presentation.ui.login.components

import android.net.Uri

data class LoginUiState(
    val selectedImageUri: Uri? = null,
    // Login
    val name: String = "",
    val userName: String = "",
    val password: String = "",
    // Nueva contraseña
    val userNameNewPassword: String = "",
    val newPassword: String = "",
    // UI flags
    val enableLoginButton: Boolean = false,
    val enableConfirmButton: Boolean = false,
    val showNewPasswordFields: Boolean = false
)

