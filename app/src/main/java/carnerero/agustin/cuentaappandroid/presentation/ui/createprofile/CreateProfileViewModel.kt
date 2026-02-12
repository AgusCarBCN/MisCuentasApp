package carnerero.agustin.cuentaappandroid.presentation.ui.createprofile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.datastore.GetPhotoFromUriUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetUserProfileDataUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SaveUriUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetToLoginUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetUserProfileDataUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile.ProfileEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile.ProfileUiEvent
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile.ProfileUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val setProfileData: SetUserProfileDataUseCase,
    private val getProfileData: GetUserProfileDataUseCase,
    private val setLoginTo: SetToLoginUseCase,
    private val saveUri: SaveUriUseCase,
    private val getUri: GetPhotoFromUriUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val _effect = Channel<ProfileEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
           getProfileData()
            loadImageUri()
        }
    }


    fun onUserEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.OnNameChange -> {
                onNameChanged(event.value)
            }

            is ProfileUiEvent.OnUserNameChange -> {
                onUserNameChanged(event.value)
            }

            is ProfileUiEvent.OnPasswordChange -> {
                onPasswordChanged(event.value)
            }

            is ProfileUiEvent.OnRepeatPasswordChange -> {
                onRepeatPassword(event.value)
            }

            is ProfileUiEvent.OnProfileImageChange -> {
                onImageSelected(event.value)

            }

            is ProfileUiEvent.OnCreateProfile -> {
                createProfile(event.value)
            }
            else -> {}
        }
    }

    fun onRepeatPassword(newRepeatPassword: String) {

        _uiState.update { current ->
            current.copy(repeatPassword = newRepeatPassword)
        }
    }

    fun onNameChanged(newName: String) {
        _uiState.update { current ->
            current.copy(name = newName)
        }
    }

    fun onUserNameChanged(newUserName: String) {
        _uiState.update { current ->
            current.copy(username = newUserName)
        }
    }

    fun onPasswordChanged(newPassword: String) {
            _uiState.update { current ->
                current.copy(
                    password = newPassword)
            }
    }

    fun onImageSelected(selectedImage: Uri) {
        _uiState.update { current ->
            current.copy(
                selectedImageUri = selectedImage
            )
        }
    }

    fun loadImageUri() {
        viewModelScope.launch {
            val uri = getUri()
            _uiState.update {
                it.copy(selectedImageUriSaved = uri)
            }
        }
    }

    fun createProfile(
        profile: UserProfile
    ) {
        viewModelScope.launch {
                setProfileData(profile)
                setLoginTo(true)
                _uiState.value.selectedImageUri?.let {
                    saveUri(it)
                }
                _uiState.update {
                    it.copy(
                        name = profile.name,
                        username = profile.userName,
                        password = profile.password
                    )
                }
                 _effect.send(ProfileEffect.ProfileSavedMessage)
                  delay(2000)
                 _effect.send(ProfileEffect.NavigateToCreateAccounts)
            }
        }


    }







