package carnerero.agustin.cuentaappandroid.presentation.ui.createprofile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest




@Composable
fun ProfileImageWithCamera(modifier: Modifier,viewModel: ProfileViewModel) {

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val selectedImageUriSavedFromFile by viewModel.selectedImageUriSaved.observeAsState(null)
    // Llama a `onImageNoSelected()` si no hay una imagen seleccionada o guardada
    //viewModel.onImageNoSelected()
    // Lanza el selector de imágenes
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        selectedImageUri?.let { viewModel.onImageSelected(it) }
    }

    Box(
        modifier, // Para que se ajuste al contenido
        contentAlignment = Alignment.Center,

        ) {   // Imagen de perfil dentro de una Card circular

            Card(
                modifier = Modifier
                    .size(250.dp)
                    .padding(top = 20.dp),
                shape = CircleShape, // Hace que el Card sea circular
                // Reemplaza lightYellow
            ) {
                if (selectedImageUri == null) {
                    Image(
                        painter = if (selectedImageUriSavedFromFile == null || selectedImageUriSavedFromFile == Uri.EMPTY) painterResource(
                            id = R.drawable.contabilidad
                        )
                        else rememberAsyncImagePainter(model = selectedImageUriSavedFromFile), // Reemplaza con tu imagen de placeholder
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize() // La imagen ocupa todo el Card
                    )
                }
                // Imagen de perfil
                selectedImageUri?.let { uri ->

                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(uri)
                                .crossfade(true)
                                .build()
                        ),
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize() // La imagen ocupa todo el Card

                    )
                }
            }

            // Ícono de cámara superpuesto en la esquina inferior izquierda
            Card(
                modifier = Modifier
                    .size(50.dp) // Tamaño del ícono de cámara
                    .align(Alignment.Center) // Posición en la esquina inferior izquierda
                    .offset(x = 80.dp, y = (100).dp),
                shape = CircleShape,

                ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera), // Reemplaza con tu ícono de cámara
                    contentDescription = "Camera Icon",
                    tint = LocalCustomColorsPalette.current.iconCamara, // Reemplaza con tu color de ícono
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LocalCustomColorsPalette.current.disableButton)
                        .clickable {
                            photoPickerLauncher.launch("image/*")

                        }
                )

            }
        }
    }

