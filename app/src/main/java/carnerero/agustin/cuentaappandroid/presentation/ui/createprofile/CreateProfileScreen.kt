package carnerero.agustin.cuentaappandroid.presentation.ui.createprofile


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import carnerero.agustin.cuentaappandroid.R
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.UserProfile
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.ProfileViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun CreateProfileComponent(
    createViewModel: ProfileViewModel = hiltViewModel (),
    navToBackLogin: () -> Unit,
    navToCreateAccounts: () -> Unit
) {


    val name by createViewModel.name.observeAsState("")
    val userName by createViewModel.username.observeAsState("")
    val password by createViewModel.password.observeAsState("")
    val selectedImageUri by createViewModel.selectedImageUri.observeAsState(null)
    //val selectedImageUriSaved by createViewModel.selectedImageUri.observeAsState( null)
    val repeatPassword by createViewModel.repeatPassword.observeAsState("")
    val scope = rememberCoroutineScope()
    val enableButton by createViewModel.enableButton.observeAsState(false)

    val errorWritingDataStore = message(resource = R.string.errorwritingdatastore)
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColorsPalette.current.backgroundPrimary) // Reemplaza con tu color de fondo
    ) {
        val (profileImage, box) = createRefs()

        Column(modifier = Modifier
            .constrainAs(profileImage) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)               // Termina en el lado derecho del padre
                start.linkTo(parent.start)
                bottom.linkTo(box.top)
                // Parte inferior anclada al padre
            }) {

            ProfileImageWithCamera(createViewModel)
        }
        Column(modifier = Modifier
            .constrainAs(box) {
                top.linkTo(profileImage.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }

        ) {

            TextFieldComponent(
                modifier = Modifier.width(360.dp),
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
                modifier = Modifier.width(360.dp),
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
                modifier = Modifier.width(360.dp),
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
                modifier = Modifier.width(360.dp),
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
            ModelButton(text = stringResource(id = R.string.confirmButton),
                MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(360.dp),
                enableButton,
                onClickButton = {
                    navToCreateAccounts()

                    scope.launch(Dispatchers.IO) {
                        try {
                            createViewModel.setUserDataProfile(
                                UserProfile(
                                    name, userName, password
                                )
                            )
                            selectedImageUri?.let { createViewModel.saveImageUri(it) }
                        } catch (_: Exception) {
                            withContext(Dispatchers.Main) {
                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        errorWritingDataStore
                                    )
                                )
                            }
                        }
                    }
                }
            )

            ModelButton(text = stringResource(id = R.string.backButton),
                MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(360.dp),
                true,
                onClickButton = {
                    navToBackLogin()
                }
            )
        }
    }
}

@Composable

fun ProfileImageWithCamera(viewModel: ProfileViewModel) {

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
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), // Para que se ajuste al contenido
        contentAlignment = Alignment.Center
    ) {
        // Imagen de perfil dentro de una Card circular

        Card(
            modifier = Modifier
                .size(250.dp),
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





