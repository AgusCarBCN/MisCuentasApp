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
                         createViewModel: ProfileViewModel,
                         categoriesViewModel: CategoriesViewModel,
                         navToCreateAccounts:()->Unit
                         ){

    val state by createViewModel.uiState.collectAsStateWithLifecycle()
    val eventEffect by createViewModel.effect.collectAsState(initial = ProfileEffect.Idle)
    val messageProfileCreated = message(resource = R.string.profileCreated)
    val enableButton=state.name.isNotBlank() &&
            state.username.length >= 3 &&
            state.password.length >= 6 &&
            state.password == state.repeatPassword

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
            state.name,
            onTextChange = {
                createViewModel.onUserEvent(ProfileUiEvent.OnNameChange(it))
            },
            BoardType.TEXT,
            false
        )
        TextFieldComponent(
            modifier = modifierField,
            stringResource(id = R.string.enterUsername),
            state.username,
            onTextChange = {
                createViewModel.onUserEvent(ProfileUiEvent.OnUserNameChange(it))
            },
            BoardType.TEXT,
            false
        )
        TextFieldComponent(
            modifier = modifierField,
            stringResource(id = R.string.enterPassword),
            state.password,
            onTextChange = {
                createViewModel.onUserEvent(ProfileUiEvent.OnPasswordChange(it))
            },
            BoardType.PASSWORD,
            true
        )
        TextFieldComponent(
            modifier = modifierField,
            stringResource(id = R.string.repeatpassword),
            state.repeatPassword,
            onTextChange = {
                createViewModel.onUserEvent(ProfileUiEvent.OnRepeatPasswordChange(it))
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
                val profile= UserProfile(state.name,state.username,state.password)
                createViewModel.onUserEvent(ProfileUiEvent.OnCreateProfile(profile))

               // createViewModel.createProfile(profile)
                categoriesViewModel.populateCategories()
            }
        )
    }
}