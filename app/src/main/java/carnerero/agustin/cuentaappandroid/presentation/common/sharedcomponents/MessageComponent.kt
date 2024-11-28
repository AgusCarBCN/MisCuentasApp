package carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable

fun message(resource:Int):String{

    return stringResource(id =resource )
}