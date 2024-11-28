package carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels


import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.main.domain.datastore.GetPhotoFromUriUseCase
import carnerero.agustin.cuentaappandroid.main.domain.datastore.GetUserProfileDataUseCase
import carnerero.agustin.cuentaappandroid.main.domain.datastore.SaveUriUseCase
import carnerero.agustin.cuentaappandroid.main.domain.datastore.SetToLoginUseCase
import carnerero.agustin.cuentaappandroid.main.domain.datastore.SetUserProfileDataUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
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

    // LiveData para los campos de texto
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _repeatPassword = MutableLiveData<String>()
    val repeatPassword: LiveData<String> = _repeatPassword

    //LiveData para habilitar boton de confirmar
    private val _enableButton = MutableLiveData<Boolean>()
    val enableButton: LiveData<Boolean> = _enableButton

    //LiveData para habilitar boton de cambiar imagen
    private val _enableChangeImage = MutableLiveData<Boolean>()
    val enableChangeImage: LiveData<Boolean> = _enableChangeImage

    //LiveData para habilitar los botones de cambiar campos de perfil
    private val _enableNameButton = MutableLiveData<Boolean>()
    val enableNameButton: LiveData<Boolean> = _enableNameButton

    private val _enablePasswordButton = MutableLiveData<Boolean>()
    val enablePasswordButton: LiveData<Boolean> = _enablePasswordButton

    private val _enableUserNameButton = MutableLiveData<Boolean>()
    val enableUserNameButton: LiveData<Boolean> = _enableUserNameButton


    // Definimos selectedImageUri como un LiveData
    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: LiveData<Uri?> = _selectedImageUri

    // Definimos selectedImageUri guardados en fichero como un LiveData
    private val _selectedImageUriSaved = MutableLiveData<Uri?>()
    val selectedImageUriSaved: LiveData<Uri?> = _selectedImageUriSaved


    init{
        viewModelScope.launch {

            val user = getProfileData.invoke()
            _name.value = user.profileName
            _username.value = user.profileUserName
            _password.value = user.profilePass
            loadImageUri()
        }
    }

    fun onTextFieldsChanged(name: String, userName: String, password: String, newPassword: String) {
        _name.value = name
        _username.value = userName
        _password.value = password
        _repeatPassword.value = newPassword
        //Verificaciones para activar boton de confirmar
        _enableButton.value = enableConfirmButton(name, userName, password, newPassword)
    }
    fun onNameChanged(newName: String){
        _name.value = newName
        _enableNameButton.value=true
    }

    fun onUserNameChanged(newUserName: String){
        _username.value = newUserName
        _enableUserNameButton.value=true
    }

    fun onPasswordChanged(newPassword: String){
        _password.value = newPassword
        _enablePasswordButton.value=true
    }
    fun onImageSelected(selectedImage:Uri)
    {
        _selectedImageUri.value = selectedImage
        _enableChangeImage.value=true
    }
    fun onButtonProfileNoSelected(){
        viewModelScope.launch {

            val user = getProfileData.invoke()
            _name.value = user.profileName
            _username.value = user.profileUserName
            _password.value = user.profilePass
        }

        _enableChangeImage.value=false
        _enableNameButton.value=false
        _enablePasswordButton.value=false
        _enablePasswordButton.value=false

    }
    fun saveImageUri(uri:Uri){
        viewModelScope.launch {
            saveUri(uri)
            //Actualizo para ver cambios de manera inmediata
            _selectedImageUriSaved.value=uri
        }
    }
    fun loadImageUri(){
        viewModelScope.launch {
            _selectedImageUriSaved.value = getUri()
        }
    }
    fun buttonState(photo:Boolean,useName:Boolean,name:Boolean,password:Boolean){
        _enableChangeImage.value=photo
        _enableNameButton.value=name
        _enablePasswordButton.value=password
        _enableUserNameButton.value=useName
    }

    // Función para setear un nuevo valor para toLogin en el repositorio

    // Función para setear un nuevo valor para toLogin en el repositorio
    fun setUserDataProfile(newProfile: UserProfile) {
        viewModelScope.launch {
            // Llamar a la función suspend de tu repositorio
            setProfileData(newProfile)
            setLoginTo(true)

        //Actualizamos los valores para reflejar cambios en UI
            _name.value=newProfile.name
            _username.value=newProfile.userName
            _password.value=newProfile.profilePass
        }
    }


    private fun enableConfirmButton(
        name: String,
        userName: String,
        password: String,
        newPassword: String
    ): Boolean {
        // Verifica que los campos no estén en blanco
        if (name.isBlank() || userName.isBlank() || password.isBlank() || newPassword.isBlank()) {
            return false
        }

        // Verifica que el nombre de usuario tenga al menos 3 caracteres (opcional)
        if (userName.length < 3) {
            return false
        }

        // Verifica que la contraseña tenga al menos 6 caracteres
        if (password.length < 6) {
            return false
        }

        // Verifica que la nueva contraseña coincida con la original
        if (password != newPassword) {
            return false
        }

        return true
    }


}