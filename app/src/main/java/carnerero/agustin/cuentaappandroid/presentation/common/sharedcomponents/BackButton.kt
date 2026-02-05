package carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import carnerero.agustin.cuentaappandroid.R

@Composable
 fun BackButton(onBack: () -> Unit) {
    val fieldModifier = Modifier
        .fillMaxWidth(0.85f)
        .heightIn(min = 48.dp)

    ModelButton(
        text = stringResource(R.string.backButton),
        MaterialTheme.typography.labelLarge,
        modifier = fieldModifier,
        true,
        onClickButton = onBack
    )
}
