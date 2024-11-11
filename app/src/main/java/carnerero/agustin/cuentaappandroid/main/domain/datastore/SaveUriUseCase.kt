package carnerero.agustin.cuentaappandroid.main.domain.datastore

import android.net.Uri
import carnerero.agustin.cuentaappandroid.main.data.datastore.preferences.repository.UserDataStoreRepository
import javax.inject.Inject

class SaveUriUseCase   @Inject constructor(private val repository: UserDataStoreRepository) {

    suspend operator fun invoke(uri: Uri) {
        repository.setPhotoUri(uri)
    }
}