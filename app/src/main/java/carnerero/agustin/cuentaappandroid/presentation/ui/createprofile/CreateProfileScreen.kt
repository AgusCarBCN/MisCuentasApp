package carnerero.agustin.cuentaappandroid.presentation.ui.createprofile


import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette


@Composable
fun CreateProfileScreen(
    createViewModel: ProfileViewModel,
    categoriesViewModel: CategoriesViewModel,
    navToBackLogin: () -> Unit,
    navToCreateAccounts: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    BoxWithConstraints(Modifier.fillMaxSize()) {

        val contentWidth=maxWidth*0.5f
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LocalCustomColorsPalette.current.backgroundPrimary)
            ) {
                ProfileImageWithCamera(
                    modifier = Modifier
                        .width(contentWidth)
                        .wrapContentHeight()
                        .padding(top = 50.dp)
                        ,
                    createViewModel)
                CreateContentSection(
                    Modifier
                        .width(contentWidth)
                        .verticalScroll(rememberScrollState()
                            ),
                    createViewModel,
                    categoriesViewModel,
                    navToCreateAccounts
                )
                { navToBackLogin }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LocalCustomColorsPalette.current.backgroundPrimary),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                ProfileImageWithCamera(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                    .padding(bottom = 20.dp),
                    createViewModel)
                CreateContentSection(
                    Modifier
                        .verticalScroll(rememberScrollState()),
                    createViewModel,
                    categoriesViewModel,
                    navToCreateAccounts
                )
                { navToBackLogin }
            }
        }
    }
}

