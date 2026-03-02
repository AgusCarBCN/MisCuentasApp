package carnerero.agustin.cuentaappandroid.data.repository

import android.net.Uri
import carnerero.agustin.cuentaappandroid.presentation.ui.profile.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun getUserDataProfile(): UserProfile
    suspend fun setUserDataProfile(userProfile: UserProfile)

    suspend fun updateNameProfile(name: String)

    suspend fun updateUsernameProfile(username: String)

    suspend fun updatePassword(newPassword: String)
    suspend fun getShowTutorial(): Boolean?
    suspend fun setShowTutorial(showTutorial: Boolean)

    suspend fun getToLogin(): Boolean?
    suspend fun setToLogin(toLogin: Boolean)

    fun getCurrencyCode(): Flow<String>
    suspend fun setCurrencyCode(currencyCode: String)

    suspend fun getPhotoUri(): Uri
    suspend fun setPhotoUri(uri: Uri)

    fun getEnableTutorial(): Flow<Boolean>
    suspend fun setEnableTutorial(newValue: Boolean)

    fun getEnableDarkTheme(): Flow<Boolean>
    suspend fun setEnableDarkTheme(newValue: Boolean)

    fun getEnableNotifications(): Flow<Boolean>
    suspend fun setEnableNotifications(newValue: Boolean)
}