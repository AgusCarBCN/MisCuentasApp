package carnerero.agustin.cuentaappandroid.utils

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


fun Date.dateFormat():String{
    val formatter= SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(this)
}

fun Date.dateFormatDayMonth(): String {
    // Obtener el Locale del dispositivo
    val locale = Locale.getDefault()

    // Verificar si el idioma es español (cualquier variante de español)
    return if (locale.language == "es") {
        // Formato para español
        val formatter = SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", locale)
        formatter.format(this)
    } else {
        // Formato para otros idiomas (puedes personalizar este formato para inglés o el que quieras)
        val formatter = SimpleDateFormat("EEEE, MMMM d, yyyy", locale)
        formatter.format(this)
    }
}


fun Date.dateFormatByLocale(): String {
    // Obtener el Locale del dispositivo
    val locale = Locale.getDefault()
    // Crear un objeto SimpleDateFormat utilizando el Locale
    val formatter = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale)
    // Formateamos la fecha y la devolvemos
    return formatter.format(this)
}
