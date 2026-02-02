package carnerero.agustin.cuentaappandroid.presentation.ui.createprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel

@Composable

fun CreateContentSection(modifier: Modifier,
                         createViewModel: ProfileViewModel,
                         categoriesViewModel: CategoriesViewModel,
                         navToCreateAccounts:()->Unit,
                         navToLogin:()->Unit){

    val name by createViewModel.name.observeAsState("")
    val userName by createViewModel.username.observeAsState("")
    val password by createViewModel.password.observeAsState("")
    val repeatPassword by createViewModel.repeatPassword.observeAsState("")
    val enableButton by createViewModel.enableButton.observeAsState(false)
    val messageProfileCreated = message(resource = R.string.profileCreated)
    val modifierField = Modifier
        .fillMaxWidth(0.85f)
        .heightIn(min = 48.dp)
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        TextFieldComponent(
            modifier = modifierField ,
            stringResource(id = R.string.enterName),
            name,
            onTextChange = {
                createViewModel.onTextFieldsChanged(
                    it,
                    userName,
                    password,
                    repeatPassword
                )
            },
            BoardType.TEXT,
            false
        )
        TextFieldComponent(
            modifier = modifierField,
            stringResource(id = R.string.enterUsername),
            userName,
            onTextChange = {
                createViewModel.onTextFieldsChanged(
                    name,
                    it,
                    password,
                    repeatPassword
                )
            },
            BoardType.TEXT,
            false
        )
        TextFieldComponent(
            modifier = modifierField,
            stringResource(id = R.string.enterPassword),
            password,
            onTextChange = {
                createViewModel.onTextFieldsChanged(
                    name,
                    userName,
                    it,
                    repeatPassword
                )
            },
            BoardType.PASSWORD,
            true
        )
        TextFieldComponent(
            modifier = modifierField,
            stringResource(id = R.string.repeatpassword),
            repeatPassword,
            onTextChange = {
                createViewModel.onTextFieldsChanged(
                    name,
                    userName,
                    password,
                    it
                )
            },
            BoardType.PASSWORD,
            true
        )
        ModelButton(
            text = stringResource(id = R.string.confirmButton),
            MaterialTheme.typography.labelLarge,
            modifier = modifierField,
            enableButton,
            onClickButton = {
                navToCreateAccounts()
                createViewModel.createProfile(
                    name,
                    userName,
                    password,
                    messageProfileCreated
                )
                categoriesViewModel.populateCategories()
            }
        )

        ModelButton(
            text = stringResource(id = R.string.backButton),
            MaterialTheme.typography.labelLarge,
            modifier = modifierField,
            true,
            onClickButton = {
                navToLogin()
            }
        )
    }
}