package carnerero.agustin.cuentaappandroid.presentation.theme

import androidx.compose.runtime.Composable
import com.kapps.differentscreensizesyt.ui.theme.Dimensions
import com.kapps.differentscreensizesyt.ui.theme.LocalAppDimens
import com.kapps.differentscreensizesyt.ui.theme.LocalOrientationMode
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp

object AppTheme{
    val dimens : Dimensions
        @Composable
        get() = LocalAppDimens.current

    val colors: CustomColorsPalette
        @Composable
        get() = LocalCustomColorsPalette.current

    val orientation : OrientationApp
        @Composable
        get() = LocalOrientationMode.current
}