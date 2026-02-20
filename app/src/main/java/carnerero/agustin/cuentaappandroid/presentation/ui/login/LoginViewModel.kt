package carnerero.agustin.cuentaappandroid.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.datastore.GetPhotoFromUriUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetUserProfileDataUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.UpdatePasswordUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.GreetingType
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.LoginEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.LoginUiEvent
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.LoginUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserProfileData: GetUserProfileDataUseCase,
    private val upDatePassword: UpdatePasswordUseCase,
    private val getUri: GetPhotoFromUriUseCase
) : ViewModel() {

    // ---------------------------
    // STATE
    // ---------------------------

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _effect = MutableSharedFlow<LoginEffect>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val effect = _effect.asSharedFlow()

    private var userProfile: UserProfile? = null

    // ---------------------------
    // INIT
    // ---------------------------

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            userProfile = getUserProfileData()
            _uiState.update {
                it.copy(
                    name = userProfile?.profileName.orEmpty(),
                    selectedImageUri = getUri()
                )
            }
        }
    }

    fun onUserEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnUserNameChange -> {
                _uiState.update { current ->
                    current.copy(
                        userName = event.value
                    )
                }
            }

            is LoginUiEvent.OnPasswordChange -> {
                _uiState.update { current ->
                    current.copy(
                        password = event.value
                    )
                }
            }

            is LoginUiEvent.OnNewPasswordUserNameChange -> {
                _uiState.update { current ->
                    current.copy(
                        userName = event.value
                    )
                }
            }

            is LoginUiEvent.OnNewPasswordChange -> {
                _uiState.update { current ->
                    current.copy(
                        newPassword = event.value
                    )
                }
            }



            is LoginUiEvent.OnLoginClickOk -> {
                onLoginClick()
            }

            is LoginUiEvent.OnForgotPasswordClick -> {
                onShowNewPasswordFields(true)
                cleanFields()
            }

            is LoginUiEvent.OnBackToLoginClick -> {
                onShowNewPasswordFields(false)
                cleanFields()
            }

            LoginUiEvent.OnConfirmNewPasswordClick -> {
                onConfirmNewPassword()
            }

            else -> {

            }
        }
    }

    private fun onShowNewPasswordFields(newValue: Boolean) {
        _uiState.update { current ->
            current.copy(
                showNewPasswordFields = newValue
            )
        }
    }

    private fun onLoginClick() {
        val state = _uiState.value
        val user = userProfile ?: return
        viewModelScope.launch {
            if (state.userName == user.profileUserName &&
                state.password == user.profilePass
            ) {
                _effect.emit(LoginEffect.NavigateToMainScreen)  // Evento de navegación
            } else {
                _effect.emit(LoginEffect.ShowInvalidCredentialsMessage)
            }
        }
    }


    private fun onConfirmNewPassword() {
        val state = _uiState.value
        val user = userProfile ?: return
        viewModelScope.launch {
            if (state.userName == user.profileUserName) {
                upDatePassword(state.newPassword)
                userProfile = getUserProfileData()
            } else {
                _effect.emit(LoginEffect.ShowInvalidUserNameMessage)
            }
            cleanFields()
        }
    }

    private fun cleanFields() {
        _uiState.update {
            it.copy(
                userName = "",
                password = "",
                newPassword = ""
            )
        }
    }

    // ---------------------------
    // GREETING
    // ---------------------------
    fun getGreetingHour(): GreetingType {
        val hour = LocalTime.now().hour
        return when (hour) {
            in 6..11 -> GreetingType.MORNING
            in 12..17 -> GreetingType.AFTERNOON
            else -> GreetingType.EVENING
        }
    }
}
