package carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile

import android.net.Uri

sealed class ProfileUiEvent(){

    data object Idle: ProfileUiEvent()
    data class OnUserNameChange(val value: String) : ProfileUiEvent()
    data class OnNameChange(val value: String) : ProfileUiEvent()
    data class OnPasswordChange(val value: String) : ProfileUiEvent()

    data class OnRepeatPasswordChange(val value:String): ProfileUiEvent()

    data class OnProfileImageChange(val value:Uri): ProfileUiEvent()
    data class OnCreateProfile(val value: UserProfile) : ProfileUiEvent()


}
