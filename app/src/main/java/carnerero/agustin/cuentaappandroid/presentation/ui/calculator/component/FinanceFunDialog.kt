package carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.ui.entries.ModifyEntry

@Composable
fun FinanceDialog(
    titleRes: Int,                     // Título del diálogo (PV, FV, etc.)
    descriptionRes: Int? = null,       // Descripción opcional
    fieldLabels: List<Int>,            // Etiquetas de los campos (hasta 3)
    fieldValues: List<String>,         // Valores actuales de los campos
    onFieldValuesChange: List<(String) -> Unit>, // Callbacks de cambio
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!showDialog) return
    // Estado para controlar si el diálogo está visible
    var showDescription by remember { mutableStateOf(false) }
    AlertDialog(
        containerColor = colors.backgroundPrimary,
        onDismissRequest = onDismiss,
        title = {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    stringResource(id = titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = colors.textHeadColor
                )
                Spacer(modifier = Modifier.width(dimens.extraLarge))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = colors.textHeadColor,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { showDescription = true }
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(showDescription) {
                    descriptionRes?.let {
                        Text(modifier = Modifier.clickable { showDescription=false },
                            text=stringResource(id = it),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textColor
                        )
                    }
                }
                // Campos de texto (hasta 3)
                fieldLabels.forEachIndexed { index, labelRes ->
                    TextFieldComponent(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(id = labelRes),
                        inputText =  fieldValues.getOrElse(index) { "" },
                        onTextChange = onFieldValuesChange.getOrElse(index) { {} },
                        BoardType.DECIMAL,
                        false
                    )
                }
            }
        },
        confirmButton = {
            ModelButton(
                text = stringResource(id = R.string.confirmButton),
                textStyle =  MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(130.dp),
                enabledButton = true,
                onClickButton = onConfirm,

            )
        },
        dismissButton = {
            ModelButton(
                text = stringResource(id = R.string.cancelButton),
                textStyle = MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(130.dp),
                enabledButton = true,
                onClickButton = onDismiss
            )
        }
    )
}
@Composable
fun InfoText(
    @StringRes titleRes: Int,
    @StringRes infoRes: Int
) {
    // Estado para controlar si el diálogo está visible
    var showDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Texto principal
        Text(
            text = stringResource(id = titleRes),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = colors.textHeadColor
        )

        Spacer(modifier = Modifier.width(4.dp))

        // Icono informativo
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Info",
            tint = colors.textHeadColor,
            modifier = Modifier
                .size(20.dp)
                .clickable { showDialog = true }
        )
    }

    // Dialogo que aparece al tocar el icono
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = stringResource(id = titleRes)) },
            text = { Text(text = stringResource(id = infoRes)) },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
