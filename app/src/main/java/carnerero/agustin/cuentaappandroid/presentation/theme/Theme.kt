package carnerero.agustin.cuentaappandroid.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


// Implementación del tema con soporte para tema claro/oscuro y colores dinámicos
@Composable
fun  MisCuentasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    // logic for which custom palette to use*/
    val customColorsPalette =
        if (darkTheme) DarkCustomColorsPalette
        else LightCustomColorsPalette

    // here is the important point, where you will expose custom objects
    CompositionLocalProvider(
        LocalCustomColorsPalette provides customColorsPalette // our custom palette
    ) {
        MaterialTheme(
            //colorScheme = colorScheme, // the MaterialTheme still uses the "normal" palette
            content = content,
            typography = AppTypography
            // our custom typography
        )
    }

}