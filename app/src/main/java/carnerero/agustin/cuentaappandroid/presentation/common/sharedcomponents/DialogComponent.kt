package carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors


@Composable
fun ModelDialog(
    title:Int,
    message:Int,
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    if (showDialog) {
        AlertDialog(containerColor= colors.drawerColor,
            onDismissRequest = { onDismiss() },

            title={Text(stringResource(id = title),
                style=MaterialTheme.typography.titleMedium,
                color = colors.textHeadColor)},

            text={Text(stringResource(id = message),
                style=MaterialTheme.typography.bodySmall,
                color = colors.textColor)}
            ,
            confirmButton = {
                ModelButton(
                    text = stringResource(id = R.string.confirmButton),
                    MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(130.dp),
                    true,
                    onClickButton = {
                        onConfirm()
                    } )
            },
            dismissButton = {
                ModelButton(
                    text = stringResource(id = R.string.cancelButton),
                    MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(130.dp),
                    true,
                    onClickButton = {
                        onDismiss()
                    } )
            }
        )
    }
}
@Composable
fun NotificationDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    if (showDialog) {
        AlertDialog(containerColor= colors.drawerColor,
            onDismissRequest = { onDismiss() },

            icon= {
                Icon(
                    painter = painterResource(id = R.drawable.notificationoption),
                    contentDescription = "notification icon",
                    tint = colors.textColor,
                    modifier = Modifier
                        .size(24.dp)
                )
            }                           ,

            text={Text(stringResource(id = R.string.notification_required),
                style=MaterialTheme.typography.bodySmall,
                color = colors.textColor)}
            ,
            confirmButton = {
                ModelButton(
                    text = stringResource(id = R.string.confirmButton),
                    MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(130.dp),
                    true,
                    onClickButton = {
                        onConfirm()
                    } )
            },
            dismissButton = {
                ModelButton(
                    text = stringResource(id = R.string.cancelButton),
                    MaterialTheme.typography.labelSmall ,
                    modifier = Modifier.width(130.dp),
                    true,
                    onClickButton = {
                        onDismiss()
                    } )
            }
        )
    }
}

