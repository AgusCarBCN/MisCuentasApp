package carnerero.agustin.cuentaappandroid.presentation.ui.calculator.component

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.iconSize

@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier = Modifier,
    initColorButton: Color,
    targetColorButton: Color,
    initColorText: Color,
    targetColorText: Color,
    isLandScape:Boolean=false
) {

    val colorButton = remember { Animatable(initColorButton) }
    val colorText = remember { Animatable(initColorText) }

    LaunchedEffect(Unit) {
        colorButton.animateTo(
            targetValue = targetColorButton,
            animationSpec = infiniteRepeatable(
                animation = tween(5000),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    LaunchedEffect(Unit) {
        colorText.animateTo(
            targetValue = targetColorText,
            animationSpec = infiniteRepeatable(
                animation = tween(2000),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier =if(isLandScape)modifier
                .clip(RoundedCornerShape(6.dp))
                .size(iconSize.extraLarge)
                .background(colorButton.value)
        else modifier
            .clip(RoundedCornerShape(6.dp))
            .size(iconSize.extraLarge)
            .background(colorButton.value)
    ) {
        Text(
            text = symbol,
            style = MaterialTheme.typography.displayMedium,
            color = colorText.value
        )
    }
}
