package carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.model




    sealed class TutorialEvent {
        object NavigateToLogin : TutorialEvent()
        object NavigateToCreateProfile : TutorialEvent()
    }


