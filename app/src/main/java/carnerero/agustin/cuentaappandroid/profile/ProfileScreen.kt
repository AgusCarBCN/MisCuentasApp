package carnerero.agustin.cuentaappandroid.profile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.SnackBarController
import carnerero.agustin.cuentaappandroid.SnackBarEvent
import carnerero.agustin.cuentaappandroid.components.BoardType
import carnerero.agustin.cuentaappandroid.components.ModelButton
import carnerero.agustin.cuentaappandroid.components.TextFieldComponent
import carnerero.agustin.cuentaappandroid.createprofile.ProfileImageWithCamera
import carnerero.agustin.cuentaappandroid.createprofile.ProfileViewModel
import carnerero.agustin.cuentaappandroid.main.model.UserProfile
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
import kotlinx.coroutines.launch


@Composable

fun ProfileScreen(createViewModel: ProfileViewModel) {
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

    val scope = rememberCoroutineScope()



    val snackBarHostState = remember { SnackbarHostState() }


    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {


        ProfileImageWithCamera(createViewModel)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ModelButton(
                text = "Change Photo",
                MaterialTheme.typography.labelLarge, modifier = Modifier.width(220.dp),
                enableChangeImageButton,
                onClickButton = {
                    scope.launch {
                        selectedImageUri?.let { createViewModel.saveImageUri(it) }
                        SnackBarController.sendEvent(event = SnackBarEvent(updatedMessages[3]))
                        createViewModel.buttonState(false,enableUserNameButton,enableNameButton,enablePasswordButton)
                    }

                    Log.d("SaveFromChange", selectedImageUri.toString())
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
                scope.launch {
                    createViewModel.setUserDataProfile(UserProfile(name, userName, password))
                    SnackBarController.sendEvent(event = SnackBarEvent(updatedMessages[0]))
                    createViewModel.buttonState(enableChangeImageButton,false,enableNameButton,enablePasswordButton)
                }

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
                scope.launch {
                    createViewModel.setUserDataProfile(UserProfile(name, userName, password))
                    SnackBarController.sendEvent(event = SnackBarEvent(updatedMessages[1]))
                    createViewModel.buttonState(enableChangeImageButton,enableUserNameButton,false,enablePasswordButton)
                }
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
                scope.launch {
                    createViewModel.setUserDataProfile(UserProfile(name, userName, password))
                    SnackBarController.sendEvent(event = SnackBarEvent(updatedMessages[2]))
                    createViewModel.buttonState(enableChangeImageButton,enableUserNameButton,enableNameButton,false)
                }
            },
            true
        )

    }
    SnackbarHost(hostState = snackBarHostState)
}


@Composable
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
            color= LocalCustomColorsPalette.current.textHeadColor,
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
}
