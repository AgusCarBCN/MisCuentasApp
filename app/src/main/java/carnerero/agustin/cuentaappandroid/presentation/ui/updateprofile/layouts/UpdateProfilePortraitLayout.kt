package carnerero.agustin.cuentaappandroid.presentation.ui.updateprofile.layouts

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.updateprofile.components.ProfileImageSection
import carnerero.agustin.cuentaappandroid.presentation.ui.updateprofile.components.ProfileInputsSection

@Composable
 fun UpdateProfilePortraitLayout(
    viewModel: ProfileViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImageSection(
            viewModel

        )

        ProfileInputsSection(
            viewModel
        )
    }
}
