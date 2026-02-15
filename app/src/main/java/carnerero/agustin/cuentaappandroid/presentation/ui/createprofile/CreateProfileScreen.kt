package carnerero.agustin.cuentaappandroid.presentation.ui.createprofile


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountEvent
import carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model.CreateAccountUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile.ProfileEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile.ProfileUiEvent
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp


@Composable
fun CreateProfileScreen(
    createViewModel: ProfileViewModel,
    navToCreateAccounts: () -> Unit
) {
    val state by createViewModel.uiState.collectAsStateWithLifecycle()
    val eventEffect by createViewModel.effect.collectAsState(initial = ProfileEffect.Idle)
    val messageProfileCreated = message(resource = R.string.profileCreated)
    val isLandscape = orientation == OrientationApp.Landscape
    val enableButton=state.enableButton
    LaunchedEffect(eventEffect) {
        when (eventEffect) {
            is ProfileEffect.NavigateToCreateAccounts -> {
                navToCreateAccounts()
            }
            is ProfileEffect.ProfileSavedMessage ->{
                SnackBarController.sendEvent(SnackBarEvent(messageProfileCreated))
            }
            else -> {}
        }
    }
    BoxWithConstraints(Modifier.fillMaxSize()) {

        val contentWidth=maxWidth*0.5f
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.backgroundPrimary)
            ) {
                ProfileImageWithCamera(
                    modifier = Modifier
                        .width(contentWidth)
                        .wrapContentHeight()
                        .padding(top = 50.dp),
                        state.selectedImageUriSaved,
                    {createViewModel.onUserEvent(ProfileUiEvent.OnProfileImageChange(it))}
                    )
                CreateContentSection(
                    Modifier
                        .width(contentWidth)
                        .verticalScroll(rememberScrollState()
                            ),
                    state.name,
                    state.username,
                    state.password,
                    state.repeatPassword,
                    {createViewModel.onUserEvent(ProfileUiEvent.OnUserNameChange(it))},
                    {createViewModel.onUserEvent(ProfileUiEvent.OnUserNameChange(it))},
                    {createViewModel.onUserEvent(ProfileUiEvent.OnPasswordChange(it))},
                    {createViewModel.onUserEvent(ProfileUiEvent.OnRepeatPasswordChange(it))},
                    {createViewModel.onUserEvent(ProfileUiEvent.OnCreateProfile(it))},
                        enableButton
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.backgroundPrimary),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                ProfileImageWithCamera(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                    .padding(bottom = 20.dp),
                    state.selectedImageUriSaved,
                    {createViewModel.onUserEvent(ProfileUiEvent.OnProfileImageChange(it))})
                CreateContentSection(
                    Modifier
                        .verticalScroll(rememberScrollState()
                        ),
                    state.name,
                    state.username,
                    state.password,
                    state.repeatPassword,
                    {createViewModel.onUserEvent(ProfileUiEvent.OnNameChange(it))},
                    {createViewModel.onUserEvent(ProfileUiEvent.OnUserNameChange(it))},
                    {createViewModel.onUserEvent(ProfileUiEvent.OnPasswordChange(it))},
                    {createViewModel.onUserEvent(ProfileUiEvent.OnRepeatPasswordChange(it))},
                    {createViewModel.onUserEvent(ProfileUiEvent.OnCreateProfile(it))},
                    enableButton
                )
            }
        }
    }
}

