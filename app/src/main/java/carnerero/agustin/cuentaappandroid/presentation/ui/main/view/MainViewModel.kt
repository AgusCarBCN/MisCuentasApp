package carnerero.agustin.cuentaappandroid.presentation.ui.main.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.domain.datastore.GetEnableNotificationsUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetPhotoFromUriUseCase
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Anotación para que Hilt pueda proporcionar esta clase como ViewModel.
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getImage: GetPhotoFromUriUseCase,
    private val getNotificationGranted: GetEnableNotificationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    private val _effect = MutableSharedFlow<MainEffects>()
    val effect = _effect.asSharedFlow()


    // MutableStateFlow que contiene el estado del permiso de notificaciones.
    private val _notificationPermissionGranted = MutableStateFlow(false)
    val notificationPermissionGranted: StateFlow<Boolean> = _notificationPermissionGranted

    private val _showExitDialog=MutableStateFlow(false)
    val showExitDialog: MutableStateFlow<Boolean> = _showExitDialog

    private val _isLandScape=MutableStateFlow(false)
    val isLandScape: MutableStateFlow<Boolean> = _isLandScape


    private val _title = MutableLiveData<Int>()
    val title: LiveData<Int> = _title

    init {
        observeNotificationPermission()
    }

    private fun observeNotificationPermission() {
        viewModelScope.launch {
            val image=getImage.invoke()
            getNotificationGranted().collect { isGranted ->
                _uiState.update {
                    it.copy(
                        isGranted = isGranted,
                        image = image)
                }
            }
        }
    }
    fun onUserEvent(event: MainUserEvents){
        when(event){
            MainUserEvents.CloseExitDialog -> closeDialog()
            is MainUserEvents.UpdateTitle -> updateTitle(event.newTitle)
            MainUserEvents.OpenExitDialog -> openDialog()
            is MainUserEvents.OnClickOption -> onClickOption(event.route)
        }
    }

    fun closeDialog(){
        _uiState.update { current->
            current.copy(
                showExitDialog=false
            )
        }
    }

    fun openDialog(){
        _uiState.update { current->
            current.copy(
                showExitDialog=true
            )
        }
    }

    fun updateTitle(newTitle: Int) {
       _uiState.update { current->
           current.copy(
               resourceTitle =newTitle
           )
       }
    }

    fun onClickOption(route: Routes){
        viewModelScope.launch {
            _effect.emit(MainEffects.NavToScreen(route))
        }
    }


}

