package carnerero.agustin.cuentaappandroid.presentation.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.datastore.GetEnableDarkThemUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetEnableNotificationsUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetEnableTutorialUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetShowTutorialUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetEnableDarkThemeUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetEnableNotificationsUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetEnableTutorialUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetShowTutorialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class SettingViewModel @Inject constructor(
    private val getShowTutorial: GetShowTutorialUseCase,
    private val setShowTutorial: SetShowTutorialUseCase,
    private val getSwitchTutorial: GetEnableTutorialUseCase,
    private val changeSwitchTutorial: SetEnableTutorialUseCase,
    private val getSwitchDarkTheme: GetEnableDarkThemUseCase,
    private val changeSwitchDarkTheme: SetEnableDarkThemeUseCase,
    private val getNotificationsTutorial: GetEnableNotificationsUseCase,
    private val changeSwitchNotifications: SetEnableNotificationsUseCase

) : ViewModel() {

    //Live data del switch para habilitar/deshabilitar tutorial de inicio
    private val _switchTutorial = MutableLiveData<Boolean>()
    val switchTutorial: LiveData<Boolean> = _switchTutorial

    //Live data del switch para habilitar/deshabilitar tema oscuro desde la aplicación
    private val _switchDarkTheme = MutableLiveData<Boolean>()
    val switchDarkTheme: LiveData<Boolean> = _switchDarkTheme

    //Live data del switch para habilitar/deshabilitar notificaciones
    private val _switchNotifications = MutableLiveData<Boolean>()
    val switchNotifications: LiveData<Boolean> = _switchNotifications

    // Este LiveData controla el estado de si mostrar el tutorial
    private val _showTutorial = MutableLiveData<Boolean>()
    val showTutorial: LiveData<Boolean> = _showTutorial

    //Live data para mostrar lista de cuentas con opcion para borrar o modificar
    private val _deleteAccountOption = MutableLiveData<Boolean>()
    val deleteAccountOption: LiveData<Boolean> = _deleteAccountOption

    // LiveData para el campo de nombre de fichero
    private val _fileName = MutableLiveData<String>()
    val fileName: LiveData<String> = _fileName

    // Este LiveData controla el estado de si mostrar el Dialogo de exportar fichero csv
    private val _showExportDialog = MutableLiveData<Boolean>()
    val showExportDialog: LiveData<Boolean> = _showExportDialog

    init {

        getSwitchTutorial()
        getSwitchDarkTheme()
        getSwitchNotifications()
        getShowTutorial()
    }

    // Método para cambiar el valor del switch
    fun onSwitchTutorialClicked(checked: Boolean) {
        viewModelScope.launch {
            setShowTutorial.invoke(checked)
            changeSwitchTutorial(checked)
            _switchTutorial.value = checked

        }
    }

    fun onSwitchDarkThemeClicked(checked: Boolean) {
        viewModelScope.launch {
            changeSwitchDarkTheme(checked)
            _switchDarkTheme.value = checked
        }


    }
    fun onSelectAccountOption(delete:Boolean){
        _deleteAccountOption.value=delete
    }

    fun onSwitchNotificationsClicked(checked: Boolean) {
        viewModelScope.launch {
            changeSwitchNotifications(checked)
            _switchNotifications.value = checked
        }


    }
    fun onFileNameChanged(newName:String){
        _fileName.value = newName

    }
    fun onShowExportDialog(newValue:Boolean){
        _showExportDialog.value = newValue
    }

    // Obtener el estado del tutorial cuando inicie la aplicación
    fun getShowTutorial() {
        viewModelScope.launch {
            _showTutorial.value = getShowTutorial.invoke()
        }
    }

    // Obtener el estado del switch del tutorial
    fun getSwitchTutorial() {
        viewModelScope.launch {
            _switchTutorial.value = getSwitchTutorial.invoke()
        }
    }
    // Obtener el estado del switch del tutorial
    fun getSwitchDarkTheme() {
        viewModelScope.launch {
            _switchDarkTheme.value = getSwitchDarkTheme.invoke()
        }
    }
    // Obtener el estado del switch del tutorial
    fun getSwitchNotifications() {
        viewModelScope.launch {
            _switchNotifications.value = getNotificationsTutorial.invoke()
        }
    }

}