package carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.view


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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.model.TutorialItem
import kotlinx.coroutines.launch

@Composable
fun Tutorial(
    tutorialViewModel: TutorialViewModel,
    navToScreen: () -> Unit,
    listOfItems: List<TutorialItem> = getItems(),
    modifier: Modifier
) {

    val toLogin by tutorialViewModel.toLogin.observeAsState(false)

    ConstraintLayout(

        modifier= Modifier
            .fillMaxSize()
            .background(LocalCustomColorsPalette.current.backgroundPrimary)
    ) {

        val (horizontalPager, loginButton) = createRefs()
        //Cargar valor de DataStore...

        val pagerState = rememberPagerState(pageCount = {
            listOfItems.size  // Cada página corresponde a un item de la lista
        })

        Column(
            modifier=Modifier.constrainAs(horizontalPager) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(loginButton.top)
            }
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)

            ) { page ->
                // Contenido de cada página
                ItemCard(item = listOfItems[page]) // Mostrando un item en cada página
            }

            CircleIndicator(totalDots = listOfItems.size, pagerState.targetPage)
        }


        ModelButton(text = stringResource(id =if(toLogin) R.string.loginButton else R.string.createProfileButton),
            MaterialTheme.typography.labelLarge
           ,modifier = Modifier
                .width(360.dp)
                .constrainAs(loginButton) {
                    top.linkTo(horizontalPager.bottom)          // Parte superior anclada al imageBox
                    start.linkTo(parent.start)           // Empieza en el lado izquierdo del padre
                    end.linkTo(parent.end)               // Termina en el lado derecho del padre
                    bottom.linkTo(parent.bottom)         // Parte inferior anclada al padre
                }, true,
            onClickButton = {navToScreen()}

        )
    }

}

@Composable
private fun ItemCard(item: TutorialItem) {

    // Creamos un animatable para manejar el color del ícono
    val initColor = LocalCustomColorsPalette.current.imageTutorialInit
    val targetColor = LocalCustomColorsPalette.current.imageTutorialTarget
    val color = remember { Animatable(initColor) }
    val coroutineScope = rememberCoroutineScope()
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
    // Un card sencillo que muestra título, descripción e ícono
    Card(
        modifier = Modifier
            .width(360.dp)
            .height(460.dp)

    ) {
        Column(
            modifier = Modifier
                .background(LocalCustomColorsPalette.current.backgroundPrimary)
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
                style=MaterialTheme.typography.headlineMedium,
                color = (LocalCustomColorsPalette.current.boldTextColor)
            )
            Spacer(modifier = Modifier.width(5.dp)) // Espacio entre imagen y texto
            Image(
                painter = painterResource(id = item.iconItem),
                contentDescription = null, // No se requiere descripción accesible para imágenes decorativas
                modifier = Modifier
                    .width(250.dp)
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally),
                colorFilter =if(item.iconItem!=R.drawable.contabilidad)androidx.compose.ui.graphics.ColorFilter.tint(color.value)
                else null
            )
            Spacer(modifier = Modifier.width(5.dp)) // Espacio entre imagen y texto
            Text(
                text = item.descriptionItem,
                modifier = Modifier
                    .width(360.dp)
                    .height(220.dp)
                    .padding(top = 10.dp, bottom = 5.dp)
                    .align(Alignment.CenterHorizontally),
                style=MaterialTheme.typography.labelLarge,
                color = LocalCustomColorsPalette.current.textColor
            )

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
                        LocalCustomColorsPalette.current.indicatorSelected
                    } else {
                        LocalCustomColorsPalette.current.indicatorDefault
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
