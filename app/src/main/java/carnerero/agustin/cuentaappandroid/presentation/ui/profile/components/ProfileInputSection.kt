package carnerero.agustin.cuentaappandroid.presentation.ui.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile.UserProfile

@Composable
fun ProfileInputsSection(
     name:String,
     username:String,
     password:String,
     onNameChange:(String)->Unit,
     onUserNameChange:(String)->Unit,
     onPasswordChange:(String)->Unit,
     updateName:(String)->Unit,
     updateUserName:(String)->Unit,
     updatePassword:(String)->Unit
) {

    NewInputComponent(
        title = stringResource(id = R.string.userName),
        inputNewText = username,
        R.string.userName,
        onNameTextFieldChanged = {
            //createViewModel.onUserNameChanged(it)
            onUserNameChange(it)},
        type = BoardType.TEXT,
        true,
        onChangeButtonClick = {
            //createViewModel.setUserDataProfile(UserProfile(name, userName, password),updatedMessages[0])
            updateUserName(username)
        }
    )
    NewInputComponent(
        title = stringResource(id = R.string.name),
        inputNewText = name,
        R.string.name,
        onNameTextFieldChanged = {
            //createViewModel.onNameChanged(it)
               onNameChange(it)        },
        type = BoardType.TEXT,
        true,
        onChangeButtonClick = {
           updateName(name)
        }
    )
    NewInputComponent(
        title = stringResource(id = R.string.password),
        inputNewText = password,
        R.string.password,
        onNameTextFieldChanged = {
            //createViewModel.onPasswordChanged(it)
              onPasswordChange(it)                   },
        type = BoardType.PASSWORD,
        true,
        onChangeButtonClick = {
           updatePassword(password)
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