package carnerero.agustin.cuentaappandroid.presentation.ui.main.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.IconOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Anotación para que Hilt pueda proporcionar esta clase como ViewModel.
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // MutableStateFlow que contiene la pantalla seleccionada.
    // Inicialmente se establece en "Home".
    private val _selectedScreen = MutableStateFlow(IconOptions.HOME)

    // MutableStateFlow que contiene el estado del permiso de notificaciones.
    private val _notificationPermissionGranted = MutableStateFlow(false)
    val notificationPermissionGranted: StateFlow<Boolean> = _notificationPermissionGranted

    // Exposición pública del StateFlow, permite a los composables observar cambios en el estado.
    // StateFlow es un flujo que siempre mantiene su último valor emitido.
    val selectedScreen: MutableStateFlow<IconOptions> = _selectedScreen

    //MutableStateFlow de entrada seleccionada a modificar
    private val _entryDTOSelected = MutableLiveData<EntryDTO?>()

    private val _showExitDialog=MutableStateFlow(false)
    val showExitDialog: MutableStateFlow<Boolean> = _showExitDialog

    private val _isLandScape=MutableStateFlow(false)
    val isLandScape: MutableStateFlow<Boolean> = _isLandScape



    private val _showDeleteAccountDialog= MutableStateFlow(false)
    val showDeleteAccountDialog: MutableStateFlow<Boolean> = _showDeleteAccountDialog

    private val _title = MutableLiveData<Int>()
    val title: LiveData<Int> = _title

    init {
        _title.value = R.string.home
    }

        // Función que permite cambiar la pantalla seleccionada.
    // Utiliza viewModelScope para lanzar una corrutina y emitir un nuevo valor.
    fun selectScreen(screen: IconOptions){
        viewModelScope.launch {
            // Emitir el nuevo valor al StateFlow.
            _selectedScreen.emit(screen)

        }
    }
    fun isLandScape(isLandScape:Boolean):Boolean{
        return isLandScape
    }
    fun updateTitle(newTitle: Int) {
        _title.value = newTitle
    }

    fun showExitDialog(newValue:Boolean){
        viewModelScope.launch {
            _showExitDialog.emit(newValue)
        }
    }
    fun showDeleteAccountDialog(newValue:Boolean) {

        viewModelScope.launch {
            _showDeleteAccountDialog.emit(newValue)
        }
    }
    fun updateNotificationPermission(isGranted: Boolean) {
        _notificationPermissionGranted.value = isGranted
    }
}

