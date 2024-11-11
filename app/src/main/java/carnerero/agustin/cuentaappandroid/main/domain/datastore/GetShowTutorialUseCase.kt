package carnerero.agustin.cuentaappandroid.main.domain.datastore

import carnerero.agustin.cuentaappandroid.main.data.datastore.preferences.repository.UserDataStoreRepository
import javax.inject.Inject

class GetShowTutorialUseCase @Inject constructor(private val repository: UserDataStoreRepository){

    suspend operator fun invoke(): Boolean {
        return repository.getShowTutorial()
    }
}