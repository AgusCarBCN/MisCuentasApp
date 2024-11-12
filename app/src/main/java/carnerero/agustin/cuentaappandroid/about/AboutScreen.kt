package carnerero.agustin.cuentaappandroid.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.components.HeadSetting
import carnerero.agustin.cuentaappandroid.components.RowComponent
import carnerero.agustin.cuentaappandroid.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette

@Composable

fun AboutScreen(mainViewModel: MainViewModel)
{
    val message= stringResource(id = R.string.share)
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
        HeadSetting(title = stringResource(id = R.string.aboutapp), 20)

        RowComponent(title = stringResource(id = R.string.about),
            description = stringResource(id = R.string.desaboutapp),
            iconResource = R.drawable.info,
            onClick = {
                mainViewModel.selectScreen(IconOptions.ABOUT_DESCRIPTION)
            })
        RowComponent(title = stringResource(id = R.string.share),
            description = stringResource(id = R.string.desshare),
            iconResource = R.drawable.share,
            onClick = { shareLinkGooglePlayStore(context,appMisCuentasLink,message) })
        RowComponent(title = stringResource(id = R.string.rate),
            description = stringResource(id = R.string.desrate),
            iconResource = R.drawable.star_rate,
            onClick = { openGooglePlayStore(context,appMisCuentasLink) })
        RowComponent(title = stringResource(id = R.string.contactme),
            description = stringResource(id = R.string.desemail),
            iconResource = R.drawable.email,
            onClick = {mainViewModel.selectScreen(IconOptions.EMAIL) })
        RowComponent(title = stringResource(id = R.string.othersapp),
            description = stringResource(id = R.string.desstore),
            iconResource = R.drawable.apps,
            onClick = { openGooglePlayStore(context,appClimgingCompanionLink) })
        RowComponent(title = stringResource(id = R.string.privacy),
            description = stringResource(id = R.string.despolicy),
            iconResource = R.drawable.privacy,
            onClick = { openGooglePlayStore(context,policyLink) })
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
    HeadSetting(title = stringResource(id = R.string.app_name), 20)
    Text(
        text = stringResource(id = R.string.description),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, end = 20.dp, start = 20.dp),
        fontSize = 18.sp,
        color = LocalCustomColorsPalette.current.textColor
    )
        Text(
            text = stringResource(id = R.string.developer),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 20.dp, start = 20.dp),
            fontSize = 18.sp,
            color = LocalCustomColorsPalette.current.textColor
        )
        Text(
            text = stringResource(id = R.string.version),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 20.dp, start = 20.dp),
            fontSize = 18.sp,
            color = LocalCustomColorsPalette.current.textColor
        )

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

private fun shareLinkGooglePlayStore(context: Context, link: String, msg: String) {
    // Combinar el mensaje y el enlace en una sola cadena
    val combinedMsg = "$msg\n$link"

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
/*
private fun setupGitHubLink() {
    val githubLinkTextView: TextView = binding.tvgithublink
    val visitGit = getString(R.string.visitGitHub)
    val githubLinkHtml = "<a href=\"https://github.com/AgusCarBCN\" title=\"GitHub\">$visitGit</a>"

    githubLinkTextView.movementMethod = LinkMovementMethod.getInstance()
    githubLinkTextView.text = fromHtml(githubLinkHtml)
    githubLinkTextView.setOnClickListener {
        openUrl(githubLinkHtml)
    }
}
private fun sendEmail(){
    //Uso de intent implicito para enviar un correo electrónico al desarrollador
    val sendMeEmail=Intent(Intent.ACTION_SENDTO)
    sendMeEmail.setData(Uri.parse("mailto:${getString(R.string.developeremail)}"))
    startActivity(sendMeEmail)
}

private fun setupAttributions() {
   /* val attributionsContainer = binding.attributionsContainer
    val attributionText = getString(R.string.attributionicon)
    val attributionTextPlural=getString(R.string.attributionicons)
    val accountIcon=getString(R.string.iconconta)
    val githubIcon=getString(R.string.icongithub)
    val settingIcons=getString(R.string.iconstheme)
    val sideMenuIcons=getString(R.string.menuicons)
    val dateRangeIcons=getString(R.string.iconsdate)
    val infoProfileIcons=getString(R.string.profile_icons)
    val databaseIcons=getString(R.string.database_icons)
    val attributionsList = listOf(
        "<a href=\"https://www.flaticon.es/autores/2d3ds\" " +
                "title=\"contabilidad iconos\">$accountIcon $attributionText 2D3ds - Flaticon</a>",
        "<a href=\"https://www.freepik.es/icono/logotipo-github_25231#fromView=search&term=github&page=1&position=2&track=ais&uuid=bca581ff-3f61-49a9-8010-59c21c4b0f7c\">" +
                "$githubIcon $attributionText Dave Gandy</a>",
        "<a href=\"https://fonts.google.com/icons\"> $settingIcons $attributionTextPlural Google Fonts</a>",
        "<a href=\"https://fonts.google.com/icons\"> $sideMenuIcons $attributionTextPlural Google Fonts</a>",
        "<a href=\"https://fonts.google.com/icons\"> $dateRangeIcons $attributionTextPlural Google Fonts</a>",
        "<a href=\"https://fonts.google.com/icons\"> $infoProfileIcons $attributionTextPlural Google Fonts</a>",
        "<a href=\"https://fonts.google.com/icons\"> $databaseIcons $attributionTextPlural Google Fonts</a>"
    )

    for (attributionHtml in attributionsList) {
        val attributionTextView = TextView(requireContext())
        attributionTextView.movementMethod = LinkMovementMethod.getInstance()
        attributionTextView.text = fromHtml(attributionHtml)
        attributionTextView.setOnClickListener {
            openUrl(attributionHtml)
        }
        attributionsContainer.addView(attributionTextView)
    }*/
}

private fun openUrl(html: String) {
    try {
        val url = fromHtml(html).toString()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    } catch (e: Exception) {
        // Manejo de errores
    }
}

private fun fromHtml(html: String): CharSequence {
    return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
}*/

