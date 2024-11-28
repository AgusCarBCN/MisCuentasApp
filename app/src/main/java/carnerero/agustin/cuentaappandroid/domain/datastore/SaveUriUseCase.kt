package carnerero.agustin.cuentaappandroid.domain.datastore

import android.net.Uri
import carnerero.agustin.cuentaappandroid.data.repository.UserDataStoreRepository
import javax.inject.Inject

class SaveUriUseCase   @Inject constructor(private val repository: UserDataStoreRepository) {

    suspend operator fun invoke(uri: Uri) {
        repository.setPhotoUri(uri)
    }
}