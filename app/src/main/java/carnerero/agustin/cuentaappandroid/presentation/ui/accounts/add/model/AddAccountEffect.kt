package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.model

import androidx.annotation.StringRes

sealed class AddAccountEffect {

    object Idle: AddAccountEffect()

    data class ShowMessage(@param:StringRes val messageRes: Int) : AddAccountEffect()

    object NavigateToLogin : AddAccountEffect()
    object NavigateBack : AddAccountEffect()
}
