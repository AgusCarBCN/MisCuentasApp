package carnerero.agustin.cuentaappandroid.presentation.ui.login


import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import carnerero.agustin.cuentaappandroid.R
import coil.compose.rememberAsyncImagePainter
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun LoginComponent(
    loginViewModel: LoginViewModel = hiltViewModel(),
    modifier: Modifier,
    navToMain: () -> Unit,
) {

    val image by loginViewModel.selectedImageUriSaved.observeAsState(initial = null)
    val name by loginViewModel.name.observeAsState("")
    val userName by loginViewModel.userName.observeAsState("")
    val password by loginViewModel.password.observeAsState("")

    val userNameNewPassword by loginViewModel.userNameNewPassword.observeAsState("")
    val newPassword by loginViewModel.newPassword.observeAsState("")

    val enableLoginButton by loginViewModel.enableLoginButton.observeAsState(false)
    val enableConfirmButton by loginViewModel.enableConfirmButton.observeAsState(false)

    val enableNewPasswordFields by loginViewModel.enableNewPasswordFields.observeAsState(false)

    val validateLogin by loginViewModel.validateLoginButton.observeAsState(false)
    val validateConfirm by loginViewModel.validateConfirmButton.observeAsState(false)

    val scope = rememberCoroutineScope()
    /* Se usa para gestionar el estado del Snackbar. Esto te permite mostrar y controlar el Snackbar
     desde cualquier parte de tu UI.*/
    val messageInvalidLogin= message(resource = R.string.inValidLogin)

    loginViewModel.getLoginImage()
    ConstraintLayout(
        modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.lightYellow))
    ) {
        // Crear referencias para las cajas
        val (imageBox, loginBox) = createRefs()

        // Caja de imagen en la parte superior, ocupando el 50% de la altura
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(LocalCustomColorsPalette.current.imageBackground)
                .constrainAs(imageBox) {
                    top.linkTo(parent.top)          // Parte superior anclada al padre
                    start.linkTo(parent.start)      // Empieza en el lado izquierdo del padre
                    end.linkTo(parent.end)          // Termina en el lado derecho del padre
                    bottom.linkTo(loginBox.top)     // Parte inferior anclada al loginBox
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = if (image == Uri.EMPTY) painterResource(id = R.drawable.contabilidad)
                else rememberAsyncImagePainter(image), // Carga la imagen desde el Uri ,
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = if (image == Uri.EMPTY) Modifier
                    .size(250.dp)
                    .background(LocalCustomColorsPalette.current.imageBackground)
                else Modifier
                    .fillMaxSize()
            )

        }

        // Caja de login en la parte inferior, ocupando el otro 50% de la altura
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(LocalCustomColorsPalette.current.backgroundPrimary)  // Color de fondo
                .constrainAs(loginBox) {
                    top.linkTo(imageBox.bottom)          // Parte superior anclada al imageBox
                    start.linkTo(parent.start)           // Empieza en el lado izquierdo del padre
                    end.linkTo(parent.end)               // Termina en el lado derecho del padre
                    bottom.linkTo(parent.bottom)         // Parte inferior anclada al padre
                },

            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!enableNewPasswordFields) {
                Text(
                    text = loginViewModel.getGreeting(name),
                    fontSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_title_medium).toSp() },
                    color = LocalCustomColorsPalette.current.textColor,
                    fontWeight = FontWeight.Bold
                )
                TextFieldComponent(
                    modifier = Modifier.width(360.dp),
                    stringResource(id = R.string.enterUsername),
                    userName,
                    onTextChange = { loginViewModel.onLoginChanged(it, password) },
                    BoardType.TEXT
                )
                TextFieldComponent(
                    modifier = Modifier.width(360.dp),
                    stringResource(id =R.string.enterPassword),
                    password,
                    onTextChange = { loginViewModel.onLoginChanged(userName, it) },
                    BoardType.PASSWORD,
                    true
                )
                ModelButton(text = stringResource(id = R.string.loginButton),
                    MaterialTheme.typography.labelLarge,
                    modifier = Modifier.width(360.dp),
                    enableLoginButton,
                    onClickButton = {
                        if (validateLogin) {
                            navToMain()
                        } else{
                            scope.launch(Dispatchers.Main) {
                                SnackBarController.sendEvent(event = SnackBarEvent(messageInvalidLogin))
                            }
                        }

                    }
                )

                TextButton(
                    onClick = {
                        loginViewModel.onEnableNewPasswordFields(true)
                    },
                    content = {
                        Text(
                            text = stringResource(id = R.string.forgotpassword),
                            style=MaterialTheme.typography.labelSmall,
                            color = LocalCustomColorsPalette.current.textColor
                        )
                    }
                )

            }
            if (enableNewPasswordFields) {
                TextFieldComponent(
                    modifier = Modifier.width(360.dp),
                    stringResource(id = R.string.requestuser),
                    userNameNewPassword,
                    onTextChange = { loginViewModel.onUpdatePasswordChange(it, newPassword) },
                    BoardType.TEXT
                )

                TextFieldComponent(
                    modifier = Modifier.width(360.dp),
                    stringResource(id = R.string.newpassword),
                    newPassword,
                    onTextChange = {
                        loginViewModel.onUpdatePasswordChange(
                            userNameNewPassword,
                            it
                        )
                    },
                    BoardType.PASSWORD,
                    true
                )
                ModelButton(text = stringResource(id = R.string.confirmButton),
                    MaterialTheme.typography.labelLarge,
                    modifier = Modifier.width(360.dp),
                    enableConfirmButton,
                    onClickButton = {
                        if (validateConfirm) {
                            try {
                                scope.launch {
                                    loginViewModel.updatePassword(newPassword)

                                    /*snackbarHostState.showSnackbar(
                                        "contraseña actualizada $newPassword",
                                        duration = SnackbarDuration.Short
                                    )*/
                                    loginViewModel.onEnableNewPasswordFields(false)
                                }
                            } catch (e: Exception) {
                                Log.e("DataStore", "Error Updating password", e)
                            }
                        } else {

                            loginViewModel.onClearFields()
                        }
                    }
                )

                ModelButton(text = stringResource(id = R.string.backButton),
                    MaterialTheme.typography.labelLarge,
                    modifier = Modifier.width(360.dp),
                    true,
                    onClickButton = {
                        loginViewModel.onEnableNewPasswordFields(false)
                    }
                )


            }
        }
    }

}

















