package carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.model

import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.LoginEvent


    sealed class TutorialEvent {
        object NavigateToLogin : TutorialEvent()
        object NavigateToCreateProfile : TutorialEvent()
    }


