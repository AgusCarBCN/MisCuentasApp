package carnerero.agustin.cuentaappandroid.presentation.ui.updateprofile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.createprofile.ProfileImageWithCamera

@Composable
fun ProfileImageSection(
    createProfileViewModel: ProfileViewModel

) {
    val message= stringResource(id = R.string.photoUpdated)
    val selectedImageUri by createProfileViewModel.selectedImageUri.observeAsState(null)
    val enableChangeImage by createProfileViewModel.enableChangeImage.observeAsState(false)
    val enableName by createProfileViewModel.enableNameButton.observeAsState(false)
    val enableUserName by createProfileViewModel.enableUserNameButton.observeAsState(false)
    val enablePassword by createProfileViewModel.enablePasswordButton.observeAsState(false)
    ProfileImageWithCamera(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        createProfileViewModel
    )

    ModelButton(
        text = stringResource(R.string.change),
        MaterialTheme.typography.labelLarge,
        modifier = Modifier.width(220.dp),
        enableChangeImage
    ) {
        selectedImageUri?.let {
            createProfileViewModel.saveImageUri(it, message)
        }
        createProfileViewModel.buttonState(
            false,
            enableUserName,
            enableName,
            enablePassword
        )
    }
}
