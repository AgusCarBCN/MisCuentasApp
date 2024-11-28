package carnerero.agustin.cuentaappandroid.presentation.ui.login

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.R

import carnerero.agustin.cuentaappandroid.main.domain.datastore.GetPhotoFromUriUseCase
import carnerero.agustin.cuentaappandroid.main.domain.datastore.GetUserProfileDataUseCase
import carnerero.agustin.cuentaappandroid.main.domain.datastore.UpDatePasswordUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserProfileData: GetUserProfileDataUseCase,
    private val upDatePassword: UpDatePasswordUseCase,
    private val getUri: GetPhotoFromUriUseCase
) : ViewModel() {

    // Definimos selectedImageUri como un LiveData
    private val _selectedImageUriSaved = MutableLiveData<Uri?>()
    val selectedImageUriSaved: LiveData<Uri?> = _selectedImageUriSaved

    private val _user = MutableLiveData<UserProfile>()
    private val user: LiveData<UserProfile> = _user

    // LiveData para los campos de introduccion de Login

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    //LiveData para los campos de nueva contraseña

    private val _userNameNewPassword = MutableLiveData<String>()
    val userNameNewPassword: LiveData<String> = _userNameNewPassword

    private val _newPassword = MutableLiveData<String>()
    val newPassword: LiveData<String> = _newPassword

    //LiveData para la habilitación de botones
    private val _enableLoginButton = MutableLiveData<Boolean>()
    val enableLoginButton: LiveData<Boolean> = _enableLoginButton

    private val _enableConfirmButton = MutableLiveData<Boolean>()
    val enableConfirmButton: LiveData<Boolean> = _enableConfirmButton

    //LiveData validación de Login
    private val _validateLoginButton = MutableLiveData<Boolean>()
    val validateLoginButton: LiveData<Boolean> = _validateLoginButton

    //LiveData confirmar cambio de contraseña
    private val _validateConfirmButton = MutableLiveData<Boolean>()
    val validateConfirmButton: LiveData<Boolean> = _validateConfirmButton

    //LiveData para mostrar botones para introducir nueva contraseña
    private val _enableNewPasswordFields = MutableLiveData<Boolean>()
    val enableNewPasswordFields: LiveData<Boolean> = _enableNewPasswordFields

    init {
        // Cargar el valor inicial de toLogin desde el repositorio al inicializar el ViewModel
        viewModelScope.launch {
            val userProfile = getUserProfileData()
            _selectedImageUriSaved.value = getUri()
            _user.value = userProfile
            _name.value = userProfile.profileName

        }
    }
    fun getLoginImage(){
        viewModelScope.launch {
            _selectedImageUriSaved.value = getUri()
        }
    }

    //actualizacion de _userName y _password con los valores que se pasan como argumentos
    // a la función (userName y password).
    fun onLoginChanged(userName: String, password: String) {
        _userName.value = userName
        _password.value = password
        //Log.d("Datos leidos: ", "Usuario en _user: ${_user.value?.profileUserName}, Comparado con userName: ${_userName.value}")

        _enableLoginButton.value = enableButton(userName, password)

        // Si los datos del perfil no han sido cargados aún, no intentamos validar
        if (_user.value != null) {
            _validateLoginButton.value = validateLoginButton(userName, password)
        } else {
            _validateLoginButton.value = false
            Log.d("Login", "Datos de usuario aún no disponibles")
        }
    }


    fun onUpdatePasswordChange(userName: String, newPassword: String) {
        _userNameNewPassword.value = userName
        _newPassword.value = newPassword
        _enableConfirmButton.value = enableButton(userName, newPassword)
        if (_user.value != null) {
            _validateConfirmButton.value = validateUserName(userName)

        } else {
            _validateLoginButton.value = false
            Log.d("Login", "Datos de usuario aún no disponibles")
        }
    }

    fun onClearFields() {
        _userNameNewPassword.value = ""
        _newPassword.value = ""
    }

    fun updatePassword(newPassword: String) {
        viewModelScope.launch {
            upDatePassword.invoke(newPassword)
            Log.d("Datos leidos: ", "Usuario en _user: ${_user.value?.profilePass}")
            // Obtener el perfil actualizado
            val userProfile = getUserProfileData()
            // Emitir los nuevos datos del perfil
            // Actualizar LiveData con el nuevo perfil
            _user.value = userProfile
        }
    }


    fun onEnableNewPasswordFields(enableNewPasswordFields: Boolean) {
        _enableNewPasswordFields.value = enableNewPasswordFields
    }

    @Composable
    fun getGreeting(userName: String): String {
        val hour = LocalTime.now().hour

        val greeting = when (hour) {
            in 6..11 -> stringResource(id = R.string.goodmornig)
            in 12..17 -> stringResource(id = R.string.goodafternoon)
            else -> stringResource(id = R.string.goodevening)
        }

        return "$greeting, $userName" // Concatenar el saludo con el nombre
    }

    private fun enableButton(userName: String, password: String): Boolean =
        userName.isNotEmpty() && password.isNotEmpty() && userName.isNotBlank() && password.isNotBlank() && password.length >= 6

    private fun validateLoginButton(userName: String, password: String): Boolean {
        val userProfile = _user.value
        return if (userProfile != null) {
            userName == userProfile.profileUserName && password == userProfile.profilePass
        } else {
            false
        }
    }

    private fun validateUserName(userName: String): Boolean {
        val userProfile = _user.value
        return if (userProfile != null) {
            userName == userProfile.profileUserName
        } else {
            false
        }
    }
}