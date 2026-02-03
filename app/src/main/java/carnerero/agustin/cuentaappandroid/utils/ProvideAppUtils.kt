package carnerero.agustin.cuentaappandroid.utils

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import carnerero.agustin.cuentaappandroid.presentation.theme.CustomColorsPalette
import com.kapps.differentscreensizesyt.ui.theme.Dimensions
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp
import com.kapps.differentscreensizesyt.ui.theme.smallDimensions
/*
@Composable
fun ProvideAppUtils(
    customDimensions: Dimensions,
    colors: CustomColorsPalette,
    orientation: OrientationApp,
    content: @Composable () -> Unit,
) {
    val dimSet = remember{customDimensions}
    val colorsSet = remember{colors}
    val orientationSet = remember{orientation}

    CompositionLocalProvider(
        LocalAppDimens provides dimSet,
        LocalCustomColorsPalette provides colorsSet,
        LocalOrientationMode provides orientationSet,
        content = content
    )
}

// Custom Spacing Dimensions
val LocalAppDimens = compositionLocalOf {
    smallDimensions
}
val LocalCustomColorsPalette = staticCompositionLocalOf {  CustomColorsPalette() }

// Current orientation
val LocalOrientationMode = compositionLocalOf {
    OrientationApp.Portrait
}*/