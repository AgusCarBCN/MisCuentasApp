package carnerero.agustin.cuentaappandroid.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class IconSizes(
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val extraLarge:Dp
)
val phoneIconSizes = IconSizes(
    small = 16.dp,
    medium = 24.dp, // estándar Material
    large = 32.dp,
    extraLarge = 36.dp
)

val mediumIconSizes = IconSizes(
    small = 18.dp,
    medium = 26.dp,
    large = 36.dp,
    extraLarge = 40.dp
)

val tabletIconSizes = IconSizes(
    small = 20.dp,
    medium = 28.dp,
    large = 40.dp,
    extraLarge = 48.dp
)
val LocalIconSizes = staticCompositionLocalOf { phoneIconSizes }
