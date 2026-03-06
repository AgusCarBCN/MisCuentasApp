package carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents


import androidx.compose.animation.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun IconAnimated(iconResource:Int,sizeIcon:Int,initColor:Color,targetColor: Color){


    val color = remember { Animatable(initColor) }


    val coroutineScope = rememberCoroutineScope()


    // Iniciamos una corrutina para animar el color de manera infinita

        LaunchedEffect(Unit) {

            coroutineScope.launch {
                // Animación infinita que alterna entre dos colores
                color.animateTo(
                    targetValue = targetColor,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 6000), // Duración de la transición (1 segundo)
                        repeatMode = RepeatMode.Reverse // Alterna entre los dos colores
                    )
                )
            }
        }
    Image(
        painter = painterResource(iconResource),
        contentDescription = null, // No se requiere descripción accesible para imágenes decorativas
        modifier = Modifier
            .size(sizeIcon.dp)
            ,
        colorFilter = ColorFilter.tint(color.value)

    )
}
