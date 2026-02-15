package carnerero.agustin.cuentaappandroid.presentation.ui.createprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile.ProfileEffect
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile.ProfileUiEvent
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.profile.UserProfile
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent

@Composable

fun CreateContentSection(modifier: Modifier,
                         name:String,
                         userName:String,
                         password:String,
                         repeatPassword:String,
                         onNameChange:(String)->Unit,
                         onUserNameChange:(String)->Unit,
                         onPasswordChange:(String)->Unit,
                         onRepeatPasswordChange:(String)->Unit,
                         createProfile:(UserProfile)->Unit,
                         enableButton:Boolean
                         ){

    val modifierField = Modifier
        .fillMaxWidth(0.85f)
        .heightIn(min = 48.dp)
    Column(
        modifier

    ) {
        TextFieldComponent(
            modifier = modifierField ,
            stringResource(id = R.string.enterName),
            name,
            onTextChange = {
                onNameChange(it)},
            BoardType.TEXT,
            false
        )
        TextFieldComponent(
            modifier = modifierField,
            stringResource(id = R.string.enterUsername),
            userName,
            onTextChange = {
               onUserNameChange(it)
            },
            BoardType.TEXT,
            false
        )
        TextFieldComponent(
            modifier = modifierField,
            stringResource(id = R.string.enterPassword),
            password,
            onTextChange = {
                onPasswordChange(it)
            },
            BoardType.PASSWORD,
            true
        )
        TextFieldComponent(
            modifier = modifierField,
            stringResource(id = R.string.repeatpassword),
            repeatPassword,
            onTextChange = {
                onRepeatPasswordChange(it)},
            BoardType.PASSWORD,
            true
        )
        ModelButton(
            text = stringResource(id = R.string.confirmButton),
            MaterialTheme.typography.labelLarge,
            modifier = modifierField,
            enableButton,
            onClickButton = {
                val profile= UserProfile(name,userName,password)
                createProfile(profile)
            }
        )
    }
}