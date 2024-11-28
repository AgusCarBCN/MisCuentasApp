package carnerero.agustin.cuentaappandroid.data.repository

import android.net.Uri
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.UserProfile

interface DataStoreRepository {

    suspend fun getUserDataProfile(): UserProfile
    suspend fun setUserDataProfile(userProfile: UserProfile)
    suspend fun upDatePassword(newPassword: String)
    suspend fun getShowTutorial(): Boolean?
    suspend fun setShowTutorial(showTutorial: Boolean)

    suspend fun getToLogin(): Boolean?
    suspend fun setToLogin(toLogin: Boolean)



    suspend fun getCurrencyCode(): String?
    suspend fun setCurrencyCode(currencyCode: String)

    suspend fun getPhotoUri(): Uri
    suspend fun setPhotoUri(uri: Uri)

    suspend fun getEnableTutorial(): Boolean?
    suspend fun setEnableTutorial(newValue: Boolean)

    suspend fun getEnableDarkTheme(): Boolean?
    suspend fun setEnableDarkTheme(newValue: Boolean)

    suspend fun getEnableNotifications(): Boolean?
    suspend fun setEnableNotifications(newValue: Boolean)
}