package carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.view


import android.content.res.Configuration
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.model.TutorialItem
import kotlinx.coroutines.launch

@Composable
fun Tutorial(
    tutorialViewModel: TutorialViewModel,
    navToScreen: () -> Unit
) {
    // Detectar orientacion
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val toLogin by tutorialViewModel.toLogin.observeAsState(false)
    val listOfItems = getItems()
    val pagerState = rememberPagerState(
        pageCount = { listOfItems.size }
    )
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
    ) {
        val buttonField = maxWidth * 0.85f
        Column(
                    modifier = Modifier
                        .padding(top=if(isLandscape)20.dp else 80.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) { page ->
                        ItemCard(item = listOfItems[page], isLandscape)
                    }
                    //Spacer(modifier = Modifier.height(12.dp))
                    CircleIndicator(
                        totalDots = listOfItems.size,
                        selectedIndex = pagerState.targetPage
                    )
                    ModelButton(
                        text = stringResource(id = if (toLogin) R.string.loginButton else R.string.createProfileButton),
                        MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .padding(top=20.dp)
                            .width(buttonField)
                            .heightIn(min = 48.dp)
                       , true,
                        onClickButton = { navToScreen() }
                    )
                }

            }
        }



@Composable
private fun ItemCard(item: TutorialItem,isLandscape:Boolean) {



    // Creamos un animatable para manejar el color del ícono
    val initColor = colors.imageTutorialInit
    val targetColor = colors.imageTutorialTarget
    val color = remember { Animatable(initColor) }
    val coroutineScope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val isPortrait =
        configuration.orientation == Configuration.ORIENTATION_PORTRAIT


    // Iniciamos una corrutina para animar el color de manera infinita
    if (item.iconItem != R.drawable.contabilidad) {
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                // Animación infinita que alterna entre dos colores
                color.animateTo(
                    targetValue = targetColor,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 2000), // Duración de la transición (1 segundo)
                        repeatMode = RepeatMode.Reverse // Alterna entre los dos colores
                    )
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .background(colors.backgroundPrimary)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = item.titleItem,
            modifier = Modifier
                .width(360.dp)
                .padding(top = 15.dp, bottom = 15.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            color = (colors.boldTextColor)
        )
        Spacer(modifier = Modifier.width(5.dp)) // Espacio entre imagen y texto
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val imageSize =
                if (isPortrait) maxHeight * 0.35f
                else maxHeight * 0.5f
            if (isPortrait) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = item.iconItem),
                        contentDescription = null,
                        modifier = Modifier
                            .size(imageSize)
                            .padding(bottom = 16.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = if (item.iconItem != R.drawable.contabilidad) ColorFilter.tint(
                            color.value
                        )
                        else null
                    )
                    Text(
                        text = item.descriptionItem,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.textColor
                    )
                }
            }
                else{

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(id = item.iconItem),
                            contentDescription = null,
                            modifier = Modifier
                                .size(imageSize)
                                .padding(end = 16.dp),
                            contentScale = ContentScale.Fit,
                            colorFilter = if (item.iconItem != R.drawable.contabilidad) ColorFilter.tint(
                                color.value
                            )
                            else null
                        )
                        Text(
                            text = item.descriptionItem,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.textColor
                        )
                    }
                }
            }
        }
    }





@Composable
private fun getItems(): List<TutorialItem> {
    // Obtener recursos de string usando stringResource
    return listOf(
        TutorialItem(
            title = stringResource(id = R.string.title0),
            description = stringResource(id = R.string.des0),
            icon = R.drawable.contabilidad
        ),
        TutorialItem(
            title = stringResource(id = R.string.title1),
            description = stringResource(id = R.string.des1),
            icon = R.drawable.profile
        ),
        TutorialItem(
            title = stringResource(id = R.string.title2),
            description = stringResource(id = R.string.des2),
            icon = R.drawable.ic_icontutorial2
        ),
        TutorialItem(
            title = stringResource(id = R.string.title3),
            description = stringResource(id = R.string.des3),
            icon = R.drawable.ic_icontutorial3
        ),
        TutorialItem(
            title = stringResource(id = R.string.title4),
            description = stringResource(id = R.string.des4),
            icon = R.drawable.ic_icontutorial4
        ),
        TutorialItem(
            title = stringResource(id = R.string.title5),
            description = stringResource(id = R.string.des5),
            icon = R.drawable.ic_icontutorial5
        ),
        TutorialItem(
            title = stringResource(id = R.string.title6),
            description = stringResource(id = R.string.des6),
            icon = R.drawable.ic_icontutorial6

        )

    )
}

@Composable
private fun CircleIndicator(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    val indicatorSelected= stringResource(id = R.string.indicatorSelected)
    val indicatorUnSelected= stringResource(id = R.string.indicatorNoSelected)
    Column(
        modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),

            ) {
            repeat(totalDots) { index ->
                // Animación de escala para el punto seleccionado
                val scale = animateFloatAsState(
                    targetValue = if (index == selectedIndex) 1.1f else 1f, // Escala más grande si es el seleccionado
                    animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow),
                    label = "indicatorTutorial $index"
                )
                val indicatorColor by animateColorAsState(
                    targetValue = if (index == selectedIndex) {
                        colors.indicatorSelected
                    } else {
                        colors.indicatorDefault
                    },
                    label = "indicator color $index",
                    animationSpec = tween(
                        durationMillis = 2000, // Duración de la animación
                        easing = LinearOutSlowInEasing // Controla la velocidad de la transición
                    )
                )

                Icon(
                    painter = painterResource (if(index==selectedIndex)R.drawable.indicatorselected
                    else R.drawable.circleindicator ),
                    contentDescription = if(index==selectedIndex)"$indicatorSelected $index"
                    else "$indicatorUnSelected $index"
                    ,
                    tint = indicatorColor,
                    modifier = Modifier
                        .scale(scale.value)


                )
            }
        }
    }

}
