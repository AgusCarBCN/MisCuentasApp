package carnerero.agustin.cuentaappandroid.domain.datastore

import carnerero.agustin.cuentaappandroid.data.repository.UserDataStoreRepository
import jakarta.inject.Inject

class UpdateUsernameUseCase @Inject constructor(private val repository: UserDataStoreRepository) {

    suspend operator fun invoke(newValue: String) {
        repository.updateUsernameProfile(newValue)
    }
}