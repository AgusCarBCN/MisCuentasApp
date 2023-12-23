package carnerero.agustin.cuentaappandroid.interfaces

import java.util.Locale

interface OnLocaleListener {
    fun getLocale(): Locale
    fun getConversionRate():Double
}