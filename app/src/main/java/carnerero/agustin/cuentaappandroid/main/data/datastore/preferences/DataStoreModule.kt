package carnerero.agustin.cuentaappandroid.main.data.datastore.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

// Extensión para crear una instancia de DataStore
val Context.dataStore by preferencesDataStore(name = "user_preferences")