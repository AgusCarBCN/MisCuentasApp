package carnerero.agustin.cuentaappandroid.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.components.HeadSetting
import carnerero.agustin.cuentaappandroid.components.RowComponent
import carnerero.agustin.cuentaappandroid.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette

@Composable

fun AboutScreen(mainViewModel: MainViewModel) {
    val message = stringResource(id = R.string.share)
    val context = LocalContext.current
    val appClimgingCompanionLink =
        "https://play.google.com/store/apps/details?id=com.blogspot.agusticar.climbcompanion&pli=1"
    val appMisCuentasLink =
        "https://play.google.com/store/apps/details?id=carnerero.agustin.cuentaappandroid&hl=es&gl=US"
    val policyLink =
        "https://agusticar.blogspot.com/2024/01/politicas-de-privacidad.html"


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 25.dp)
            .verticalScroll(
                rememberScrollState()
            )
    )
    {
        HeadSetting(
            title = stringResource(id = R.string.aboutapp),
            MaterialTheme.typography.titleLarge
        )

        RowComponent(title = stringResource(id = R.string.about),
            description = stringResource(id = R.string.desaboutapp),
            iconResource = R.drawable.info,
            onClick = {
                mainViewModel.selectScreen(IconOptions.ABOUT_DESCRIPTION)
            })
        RowComponent(title = stringResource(id = R.string.share),
            description = stringResource(id = R.string.desshare),
            iconResource = R.drawable.share,
            onClick = { shareLinkGooglePlayStore(context, message) })
        RowComponent(title = stringResource(id = R.string.rate),
            description = stringResource(id = R.string.desrate),
            iconResource = R.drawable.star_rate,
            onClick = { openGooglePlayStore(context, appMisCuentasLink) })
        RowComponent(title = stringResource(id = R.string.contactme),
            description = stringResource(id = R.string.desemail),
            iconResource = R.drawable.email,
            onClick = { mainViewModel.selectScreen(IconOptions.EMAIL) })
        RowComponent(title = stringResource(id = R.string.visitmygithub),
            description = stringResource(id = R.string.visitmygithubdes),
            iconResource = R.drawable.github,
            onClick = { visitMyGitHub(context) })
        RowComponent(title = stringResource(id = R.string.othersapp),
            description = stringResource(id = R.string.desstore),
            iconResource = R.drawable.apps,
            onClick = { openGooglePlayStore(context, appClimgingCompanionLink) })
        RowComponent(title = stringResource(id = R.string.privacy),
            description = stringResource(id = R.string.despolicy),
            iconResource = R.drawable.privacy,
            onClick = { openGooglePlayStore(context, policyLink) })
    }
}

@Composable
@Preview(showBackground = true)

fun AboutApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 25.dp)
            .verticalScroll(
                rememberScrollState()
            )
    )
    {
        HeadSetting(
            title = stringResource(id = R.string.app_name),
            MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(id = R.string.description),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 20.dp, start = 20.dp),
            color = LocalCustomColorsPalette.current.textColor,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = stringResource(id = R.string.developer),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 20.dp, start = 20.dp),
            color = LocalCustomColorsPalette.current.textColor,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(id = R.string.version),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, end = 20.dp, start = 20.dp),
            color = LocalCustomColorsPalette.current.textColor,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(id = R.string.atributions),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, end = 20.dp, start = 20.dp),
            color = LocalCustomColorsPalette.current.textColor,
            style = MaterialTheme.typography.bodyMedium
        )

        SetupAttributions()
    }
}

@Composable
fun SendEmail() {
    val context = LocalContext.current
    val sendEmailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:agusticar@gmail.com") // Configurar el correo del destinatario
    }

    context.startActivity(sendEmailIntent) // Iniciar el correo electrónico

}

private fun shareLinkGooglePlayStore(context: Context, msg: String) {

    val appMisCuentasLink =
        "https://play.google.com/store/apps/details?id=carnerero.agustin.cuentaappandroid&hl=es&gl=US"
    // Combinar el mensaje y el enlace en una sola cadena
    val combinedMsg = "$msg\n$appMisCuentasLink"

    // Crear un Intent para compartir
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "text/plain"
    shareIntent.putExtra(Intent.EXTRA_TEXT, combinedMsg)

    // Iniciar un Activity chooser para que el usuario seleccione la aplicación con la que desea compartir el enlace
    context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share)))
}

private fun openGooglePlayStore(context: Context, link: String) {

    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))

}

private fun visitMyGitHub(context: Context) {

    val githubUrl = "https://github.com/AgusCarBCN"

    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl)))

}

@Composable
private fun SetupAttributions() {

    val attributionsList = mapOf(
        "https://www.flaticon.es/autores/2d3ds" to stringResource(id = R.string.iconconta),
        "https://fonts.google.com/icons" to stringResource(id = R.string.iconsGoogle),
        "https://uxwing.com/tag/tools-icons" to stringResource(id = R.string.iconsuxwing),
        "https://flagpedia.net/" to stringResource(id = R.string.iconflags),
        "https://v6.exchangerate-api.com" to stringResource(id = R.string.api)
    )

    Column {
        attributionsList.forEach { (url, description) ->
            Box(modifier = Modifier.padding(top = 5.dp, start = 20.dp)) {
                Text(buildAnnotatedString {

                    withLink(
                        LinkAnnotation.Url(
                            url,
                            TextLinkStyles(SpanStyle(color = LocalCustomColorsPalette.current.link,
                                fontSize =14.sp
                            )
                        ))
                    ) {
                        append(description)
                    }
                })

            }
        }
    }
}



