package carnerero.agustin.cuentaappandroid.presentation.ui.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.components.ProfileImageSection
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.components.ProfileInputsSection
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent

//import carnerero.agustin.cuentaappandroid.presentation.ui.profile.layouts.UpdateProfilePortraitLayout
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val isPortrait = orientation == OrientationApp.Portrait
    val state by profileViewModel.uiState.collectAsStateWithLifecycle()
    val effects by profileViewModel.effect.collectAsStateWithLifecycle(initialValue = ProfileEffect.Idle)

    val updatedMessages = listOf(
        stringResource(id = R.string.photoUpdated),
        stringResource(id = R.string.userNameUpdated),  // Aquí obtienes el texto real del recurso
        stringResource(id = R.string.nameUpdated),
        stringResource(id = R.string.passwordUpdated)
    )
    Log.d("ProfileData", "profileScreen: ${state.name} ${state.username}")
    LaunchedEffect(effects) {
        when (effects) {
            ProfileEffect.UpdateImagePhoto -> SnackBarController.sendEvent(
                SnackBarEvent(
                    updatedMessages[0]
                )
            )

            ProfileEffect.UpdateUsernameMessage -> SnackBarController.sendEvent(
                SnackBarEvent(
                    updatedMessages[1]
                )
            )

            ProfileEffect.UpdateNameMessage -> SnackBarController.sendEvent(
                SnackBarEvent(
                    updatedMessages[2]
                )
            )

            ProfileEffect.UpdatePasswordMessage -> SnackBarController.sendEvent(
                SnackBarEvent(
                    updatedMessages[3]
                )
            )

            else -> {

            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
    ) {
        if (isPortrait) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileImageSection(
                    state.selectedImageUri,
                    { profileViewModel.onUserEvent(ProfileUiEvent.OnProfileImageChange(it)) },
                    { profileViewModel.onUserEvent(ProfileUiEvent.UpdatePhotoProfile(it)) }
                )
                ProfileInputsSection(
                    state.name,
                    state.username,
                    state.password,
                    { profileViewModel.onUserEvent(ProfileUiEvent.OnNameChange(it)) },
                    { profileViewModel.onUserEvent(ProfileUiEvent.OnUserNameChange(it)) },
                    { profileViewModel.onUserEvent(ProfileUiEvent.OnPasswordChange(it)) },
                    { profileViewModel.onUserEvent(ProfileUiEvent.UpdateNameProfile(it)) },
                    { profileViewModel.onUserEvent(ProfileUiEvent.UpdateUsernameProfile(it)) },
                    { profileViewModel.onUserEvent(ProfileUiEvent.UpdatePasswordProfile(it)) }
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimens.extraLarge)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ProfileImageSection(
                        state.selectedImageUri,
                        { profileViewModel.onUserEvent(ProfileUiEvent.OnProfileImageChange(it)) },
                        { profileViewModel.onUserEvent(ProfileUiEvent.UpdatePhotoProfile(it)) }
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfileInputsSection(
                        state.name,
                        state.username,
                        state.password,
                        { profileViewModel.onUserEvent(ProfileUiEvent.OnNameChange(it)) },
                        { profileViewModel.onUserEvent(ProfileUiEvent.OnUserNameChange(it)) },
                        { profileViewModel.onUserEvent(ProfileUiEvent.OnPasswordChange(it)) },
                        { profileViewModel.onUserEvent(ProfileUiEvent.UpdateNameProfile(it)) },
                        { profileViewModel.onUserEvent(ProfileUiEvent.UpdateUsernameProfile(it)) },
                        { profileViewModel.onUserEvent(ProfileUiEvent.UpdatePasswordProfile(it)) }
                    )
                }
                Spacer(modifier = Modifier.width(32.dp))
            }
        }
    }
}

