package carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors

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

    AlertDialog(
        containerColor = colors.backgroundPrimary,
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(id = titleRes),
                style = MaterialTheme.typography.titleMedium,
                color = colors.textHeadColor
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                descriptionRes?.let {
                    Text(
                        stringResource(id = it),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textColor
                    )
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
