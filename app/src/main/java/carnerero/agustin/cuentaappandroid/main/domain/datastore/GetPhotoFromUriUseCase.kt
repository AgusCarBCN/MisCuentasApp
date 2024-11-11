package carnerero.agustin.cuentaappandroid.main.domain.datastore

import android.net.Uri
import carnerero.agustin.cuentaappandroid.main.data.datastore.preferences.repository.UserDataStoreRepository
import javax.inject.Inject

class GetPhotoFromUriUseCase  @Inject constructor(private val repository: UserDataStoreRepository) {

    suspend operator fun invoke(): Uri {
        return repository.getPhotoUri()
    }
}