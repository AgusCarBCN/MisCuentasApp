package carnerero.agustin.cuentaappandroid.presentation.ui.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile.ProfileEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile.ProfileUiEvent
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.components.ProfileImageSection
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.components.ProfileInputsSection
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent

//import carnerero.agustin.cuentaappandroid.presentation.ui.profile.layouts.UpdateProfilePortraitLayout
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp

@Composable
fun UpdateProfileScreen(profileViewModel: ProfileViewModel) {
    val isPortrait = orientation == OrientationApp.Portrait
    val state by profileViewModel.uiState.collectAsStateWithLifecycle()
    val effects by profileViewModel.effect.collectAsStateWithLifecycle(initialValue = ProfileEffect.Idle)

    val updatedMessages = listOf(
        stringResource(id=R.string.photoUpdated),
        stringResource(id = R.string.userNameUpdated),  // Aquí obtienes el texto real del recurso
        stringResource(id = R.string.nameUpdated),
        stringResource(id = R.string.passwordUpdated)
    )
    Log.d("ProfileData", "profileScreen: ${state.name} ${state.username}")
    LaunchedEffect(effects) {
        when(effects){
           ProfileEffect.UpdateImagePhoto -> SnackBarController.sendEvent(
                SnackBarEvent(updatedMessages[0]
                )
            )
            ProfileEffect.UpdateUsernameMessage -> SnackBarController.sendEvent(
                SnackBarEvent(updatedMessages[1]
                )
            )
            ProfileEffect.UpdateNameMessage -> SnackBarController.sendEvent(
                SnackBarEvent(updatedMessages[2]
                )
            )
            ProfileEffect.UpdatePasswordMessage -> SnackBarController.sendEvent(
                SnackBarEvent(updatedMessages[3]
                )
            )
            else ->{

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
                    {profileViewModel.onUserEvent(ProfileUiEvent.OnProfileImageChange(it))},
                    {profileViewModel.onUserEvent(ProfileUiEvent.UpdatePhotoProfile(it))}
                )
                ProfileInputsSection(
                    state.name,
                    state.username,
                    state.password,
                    {profileViewModel.onUserEvent(ProfileUiEvent.OnNameChange(it))},
                    {profileViewModel.onUserEvent(ProfileUiEvent.OnUserNameChange(it))},
                    {profileViewModel.onUserEvent(ProfileUiEvent.OnPasswordChange(it))} ,
                    {profileViewModel.onUserEvent(ProfileUiEvent.UpdateNameProfile(it))},
                    {profileViewModel.onUserEvent(ProfileUiEvent.UpdateUsernameProfile(it))},
                    {profileViewModel.onUserEvent(ProfileUiEvent.UpdatePasswordProfile(it))}
                    )
            }
        } else {
            /*UpdateProfileLandscapeLayout(
                createViewModel
            )*/
        }
    }
}

/*
@Composable

fun UpdateProfileScreen(createViewModel: ProfileViewModel) {
    val updatedMessages = listOf(
        stringResource(id = R.string.userNameUpdated),  // Aquí obtienes el texto real del recurso
        stringResource(id = R.string.nameUpdated),
        stringResource(id = R.string.passwordUpdated),
        stringResource(id = R.string.photoUpdated)
    )


    val name by createViewModel.name.observeAsState("")
    val userName by createViewModel.username.observeAsState("")
    val password by createViewModel.password.observeAsState("")
    val selectedImageUri by createViewModel.selectedImageUri.observeAsState(null)
    val enableChangeImageButton by createViewModel.enableChangeImage.observeAsState(false)
    val enableNameButton by createViewModel.enableNameButton.observeAsState(false)
    val enableUserNameButton by createViewModel.enableUserNameButton.observeAsState(false)
    val enablePasswordButton by createViewModel.enablePasswordButton.observeAsState(false)


    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxWidthDp=maxWidth*0.85f

        Column(modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally) {

            ProfileImageWithCamera(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
                createViewModel)
            Row(
                modifier = Modifier.width(maxWidthDp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ModelButton(
                    text = "Change Photo",
                    MaterialTheme.typography.labelLarge, modifier = Modifier.width(220.dp),
                    enableChangeImageButton,
                    onClickButton = {
                        selectedImageUri?.let { createViewModel.saveImageUri(it,updatedMessages[3])}

                            createViewModel.buttonState(
                                false,
                                enableUserNameButton,
                                enableNameButton,
                                enablePasswordButton
                            )
                    }
                )
            }

            NewInputComponent(
                title = stringResource(id = R.string.userName),
                inputNewText = userName,
                R.string.userName,
                onNameTextFieldChanged = { createViewModel.onUserNameChanged(it) },
                type = BoardType.TEXT,
                enableUserNameButton,
                onChangeButtonClick = {
                        createViewModel.setUserDataProfile(UserProfile(name, userName, password),updatedMessages[0])
                        createViewModel.buttonState(
                            enableChangeImageButton,
                            false,
                            enableNameButton,
                            enablePasswordButton
                        )
                }
            )
            NewInputComponent(
                title = stringResource(id = R.string.name),
                inputNewText = name,
                R.string.name,
                onNameTextFieldChanged = { createViewModel.onNameChanged(it) },
                type = BoardType.TEXT,
                enableNameButton,
                onChangeButtonClick = {
                        createViewModel.setUserDataProfile(UserProfile(name, userName, password),updatedMessages[1])
                        createViewModel.buttonState(
                            enableChangeImageButton,
                            enableUserNameButton,
                            false,
                            enablePasswordButton
                        )
                }
            )
            NewInputComponent(
                title = stringResource(id = R.string.password),
                inputNewText = password,
                R.string.password,
                onNameTextFieldChanged = { createViewModel.onPasswordChanged(it) },
                type = BoardType.PASSWORD,
                enablePasswordButton,
                onChangeButtonClick = {
                        createViewModel.setUserDataProfile(UserProfile(name, userName, password),
                            updatedMessages[2])
                        createViewModel.buttonState(
                            enableChangeImageButton,
                            enableUserNameButton,
                            enableNameButton,
                            false
                        )
                },
                true
            )
        }

    }

}*/
/*@Composable
fun NewInputComponent(
    title: String,
    inputNewText: String,
    label:Int,
    onNameTextFieldChanged: (String) -> Unit,
    type: BoardType,
    enableInputButton: Boolean,
    onChangeButtonClick: () -> Unit,
    isPassword: Boolean = false
) {
    Column(verticalArrangement = Arrangement.Center) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 30.dp),
            text = title,
            color= colors.textHeadColor,
            style=MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Start
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 15.dp, start = 15.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextFieldComponent(
                modifier = Modifier.weight(0.60f),
                label = stringResource(label),
                inputText = inputNewText,
                onTextChange = onNameTextFieldChanged,
                type,
                isPassword
            )
            ModelButton(
                text = stringResource(id = R.string.change),
                MaterialTheme.typography.labelMedium,
                modifier = Modifier.weight(0.40f),
                enableInputButton,
                onClickButton = onChangeButtonClick
            )
        }
    }
}*/