package carnerero.agustin.cuentaappandroid.presentation.ui.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.ui.login.LoginViewModel

@Composable
fun ContentSection(loginViewModel: LoginViewModel,
                   modifier:Modifier,
                   navToMain:()->Unit){

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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .background(colors.backgroundPrimary)
            ,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (!enableNewPasswordFields) {
            Text(
                modifier= Modifier.padding(top=dimens.extraLarge),
                text = loginViewModel.getGreeting(name),
                fontSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_title_medium).toSp() },
                color = colors.textColor,
                fontWeight = FontWeight.Bold
            )
            TextFieldComponent(
                modifier = modifier,
                stringResource(id = R.string.enterUsername),
                userName,
                onTextChange = { loginViewModel.onLoginChanged(it, password) },
                BoardType.TEXT
            )
            TextFieldComponent(
                modifier = modifier,
                stringResource(id = R.string.enterPassword),
                password,
                onTextChange = { loginViewModel.onLoginChanged(userName, it) },
                BoardType.PASSWORD,
                true
            )
            ModelButton(
                text = stringResource(id = R.string.loginButton),
                MaterialTheme.typography.labelLarge,
                modifier = modifier,
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
                        color = colors.textColor
                    )
                }
            )
        }
        if (enableNewPasswordFields) {
            TextFieldComponent(
                modifier = modifier,
                stringResource(id = R.string.requestuser),
                userNameNewPassword,
                onTextChange = { loginViewModel.onUpdatePasswordChange(it, newPassword) },
                BoardType.TEXT
            )

            TextFieldComponent(
                modifier = modifier,
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
                modifier = modifier ,
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
                modifier = modifier ,
                true,
                onClickButton = {
                    loginViewModel.onEnableNewPasswordFields(false)
                }
            )
        }
    }
}