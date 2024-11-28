package carnerero.agustin.cuentaappandroid.domain.datastore

import carnerero.agustin.cuentaappandroid.data.repository.UserDataStoreRepository
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.UserProfile
import javax.inject.Inject

class SetUserProfileDataUseCase @Inject constructor(private val repository: UserDataStoreRepository) {

    suspend operator fun invoke(user: UserProfile) {
        repository.setUserDataProfile(user)
    }
}
