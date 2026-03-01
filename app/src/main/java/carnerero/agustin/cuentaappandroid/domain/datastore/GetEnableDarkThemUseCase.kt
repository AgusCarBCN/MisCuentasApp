package carnerero.agustin.cuentaappandroid.domain.datastore

import carnerero.agustin.cuentaappandroid.data.repository.UserDataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEnableDarkThemUseCase @Inject constructor(private val repository: UserDataStoreRepository){

     operator fun invoke(): Flow<Boolean> {
        return repository.getEnableDarkTheme()
    }
}