package carnerero.agustin.cuentaappandroid.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.datastore.preferences.core.edit
import carnerero.agustin.cuentaappandroid.data.pref.UserPreferencesKeys
import carnerero.agustin.cuentaappandroid.data.pref.dataStore
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class UserDataStoreRepository @Inject constructor(private val context: Context) :
    DataStoreRepository {


    override suspend fun getUserDataProfile(): UserProfile {

        return context.dataStore.data.first().let { preferences ->
            UserProfile(
                name = preferences[UserPreferencesKeys.NAME] ?: "",
                userName = preferences[UserPreferencesKeys.USERNAME] ?: "",
                password = preferences[UserPreferencesKeys.PASSWORD] ?: ""
            )

        }

    }

    override suspend fun setUserDataProfile(userProfile: UserProfile) {
        try {
            withContext(Dispatchers.IO) {
                context.dataStore.edit { preferences ->
                    preferences[UserPreferencesKeys.NAME] = userProfile.name
                    preferences[UserPreferencesKeys.USERNAME] = userProfile.userName
                    preferences[UserPreferencesKeys.PASSWORD] = userProfile.password
                }
                Log.e(
                    "DataStoreUser",
                    "value ${userProfile.name} ${userProfile.userName} ${userProfile.password}"
                )
            }

        } catch (e: Exception) {
            Log.e("DataStoreUser", "Error writing to DataStore", e)
        }
    }

    override suspend fun upDatePassword(newPassword: String) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.PASSWORD] = newPassword
        }
    }

    override suspend fun getShowTutorial(): Boolean =
        context.dataStore.data.first()[UserPreferencesKeys.SHOW_TUTORIAL] ?: true


    override suspend fun setShowTutorial(showTutorial: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.SHOW_TUTORIAL] = showTutorial
        }
    }


    override suspend fun getToLogin(): Boolean =
        context.dataStore.data.first()[UserPreferencesKeys.TO_LOGIN] ?: false


    override suspend fun setToLogin(toLogin: Boolean) {
        try {
            context.dataStore.edit { preferences ->
                preferences[UserPreferencesKeys.TO_LOGIN] = toLogin
                Log.e("DataStoreWrite", "value $toLogin wrote")
            }
        } catch (e: Exception) {
            Log.e("DataStoreWrite", "Error writing to DataStore", e)
        }

    }

    override suspend fun getCurrencyCode(): String =
        context.dataStore.data.first()[UserPreferencesKeys.CURRENCY_CODE] ?: "USD"

    override suspend fun setCurrencyCode(currencyCode: String) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.CURRENCY_CODE] = currencyCode
        }
    }

    override suspend fun getPhotoUri(): Uri {

        val uriString=context.dataStore.data.first()[UserPreferencesKeys.PHOTO_URI] ?: ""

        return Uri.parse(uriString)

    }
    override suspend fun setPhotoUri(uri: Uri) {

        if(uri!=Uri.EMPTY) {
            val pathImageUri = saveUri(uri)
            context.dataStore.edit { preferences ->
                preferences[UserPreferencesKeys.PHOTO_URI] = pathImageUri.toString()
            }
        }
    }

    override suspend fun getEnableTutorial(): Boolean =
        context.dataStore.data.first()[UserPreferencesKeys.ENABLE_SWITCH_TUTORIAL] ?: true

    override suspend fun setEnableTutorial(newValue: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.ENABLE_SWITCH_TUTORIAL] =newValue
        }
    }

    override suspend fun getEnableDarkTheme(): Boolean =
        context.dataStore.data.first()[UserPreferencesKeys.ENABLE_SWITCH_DARKTHEME] ?: false

    override suspend fun setEnableDarkTheme(newValue: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.ENABLE_SWITCH_DARKTHEME] =newValue
        }
    }

    override suspend fun getEnableNotifications(): Boolean =
        context.dataStore.data.first()[UserPreferencesKeys.ENABLE_SWITCH_NOTIFICATIONS] ?: false

    override suspend fun setEnableNotifications(newValue: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.ENABLE_SWITCH_NOTIFICATIONS] =newValue
        }
    }

    private suspend fun saveUri(uri: Uri): String? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.externalCacheDir, "image.jpg")
        return if (inputStream != null) {
            withContext(Dispatchers.IO) {
                FileOutputStream(file).use { outputStream ->
                    try {
                        inputStream.copyTo(outputStream)
                        file.absolutePath
                    } catch (e: Exception) {
                        e.printStackTrace() // Manejo de errores
                        null
                    }
                }
            }
        } else {
            null
        }
    }


}