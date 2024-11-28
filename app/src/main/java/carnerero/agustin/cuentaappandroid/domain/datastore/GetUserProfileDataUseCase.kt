package carnerero.agustin.cuentaappandroid.domain.datastore

import carnerero.agustin.cuentaappandroid.data.repository.UserDataStoreRepository
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.UserProfile
import javax.inject.Inject

/*/ Este es un método suspendido que usa el operador `invoke`.
     Al ser suspendido, significa que debe ser llamado desde una corrutina o
     otra función suspendida. El operador `invoke` permite llamar la instancia
     de esta clase como si fuera una función.*/

class GetUserProfileDataUseCase @Inject constructor(private val repository: UserDataStoreRepository) {

    suspend operator fun invoke(): UserProfile {
        return repository.getUserDataProfile()
    }
}