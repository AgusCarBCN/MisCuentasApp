package carnerero.agustin.cuentaappandroid.main.domain.datastore

import carnerero.agustin.cuentaappandroid.main.data.datastore.preferences.repository.UserDataStoreRepository
import javax.inject.Inject

class UpDatePasswordUseCase @Inject constructor(private val repository: UserDataStoreRepository) {

    suspend operator fun invoke(newValue: String) {
        repository.upDatePassword(newValue)
    }
}