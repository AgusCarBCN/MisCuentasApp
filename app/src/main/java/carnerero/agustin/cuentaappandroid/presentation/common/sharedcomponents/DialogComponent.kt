package carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
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
                ModelButton(text = stringResource(id = R.string.confirmButton),
                    MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(130.dp),
                    true,
                    onClickButton = {
                        onConfirm()
                    } )
            },
            dismissButton = {
                ModelButton(text = stringResource(id = R.string.cancelButton),
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
                ModelButton(text = stringResource(id = R.string.confirmButton),
                    MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(130.dp),
                    true,
                    onClickButton = {
                        onConfirm()
                    } )
            },
            dismissButton = {
                ModelButton(text = stringResource(id = R.string.cancelButton),
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

@Composable
fun ModelDialogWithTextField(
    name:String,
    showDialog: Boolean,
    textFieldValue: String,
    onValueChange: (String) -> Unit,  // Callback para actualizar el valor del TextField
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    searchViewModel: SearchViewModel
) {
    if (showDialog) {
        AlertDialog(
            containerColor = colors.backgroundPrimary,
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    name,
                    color = colors.textHeadColor,
                    style=MaterialTheme.typography.titleSmall
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center, // Espacio entre DatePickers
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    TextFieldComponent(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(id = R.string.limitMax),
                        textFieldValue,
                        onTextChange = onValueChange,
                        BoardType.DECIMAL,
                        false
                    )
                        DatePickerSearch(
                            modifier = Modifier
                                .width(240.dp)
                                .padding(bottom = 10.dp)
                                , // Espacio a la derecha para separar de forma equitativa
                            R.string.fromdate,
                            searchViewModel = searchViewModel,
                            true
                        )

                        DatePickerSearch(
                            modifier = Modifier
                                .width(240.dp)
                                , // Espacio a la izquierda para separar de forma equitativa
                            R.string.todate,
                            searchViewModel = searchViewModel,
                            false
                        )
                    }


                },
            confirmButton = {
                ModelButton(
                    text = stringResource(id = R.string.confirmButton),
                    MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(130.dp),
                    true,
                    onClickButton = {
                        onConfirm()
                    }
                )
            },
            dismissButton = {
                ModelButton(
                    text = stringResource(id = R.string.cancelButton),
                    MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(130.dp),
                    true,
                    onClickButton = {
                        onDismiss()
                    }
                )
            }
        )
    }
}
