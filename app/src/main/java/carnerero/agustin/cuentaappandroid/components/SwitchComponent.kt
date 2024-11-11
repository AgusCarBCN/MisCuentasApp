package carnerero.agustin.cuentaappandroid.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette



@Composable
fun SwitchComponent(title:String,
                    description:String,
                    isChecked:Boolean,
                    onClickSwitch: (Boolean) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, end = 20.dp, start = 20.dp),
        Arrangement.SpaceEvenly,
        Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(0.75f)) {
            Text(text = title,
                fontWeight = FontWeight.Bold,
                fontSize =18.sp,
                color = LocalCustomColorsPalette.current.textColor)
            Text(text = description,
                color= LocalCustomColorsPalette.current.textColor)
        }
        Switch(modifier = Modifier.weight(0.25f),
            checked = isChecked,
            onCheckedChange = {onClickSwitch(it)},

            /*
            The "checkedIconColor" and "uncheckedIconColor" excluded from this list
            because you need to set an icon first, use the "thumbContent" parameter.
            You can find an example in the next section.
             */
            colors = SwitchColors(
                checkedThumbColor = LocalCustomColorsPalette.current.thumbCheckedColor, // Color del thumb activado
                checkedTrackColor = LocalCustomColorsPalette.current.trackCheckedColor,      // Color del track activado
                checkedBorderColor = LocalCustomColorsPalette.current.trackCheckedColor,         // Color del borde activado
                checkedIconColor = LocalCustomColorsPalette.current.backgroundSwitch,          // Color del icono activado
                uncheckedThumbColor = LocalCustomColorsPalette.current.thumbDefaultColor,  // Color del thumb desactivado
                uncheckedTrackColor = LocalCustomColorsPalette.current.trackDefaultColor,       // Color del track desactivado
                uncheckedBorderColor = LocalCustomColorsPalette.current.backgroundSwitch,          // Color del borde desactivado
                uncheckedIconColor = LocalCustomColorsPalette.current.backgroundSwitch,           // Color del icono desactivado
                disabledCheckedThumbColor = LocalCustomColorsPalette.current.thumbDefaultColor, // Color del thumb activado cuando está deshabilitado
                disabledCheckedTrackColor = LocalCustomColorsPalette.current.trackDefaultColor, // Color del track activado cuando está deshabilitado
                disabledCheckedBorderColor = LocalCustomColorsPalette.current.backgroundSwitch,    // Color del borde activado cuando está deshabilitado
                disabledCheckedIconColor = LocalCustomColorsPalette.current.backgroundSwitch,     // Color del icono activado cuando está deshabilitado
                disabledUncheckedThumbColor = LocalCustomColorsPalette.current.thumbDefaultColor, // Color del thumb desactivado cuando está deshabilitado
                disabledUncheckedTrackColor = LocalCustomColorsPalette.current.trackDefaultColor, // Color del track desactivado cuando está deshabilitado
                disabledUncheckedBorderColor = LocalCustomColorsPalette.current.backgroundSwitch,    // Color del borde desactivado cuando está deshabilitado
                disabledUncheckedIconColor = LocalCustomColorsPalette.current.backgroundSwitch      // Color del icono desactivado cuando está deshabilitado
            )
        )
    }
}
@Composable
fun RowComponent(title: String,
                 description:String,
                 iconResource:Int,
                 onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, end = 20.dp, start = 20.dp)
            .clickable { onClick() },
        Arrangement.SpaceEvenly,
        Alignment.CenterVertically
    )
    {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = title,
            modifier = Modifier.weight(0.10f)
                .size(24.dp)
                .padding(end=10.dp),
            tint = LocalCustomColorsPalette.current.textColor


        )

        Column(modifier = Modifier.weight(0.90f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = LocalCustomColorsPalette.current.textColor
            )
            Text(
                text = description,
                color = LocalCustomColorsPalette.current.textColor
            )
        }

    }


}
@Composable
fun HeadSetting(title: String,size:Int) {

    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = size.sp,
        modifier = Modifier
            .padding(top = 15.dp, bottom = 15.dp)
            .fillMaxWidth(),
        color = LocalCustomColorsPalette.current.textHeadColor,
        textAlign = TextAlign.Center
    )
}