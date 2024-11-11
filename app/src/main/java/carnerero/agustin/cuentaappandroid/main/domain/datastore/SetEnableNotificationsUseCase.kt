package carnerero.agustin.cuentaappandroid.main.domain.datastore

import carnerero.agustin.cuentaappandroid.main.data.datastore.preferences.repository.UserDataStoreRepository
import javax.inject.Inject

class SetEnableNotificationsUseCase @Inject constructor(private val repository: UserDataStoreRepository) {

    suspend operator fun invoke(newValue: Boolean) {
        repository.setEnableNotifications(newValue)
    }
}