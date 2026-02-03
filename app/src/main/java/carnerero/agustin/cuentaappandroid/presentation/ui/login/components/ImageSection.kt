package carnerero.agustin.cuentaappandroid.presentation.ui.login.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.login.LoginViewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImageSection(modifier: Modifier,
    loginViewModel: LoginViewModel
) {
    val image by loginViewModel.selectedImageUriSaved.observeAsState(initial = null)
    Column(
        modifier = modifier
            .background(colors.imageBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = if (image == Uri.EMPTY)
                painterResource(R.drawable.contabilidad)
            else rememberAsyncImagePainter(image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
