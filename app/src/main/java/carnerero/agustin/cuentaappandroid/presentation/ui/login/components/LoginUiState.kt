package carnerero.agustin.cuentaappandroid.presentation.ui.login.components

import android.net.Uri

data class LoginUiState(
    val selectedImageUri: Uri? = null,
    // Login
    val name: String = "",
    val userName: String = "",
    val password: String = "",
    // Nueva contraseña
    val newPassword: String = "",
    val showNewPasswordFields:Boolean=false
){
    val enableButton: Boolean
        get()=userName.isNotBlank() &&
                ((password.isNotBlank() && password.length >= 6)
                        ||
                (newPassword.isNotBlank() && newPassword.length>=6))


}

