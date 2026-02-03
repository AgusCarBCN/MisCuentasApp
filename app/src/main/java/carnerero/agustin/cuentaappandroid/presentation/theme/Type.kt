package carnerero.agustin.cuentaappandroid.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import carnerero.agustin.cuentaappandroid.R
private val latoBold = FontFamily(Font(R.font.latobold))
private val latoRegular = FontFamily(Font(R.font.latoregular))


// 1. SMALL (≤ 360dp) - Teléfonos muy pequeños
fun smallTypography(): Typography = Typography(
    headlineSmall = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,          // Reducido de 22
        lineHeight = 24.sp,        // Reducido de 28
        letterSpacing = 0.2.sp     // Aumentado para mejor legibilidad
    ),
    headlineMedium = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,          // Reducido de 24
        lineHeight = 26.sp,        // Reducido de 32
        letterSpacing = 0.1.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,          // Reducido de 28
        lineHeight = 30.sp,        // Reducido de 36
        letterSpacing = 0.6.sp
    ),
    displayMedium = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,          // Reducido de 36
        lineHeight = 38.sp,        // Reducido de 44
        letterSpacing = 0.3.sp
    ),
    displayLarge = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,          // Reducido de 48
        lineHeight = 48.sp,        // Reducido de 56
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,          // Reducido de 14
        lineHeight = 18.sp,        // Reducido de 20
        letterSpacing = 0.15.sp
    ),
    titleMedium = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,          // Reducido de 16
        lineHeight = 22.sp,        // Reducido de 24
        letterSpacing = 0.2.sp
    ),
    titleLarge = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,          // Reducido de 20
        lineHeight = 24.sp,        // Reducido de 28
        letterSpacing = 0.15.sp
    ),
    bodySmall = TextStyle(
        fontFamily = latoRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,          // Reducido de 12
        lineHeight = 16.sp,        // Reducido de 18
        letterSpacing = 0.3.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = latoRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,          // Reducido de 16
        lineHeight = 20.sp,        // Reducido de 24
        letterSpacing = 0.2.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = latoRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,          // Reducido de 18
        lineHeight = 22.sp,        // Reducido de 24
        letterSpacing = 0.2.sp
    ),
    labelLarge = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,          // Reducido de 20
        lineHeight = 24.sp,        // Reducido de 28
        letterSpacing = 0.15.sp
    ),
    labelMedium = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,          // Reducido de 18
        lineHeight = 22.sp,        // Reducido de 24
        letterSpacing = 0.2.sp
    ),
    labelSmall = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,          // Reducido de 14
        lineHeight = 18.sp,        // Reducido de 20
        letterSpacing = 0.3.sp
    )
)

// 2. COMPACT (361-480dp) - Tu Samsung A23 y mayoría de smartphones
fun compactTypography(): Typography = Typography(
    headlineSmall = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,          // Reducido ligeramente
        lineHeight = 26.sp,
        letterSpacing = 0.18.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,          // Reducido ligeramente
        lineHeight = 30.sp,
        letterSpacing = 0.05.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,          // Reducido de 28
        lineHeight = 34.sp,
        letterSpacing = 0.5.sp
    ),
    displayMedium = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,          // Reducido de 36
        lineHeight = 42.sp,
        letterSpacing = 0.28.sp
    ),
    displayLarge = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 44.sp,          // Reducido de 48
        lineHeight = 52.sp,
        letterSpacing = 0.12.sp
    ),
    titleSmall = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,          // Mantenido
        lineHeight = 20.sp,
        letterSpacing = 0.12.sp
    ),
    titleMedium = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,          // Mantenido
        lineHeight = 24.sp,
        letterSpacing = 0.16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 19.sp,          // Reducido de 20
        lineHeight = 26.sp,
        letterSpacing = 0.12.sp
    ),
    bodySmall = TextStyle(
        fontFamily = latoRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,          // Mantenido
        lineHeight = 18.sp,
        letterSpacing = 0.28.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = latoRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,          // Reducido de 16
        lineHeight = 22.sp,
        letterSpacing = 0.16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = latoRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,          // Reducido de 18
        lineHeight = 23.sp,
        letterSpacing = 0.16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 19.sp,          // Reducido de 20
        lineHeight = 26.sp,
        letterSpacing = 0.12.sp
    ),
    labelMedium = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,          // Reducido de 18
        lineHeight = 23.sp,
        letterSpacing = 0.16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,          // Mantenido
        lineHeight = 20.sp,
        letterSpacing = 0.28.sp
    )
)

// 3. MEDIUM (481-720dp) - Tablets pequeñas / pantallas grandes
fun mediumTypography(): Typography = Typography(
    // Usa los valores que ya tienes (tus MediumTypography original)
    headlineSmall = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.15.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    // ... (todos los demás valores como los tienes actualmente)
)

// 4. LARGE (>720dp) - Tablets grandes y escritorio
fun largeTypography(): Typography = Typography(
    headlineSmall = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,          // Aumentado de 22
        lineHeight = 30.sp,
        letterSpacing = 0.12.sp    // Reducido para pantallas grandes
    ),
    headlineMedium = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,          // Aumentado de 24
        lineHeight = 34.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,          // Aumentado de 28
        lineHeight = 38.sp,
        letterSpacing = 0.4.sp     // Reducido ligeramente
    ),
    displayMedium = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 38.sp,          // Aumentado de 36
        lineHeight = 46.sp,
        letterSpacing = 0.22.sp
    ),
    displayLarge = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 52.sp,          // Aumentado de 48
        lineHeight = 60.sp,
        letterSpacing = 0.08.sp    // Reducido para pantallas grandes
    ),
    titleSmall = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,          // Aumentado de 14
        lineHeight = 21.sp,
        letterSpacing = 0.08.sp
    ),
    titleMedium = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,          // Aumentado de 16
        lineHeight = 25.sp,
        letterSpacing = 0.12.sp
    ),
    titleLarge = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 21.sp,          // Aumentado de 20
        lineHeight = 29.sp,
        letterSpacing = 0.08.sp
    ),
    bodySmall = TextStyle(
        fontFamily = latoRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,          // Aumentado de 12
        lineHeight = 19.sp,
        letterSpacing = 0.22.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = latoRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,          // Aumentado de 16
        lineHeight = 25.sp,
        letterSpacing = 0.12.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = latoRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 19.sp,          // Aumentado de 18
        lineHeight = 25.sp,
        letterSpacing = 0.12.sp
    ),
    labelLarge = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 21.sp,          // Aumentado de 20
        lineHeight = 29.sp,
        letterSpacing = 0.08.sp
    ),
    labelMedium = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 19.sp,          // Aumentado de 18
        lineHeight = 25.sp,
        letterSpacing = 0.12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = latoBold,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,          // Aumentado de 14
        lineHeight = 21.sp,
        letterSpacing = 0.22.sp
    )
)




