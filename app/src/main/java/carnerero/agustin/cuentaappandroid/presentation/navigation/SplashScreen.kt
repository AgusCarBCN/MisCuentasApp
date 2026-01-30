package carnerero.agustin.cuentaappandroid.presentation.navigation

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    showTutorial: Boolean?
) {
    var isVisible by remember { mutableStateOf(false) }

    val scaleAnimation = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(key1 = true,showTutorial) {

        isVisible = true // Activar visibilidad después de la inicialización
        scaleAnimation.animateTo(
            targetValue = 0.5F,
            animationSpec = tween(
                durationMillis = 1000,
                easing = { OvershootInterpolator(3F).getInterpolation(it) }
            )
        )
        delay(3000) // Espera 3 segundos antes de navegar
        navController.navigate(
            if (showTutorial == true) Routes.Tutorial.route
            else Routes.Login.route
        ) {
            popUpTo(Routes.Splash.route) {
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColorsPalette.current.backgroundPrimary),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = isVisible) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.contabilidad),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(350.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(4.dp, Color.Transparent, RoundedCornerShape(24.dp))
                        .background(Color.Transparent)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }





}
