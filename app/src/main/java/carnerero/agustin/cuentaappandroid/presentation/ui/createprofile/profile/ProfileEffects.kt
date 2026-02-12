package carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile

sealed class ProfileEffect {

     data object Idle: ProfileEffect()

     data object NavigateToCreateAccounts: ProfileEffect()
     data object ProfileSavedMessage : ProfileEffect()

}
