package carnerero.agustin.cuentaappandroid.domain.datastore

import carnerero.agustin.cuentaappandroid.data.repository.UserDataStoreRepository
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(private val repository: UserDataStoreRepository) {

    suspend operator fun invoke(newValue: String) {
        repository.updatePassword(newValue)
    }
}