package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model

import androidx.annotation.StringRes

sealed class CreateAccountEffect {

    object Idle: CreateAccountEffect()

    data class ShowMessage(@param:StringRes val messageRes: Int) : CreateAccountEffect()

    object NavigateToLogin : CreateAccountEffect()
    object NavigateBack : CreateAccountEffect()
}
