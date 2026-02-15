package carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile

import android.net.Uri

data class ProfileUiState(
    val username: String = "",
    val name: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val selectedImageUri: Uri? = null,
    val selectedImageUriSaved: Uri? = null
){
    val enableButton=name.isNotBlank() &&
            username.length >= 3 &&
            password.length >= 6 &&
            password == repeatPassword
}

