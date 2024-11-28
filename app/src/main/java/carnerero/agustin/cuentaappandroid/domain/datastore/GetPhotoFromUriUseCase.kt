package carnerero.agustin.cuentaappandroid.domain.datastore

import android.net.Uri
import carnerero.agustin.cuentaappandroid.data.repository.UserDataStoreRepository
import javax.inject.Inject

class GetPhotoFromUriUseCase  @Inject constructor(private val repository: UserDataStoreRepository) {

    suspend operator fun invoke(): Uri {
        return repository.getPhotoUri()
    }
}