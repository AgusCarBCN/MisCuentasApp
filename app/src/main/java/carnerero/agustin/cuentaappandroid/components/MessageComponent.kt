package carnerero.agustin.cuentaappandroid.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dagger.Component

@Composable

fun message(resource:Int):String{

    return stringResource(id =resource )
}