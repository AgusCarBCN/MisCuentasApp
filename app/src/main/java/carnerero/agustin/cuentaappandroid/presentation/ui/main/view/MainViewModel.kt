package carnerero.agustin.cuentaappandroid.presentation.ui.main.view

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

    fun onClickOption(route:Routes){

        viewModelScope.launch {
             _uiState.update { current->
                 current.copy(
                     route=route
                 )
             }
            _effect.emit(MainEffects.NavToScreen(route.route))

        }
    }

    fun onTitleChange (currentRoute:String){
        val allRoutes = listOf(
            Routes.Home,
            Routes.NewIncome,
            Routes.NewExpense,
            Routes.Transfer,
            Routes.Statistics,
            Routes.SpendingControl,
            Routes.Calculator,
            Routes.About,
            Routes.Search,
            Routes.Profile,
            Routes.Settings,
            Routes.ModifyRecords,
            Routes.DeleteRecords,
            Routes.ModifyAccount,
            Routes.DeleteAccount,
            Routes.AddAccount

        )
        val title= allRoutes
            .find { it.route == currentRoute }
            ?.labelResource
            ?: R.string.home
        _uiState.update { current->
            current.copy(
                title=title
            )
        }
    }

}

