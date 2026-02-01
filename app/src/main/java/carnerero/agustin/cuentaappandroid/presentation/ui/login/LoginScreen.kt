package carnerero.agustin.cuentaappandroid.presentation.ui.login

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import carnerero.agustin.cuentaappandroid.R
import coil.compose.rememberAsyncImagePainter
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette

@Composable
fun LoginComponent(
    loginViewModel: LoginViewModel = androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel(),
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

    /* Se usa para gestionar el estado del Snackbar. Esto te permite mostrar y controlar el Snackbar
     desde cualquier parte de tu UI.*/
    val messageInvalidLogin= message(resource = R.string.inValidLogin)
    val messageInvalidUserName= message(resource = R.string.inValidUserName)
    loginViewModel.getLoginImage()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {

        val imageHeight = maxHeight * 0.5f
        val contentHeight = maxHeight * 0.5f

        val fieldModifier = Modifier
            .fillMaxWidth(0.85f) // mismo ancho para TODOS
            .heightIn(min = 48.dp)

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalCustomColorsPalette.current.backgroundPrimary)
        ) {
            val (imageBox, loginBox) = createRefs()


            // Caja de imagen (arriba)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .background(LocalCustomColorsPalette.current.imageBackground)
                    .constrainAs(imageBox) {
                        top.linkTo(parent.top)
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = if (image == Uri.EMPTY)
                        painterResource(id = R.drawable.contabilidad)
                    else rememberAsyncImagePainter(image),
                    contentDescription = "Profile image",
                    modifier = if (image == Uri.EMPTY) Modifier .size(250.dp)
                        .background(LocalCustomColorsPalette.current.imageBackground) else Modifier .fillMaxSize()
                   ,contentScale = ContentScale.Crop
                )
            }
            // Caja de login en la parte inferior, ocupando el otro 50% de la altura
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(contentHeight)
                    .verticalScroll(rememberScrollState())
                    .background(LocalCustomColorsPalette.current.backgroundPrimary)
                    .constrainAs(loginBox) {
                        top.linkTo(imageBox.bottom)
                        bottom.linkTo(parent.bottom)
                    },
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (!enableNewPasswordFields) {
                    Text(
                        modifier= Modifier.padding(top=10.dp),
                        text = loginViewModel.getGreeting(name),
                        fontSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_title_medium).toSp() },
                        color = LocalCustomColorsPalette.current.textColor,
                        fontWeight = FontWeight.Bold
                    )
                    TextFieldComponent(
                        modifier = fieldModifier,
                        stringResource(id = R.string.enterUsername),
                        userName,
                        onTextChange = { loginViewModel.onLoginChanged(it, password) },
                        BoardType.TEXT
                    )
                    TextFieldComponent(
                        modifier = fieldModifier,
                        stringResource(id = R.string.enterPassword),
                        password,
                        onTextChange = { loginViewModel.onLoginChanged(userName, it) },
                        BoardType.PASSWORD,
                        true
                    )
                    ModelButton(
                        text = stringResource(id = R.string.loginButton),
                        MaterialTheme.typography.labelLarge,
                        modifier = fieldModifier,
                        enableLoginButton,
                        onClickButton = {
                            if (validateLogin) {
                                navToMain()
                            } else {
                                loginViewModel.sendMessage(messageInvalidLogin)
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
                                style = MaterialTheme.typography.labelSmall,
                                color = LocalCustomColorsPalette.current.textColor
                            )
                        }
                    )

                }
                if (enableNewPasswordFields) {
                    TextFieldComponent(
                        modifier = fieldModifier,
                        stringResource(id = R.string.requestuser),
                        userNameNewPassword,
                        onTextChange = { loginViewModel.onUpdatePasswordChange(it, newPassword) },
                        BoardType.TEXT
                    )

                    TextFieldComponent(
                        modifier = fieldModifier,
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
                    ModelButton(
                        text = stringResource(id = R.string.confirmButton),
                        MaterialTheme.typography.labelLarge,
                        modifier = fieldModifier ,
                        enableConfirmButton,
                        onClickButton = {
                            loginViewModel.confirmNewPassword(newPassword,
                                validateConfirm,
                                messageInvalidUserName)

                        }
                    )

                    ModelButton(
                        text = stringResource(id = R.string.backButton),
                        MaterialTheme.typography.labelLarge,
                        modifier = fieldModifier ,
                        true,
                        onClickButton = {
                            loginViewModel.onEnableNewPasswordFields(false)
                        }
                    )
                }
            }
        }
    }
}

















