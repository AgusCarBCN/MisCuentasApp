package carnerero.agustin.cuentaappandroid.presentation.ui.profile.components

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.createprofile.ProfileImageWithCamera

@Composable
fun ProfileImageSection(
    selectedImageUri: Uri?,
    onSelectedImageUri:(Uri)->Unit,
    updateImage:(Uri)->Unit
    ) {

    ProfileImageWithCamera(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        selectedImageUri
    ) { onSelectedImageUri(it) }

    ModelButton(
        text = stringResource(R.string.change),
        MaterialTheme.typography.labelLarge,
        modifier = Modifier.width(220.dp),
        true
    ) {
        selectedImageUri?.let {
            updateImage(it)
        }

    }
}
