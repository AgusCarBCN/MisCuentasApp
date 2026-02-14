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
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens

@Composable
fun ContentSection(
    modifier: Modifier,
    state : LoginUiState,
    onUserNameChange:(String)->Unit,
    onPasswordChange:(String)->Unit,
    onForgotPasswordClick:(Boolean)->Unit,
    onNewPasswordChange:(String)->Unit,
    confirm:()->Unit,
    confirmNewPassword:()->Unit,
    backToLogin:()->Unit,
    greeting:String
) {
    val showNewPasswordFields=state.showNewPasswordFields
    val enableButton=state.enableButton
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .background(colors.backgroundPrimary),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!showNewPasswordFields) {
            Text(
                modifier = Modifier.padding(top = dimens.extraLarge),
                text = greeting,
                fontSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_title_medium).toSp() },
                color = colors.textColor,
                fontWeight = FontWeight.Bold
            )
            TextFieldComponent(
                modifier = modifier,
                stringResource(id = R.string.enterUsername),
                state.userName,
                onTextChange = {onUserNameChange(it)},
                BoardType.TEXT
            )
            TextFieldComponent(
                modifier = modifier,
                stringResource(id = R.string.enterPassword),
                state.password,
                onTextChange = {onPasswordChange(it)},
                BoardType.PASSWORD,
                true
            )
            ModelButton(
                text = stringResource(id = R.string.loginButton),
                MaterialTheme.typography.labelLarge,
                modifier = modifier,
                enableButton,
                onClickButton = {
                    confirm()
                }
            )

            TextButton(
                onClick = {
                    onForgotPasswordClick(true)
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
        if (showNewPasswordFields) {
            TextFieldComponent(
                modifier = modifier,
                stringResource(id = R.string.requestuser),
                state.userName,
                onTextChange = { onUserNameChange(it) },
                BoardType.TEXT
            )

            TextFieldComponent(
                modifier = modifier,
                stringResource(id = R.string.newpassword),
                state.newPassword,
                onTextChange = {
                    onNewPasswordChange(it)
                },
                BoardType.PASSWORD,
                true
            )
            ModelButton(
                text = stringResource(id = R.string.confirmButton),
                MaterialTheme.typography.labelLarge,
                modifier = modifier,
                enableButton,
                onClickButton = {
                    confirmNewPassword()
                }
            )

            ModelButton(
                text = stringResource(id = R.string.backButton),
                MaterialTheme.typography.labelLarge,
                modifier = modifier,
                true,
                onClickButton = {
                    backToLogin()
                }
            )
        }
    }
}