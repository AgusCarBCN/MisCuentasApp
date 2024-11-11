package carnerero.agustin.cuentaappandroid.main.domain.datastore

import carnerero.agustin.cuentaappandroid.main.data.datastore.preferences.repository.UserDataStoreRepository
import carnerero.agustin.cuentaappandroid.main.model.UserProfile
import javax.inject.Inject

class SetUserProfileDataUseCase @Inject constructor(private val repository: UserDataStoreRepository) {

    suspend operator fun invoke(user: UserProfile) {
        repository.setUserDataProfile(user)
    }
}
