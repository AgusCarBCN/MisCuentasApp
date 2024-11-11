package carnerero.agustin.cuentaappandroid.main.data.datastore.preferences


import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

// Claves para almacenar las preferencias en DataStore

object UserPreferencesKeys {
    val SHOW_TUTORIAL = booleanPreferencesKey("show_tutorial")
    val TO_LOGIN = booleanPreferencesKey("show_create_profile")
    val NAME = stringPreferencesKey("name")
    val USERNAME = stringPreferencesKey("username")
    val PASSWORD = stringPreferencesKey("password")
    val CURRENCY_SYMBOL=stringPreferencesKey("currency_symbol")
    val CURRENCY_CODE=stringPreferencesKey("currency_code")
    val PHOTO_URI = stringPreferencesKey("photo_uri")
    val ENABLE_SWITCH_TUTORIAL = booleanPreferencesKey("enable_switch_tutorial")
    val ENABLE_SWITCH_DARKTHEME = booleanPreferencesKey("enable_switch_dark theme")
    val ENABLE_SWITCH_NOTIFICATIONS = booleanPreferencesKey("enable_switch_notification")
}
