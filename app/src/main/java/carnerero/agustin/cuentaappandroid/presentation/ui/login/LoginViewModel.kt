package carnerero.agustin.cuentaappandroid.presentation.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.domain.datastore.GetPhotoFromUriUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetUserProfileDataUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.UpDatePasswordUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.GreetingType
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.LoginEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.LoginUiEvent
import carnerero.agustin.cuentaappandroid.presentation.ui.login.components.LoginUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserProfileData: GetUserProfileDataUseCase,
    private val upDatePassword: UpDatePasswordUseCase,
    private val getUri: GetPhotoFromUriUseCase
) : ViewModel() {

    // ---------------------------
    // STATE
    // ---------------------------

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _effect = Channel<LoginEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()


    private var userProfile: UserProfile? = null

    // ---------------------------
    // INIT
    // ---------------------------

    init {
        loadProfileData()
    }
    private fun loadProfileData(){
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
    fun onUserEvent(event: LoginUiEvent){
        when(event){
            is LoginUiEvent.OnUserNameChange->{
                onUserNameChange(event.value)
            }
            is LoginUiEvent.OnPasswordChange -> {
                onPasswordChange(event.value)
            }
            is LoginUiEvent.OnNewPasswordUserNameChange -> {
                onNewPasswordUserNameChange(event.value)
            }
            is LoginUiEvent.OnNewPasswordChange -> {
                onNewPasswordChange(event.value)
            }
            is LoginUiEvent.OnLoginClickOk->{
                onLoginClick()
            }
            is LoginUiEvent.OnConfirmNewPasswordClick->{
                onConfirmNewPassword()
                enableNewPasswordFields(false)
            }
            is LoginUiEvent.OnForgotPasswordClick ->{
                enableNewPasswordFields(true)
            }
            is LoginUiEvent.OnBackToLoginClick->{
                enableNewPasswordFields(false)
            }
            else -> {}
        }
    }

    // ---------------------------
    // LOGIN INPUTS
    // ---------------------------

    private fun onUserNameChange(value: String) {
        _uiState.update { current ->
            current.copy(
                userName = value,
                enableLoginButton = isLoginEnabled(
                    value,
                    current.password
                )
            )
        }
    }

    private fun onPasswordChange(value: String) {
        _uiState.update { current ->
            current.copy(
                password = value,
                enableLoginButton = isLoginEnabled(
                    current.userName,
                    value
                )
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
                _effect.send(LoginEffect.NavigateToHome)  // Evento de navegación
            }else{
                _effect.send(LoginEffect.ShowInvalidCredentialsMessage)
            }
        }
    }


    // ---------------------------
    // NUEVA CONTRASEÑA
    // ---------------------------

    private fun onNewPasswordUserNameChange(value: String) {
        _uiState.update { current ->
            current.copy(
                userNameNewPassword = value,
                enableConfirmButton = isConfirmEnabled(
                    value,
                    current.newPassword
                )
            )
        }
    }

    private fun onNewPasswordChange(value: String) {
        _uiState.update { current ->
            current.copy(
                newPassword = value,
                enableConfirmButton = isConfirmEnabled(
                    current.userNameNewPassword,
                    value
                )
            )
        }
    }

    private fun enableNewPasswordFields(enable: Boolean) {
        _uiState.update {
            it.copy(showNewPasswordFields = enable)
        }
    }


   private fun onConfirmNewPassword() {
        val state = _uiState.value
        val user = userProfile ?: return
        viewModelScope.launch {
            if (state.userNameNewPassword == user.profileUserName) {
                upDatePassword(state.newPassword)
                userProfile = getUserProfileData()
                _uiState.update {
                    it.copy(
                        showNewPasswordFields = false,
                        userNameNewPassword = "",
                        newPassword = "",
                        enableConfirmButton = false
                    )
                }
            }else{
                _effect.send(LoginEffect.ShowInvalidUserNameMessage)
                clearNewPasswordFields()
            }
        }
    }

    private fun clearNewPasswordFields() {
        _uiState.update {
            it.copy(
                userNameNewPassword = "",
                newPassword = "",
                enableConfirmButton = false
            )
        }
    }


    // ---------------------------
    // GREETING
    // ---------------------------
    fun getGreetingHour(): GreetingType {
        val hour = LocalTime.now().hour
        return  when (hour) {
            in 6..11 -> GreetingType.MORNING
            in 12..17 -> GreetingType.AFTERNOON
            else -> GreetingType.EVENING
        }
    }


    // ---------------------------
    // VALIDATION HELPERS
    // ---------------------------

    private fun isLoginEnabled(userName: String, password: String): Boolean {
        return userName.isNotBlank() &&
                password.isNotBlank() &&
                password.length >= 6
    }

    private fun isConfirmEnabled(userName: String, password: String): Boolean {
        return userName.isNotBlank() &&
                password.isNotBlank() &&
                password.length >= 6
    }

}
