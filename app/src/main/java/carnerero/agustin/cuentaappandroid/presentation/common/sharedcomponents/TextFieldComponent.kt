package carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R

import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette


@Composable

fun TextFieldComponent(
    modifier: Modifier,
    label: String,
    inputText: String,
    onTextChange: (String) -> Unit,
    type: BoardType,
    textInvisible: Boolean=false
) {
    var visiblePasswordIcon by rememberSaveable { mutableStateOf(false) }
    TextField(
        value = inputText,
        onValueChange = {
            onTextChange(it)

        },
        label = { Text(text = label,style= MaterialTheme.typography.labelSmall) },
        shape = RoundedCornerShape(10.dp),
        maxLines = 1,
        singleLine = true, // Limita la altura de la caja de entrada a una sola línea

        modifier = modifier
            .wrapContentHeight() // Altura ajustable
            .padding(16.dp), keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = getBoardType(type) // Set the keyboard type based on the input parameter
        ),
        visualTransformation = if (textInvisible && !visiblePasswordIcon) PasswordVisualTransformation() else VisualTransformation.None, // Change display based on boolean

        colors = TextFieldDefaults.colors(
            focusedTextColor = LocalCustomColorsPalette.current.textColor,
            focusedIndicatorColor = Color.Transparent,  // Sin borde cuando está enfocado
            unfocusedIndicatorColor = Color.Transparent,  // Sin borde cuando no está enfocado
            focusedContainerColor = LocalCustomColorsPalette.current.focusedContainerTextField,
            unfocusedContainerColor = LocalCustomColorsPalette.current.unfocusedContainerTextField,
            unfocusedTextColor = LocalCustomColorsPalette.current.textColor,
            focusedLabelColor = LocalCustomColorsPalette.current.textColor,
            unfocusedLabelColor = LocalCustomColorsPalette.current.textColor,
            disabledLabelColor = LocalCustomColorsPalette.current.textColor,
            disabledTextColor = LocalCustomColorsPalette.current.textColor,
            unfocusedTrailingIconColor = LocalCustomColorsPalette.current.textColor,
            focusedTrailingIconColor = LocalCustomColorsPalette.current.textColor),

        trailingIcon = {
            if (type == BoardType.PASSWORD) {
                IconButton(onClick = { visiblePasswordIcon = !visiblePasswordIcon }) {
                    if (!visiblePasswordIcon) {
                        Icon(
                            painter = painterResource(id = R.drawable.visibility),
                            contentDescription = stringResource(id = R.string.showpassword) // Descripción para mostrar la contraseña
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.visibility_off),
                            contentDescription = stringResource(id = R.string.hidepassword) // Descripción para ocultar la contraseña
                        )
                    }
                }
            }
        }
    )
}

private fun getBoardType(type: BoardType): KeyboardType {
    return when (type) {
        BoardType.EMAIL -> KeyboardType.Email
        BoardType.NUMBER -> KeyboardType.Number
        BoardType.PASSWORD -> KeyboardType.Password
        BoardType.PHONE -> KeyboardType.Phone
        BoardType.TEXT -> KeyboardType.Text
        BoardType.URI -> KeyboardType.Uri
        BoardType.DECIMAL -> KeyboardType.Decimal

    }
}

