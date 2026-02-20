package carnerero.agustin.cuentaappandroid.presentation.ui.profile

import android.net.Uri
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.model.UserProfile

sealed class ProfileUiEvent(){

    data object Idle: ProfileUiEvent()
    data class OnUserNameChange(val value: String) : ProfileUiEvent()
    data class OnNameChange(val value: String) : ProfileUiEvent()
    data class OnPasswordChange(val value: String) : ProfileUiEvent()

    data class OnRepeatPasswordChange(val value:String): ProfileUiEvent()

    data class OnProfileImageChange(val value: Uri): ProfileUiEvent()
    data class OnCreateProfile(val value: UserProfile) : ProfileUiEvent()

    data class UpdateNameProfile(val newName: String) : ProfileUiEvent()

    data class UpdateUsernameProfile(val newUsername: String) : ProfileUiEvent()

    data class UpdatePasswordProfile(val newPassword: String) : ProfileUiEvent()

    data class UpdatePhotoProfile(val newPhoto: Uri?) : ProfileUiEvent()
}