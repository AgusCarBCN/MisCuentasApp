package carnerero.agustin.cuentaappandroid.presentation.ui.login.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImageSection(
    modifier: Modifier,
    imageUri:Uri?
) {

    Column(
        modifier = modifier
            .background(colors.imageBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = if (imageUri == Uri.EMPTY)
                painterResource(R.drawable.contabilidad)
            else rememberAsyncImagePainter(imageUri),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
