package com.kapps.differentscreensizesyt.ui.theme

import androidx.compose.runtime.compositionLocalOf

enum class OrientationApp {

    Portrait, Landscape

}

val LocalOrientationMode = compositionLocalOf {
    OrientationApp.Portrait
}