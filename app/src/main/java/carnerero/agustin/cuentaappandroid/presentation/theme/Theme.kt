package carnerero.agustin.cuentaappandroid.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.kapps.differentscreensizesyt.ui.theme.LocalAppDimens
import com.kapps.differentscreensizesyt.ui.theme.LocalOrientationMode
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp
import com.kapps.differentscreensizesyt.ui.theme.compactDimensions
import com.kapps.differentscreensizesyt.ui.theme.largeDimensions
import com.kapps.differentscreensizesyt.ui.theme.mediumDimensions
import com.kapps.differentscreensizesyt.ui.theme.smallDimensions


// Implementación del tema con soporte para tema claro/oscuro y colores dinámicos
@Composable
fun  MisCuentasTheme(
    windowSize: WindowSizeClass,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    //val window=rememberWindowSize()
    // logic for which custom palette to use*/
    val customColorsPalette =
        if (darkTheme) DarkCustomColorsPalette
        else LightCustomColorsPalette

    /** ***************** Determine the orientation *****************
     * if(Width > Height) { LANDSCAPE } else { PORTRAIT }
     ** ***************** Determine the orientation ***************** **/

    val orientation = when{
        windowSize.width.size > windowSize.height.size -> OrientationApp.Landscape
        else -> OrientationApp.Portrait
    }
    /** ***************** Determine the size preference *****************
     * if(PORTRAIT) { Width is Preference } else { Height is Preference }
     ** ***************** Determine the size preference ***************** **/
    val sizeThatMatters = when(orientation){
        OrientationApp.Portrait -> windowSize.width
        else -> windowSize.height
    }

    /** ***************** Determine the dimensions *****************
     * Based on size determine the dimensions
     ** ***************** Determine the dimensions ***************** **/
    val dimensions = when(sizeThatMatters){
        is WindowSize.Small -> smallDimensions
        is WindowSize.Compact -> compactDimensions
        is WindowSize.Medium -> mediumDimensions
        else -> largeDimensions
    }
    /** ***************** Determine the dimensions *****************
     * Based on size determine the text-unit
     ** ***************** Determine the dimensions ***************** **/
    val textUnits = when(sizeThatMatters){
        is WindowSize.Small -> smallCustomTextUnits
        is WindowSize.Compact -> compactCustomTextUnits
        is WindowSize.Medium -> mediumCustomTextUnits
        else -> largeCustomTextUnits
    }

    /** ***************** Determine the dimensions *****************
     * Based on size determine the typography
     ** ***************** Determine the dimensions ***************** **/
    val typography = when (windowSize.width) {
        is WindowSize.Small -> smallTypography()
        is WindowSize.Compact -> compactTypography()
        is WindowSize.Medium -> mediumTypography()
        is WindowSize.Large -> largeTypography()
    }

    CompositionLocalProvider(
        LocalCustomColorsPalette provides customColorsPalette,
        LocalOrientationMode provides orientation,
        LocalAppDimens provides dimensions,
        LocalAppTextUnits provides textUnits
    )
    // here is the important point, where you will expose custom objects
    {
        MaterialTheme(
            //colorScheme = colorScheme, // the MaterialTheme still uses the "normal" palette
            content = content,
            typography = typography
        )
    }

}