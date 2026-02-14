package carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.view


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.datastore.GetShowTutorialUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetToLoginUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.model.TutorialEvent
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.model.TutorialUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor(
    private val getLoginValue: GetToLoginUseCase,
    private val getShowTutorial: GetShowTutorialUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(TutorialUiState())
    val uiState: StateFlow<TutorialUiState> = _uiState

    private val _tutorialEvent = MutableSharedFlow<TutorialEvent>()
    val tutorialEvent = _tutorialEvent.asSharedFlow()


    init {
        // Launch separate coroutines to manage the two LiveData values independently
        loadToLoginValue()
        getShowTutorialValue()

    }


    fun onFromOnBoardingClick() {
        val state = _uiState.value
        viewModelScope.launch {
            if (state.toLogin) {
                _tutorialEvent.emit(TutorialEvent.NavigateToLogin)
            }
            else {
                _tutorialEvent.emit(TutorialEvent.NavigateToCreateProfile)
            }
        }
    }

    // Load `toLogin` value separately
    private fun loadToLoginValue() {
        viewModelScope.launch {
                _uiState.update {
                    it.copy(toLogin =getLoginValue())
                }
        }
    }

    // Load `showTutorial` value separately
    private fun getShowTutorialValue() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(showTutorial =getShowTutorial())
            }
        }
    }
  }