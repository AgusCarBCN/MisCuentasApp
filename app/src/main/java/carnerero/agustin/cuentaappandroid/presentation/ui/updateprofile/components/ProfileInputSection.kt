package carnerero.agustin.cuentaappandroid.presentation.ui.updateprofile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.UserProfile

@Composable
fun ProfileInputsSection(
    createViewModel: ProfileViewModel

) {
    val updatedMessages = listOf(
        stringResource(id = R.string.userNameUpdated),  // Aquí obtienes el texto real del recurso
        stringResource(id = R.string.nameUpdated),
        stringResource(id = R.string.passwordUpdated)
    )

    val name by createViewModel.name.observeAsState("")
    val userName by createViewModel.username.observeAsState("")
    val password by createViewModel.password.observeAsState("")
    val enableChangeImageButton by createViewModel.enableChangeImage.observeAsState(false)
    val enableNameButton by createViewModel.enableNameButton.observeAsState(false)
    val enableUserNameButton by createViewModel.enableUserNameButton.observeAsState(false)
    val enablePasswordButton by createViewModel.enablePasswordButton.observeAsState(false)


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

@Composable
private fun NewInputComponent(
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
}