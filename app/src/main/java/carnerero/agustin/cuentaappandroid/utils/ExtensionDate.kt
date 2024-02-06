package carnerero.agustin.cuentaappandroid.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.dateFormat():String{
    val formatter= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(this)
}