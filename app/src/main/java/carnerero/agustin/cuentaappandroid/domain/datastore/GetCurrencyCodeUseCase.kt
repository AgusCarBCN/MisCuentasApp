package carnerero.agustin.cuentaappandroid.domain.datastore

import carnerero.agustin.cuentaappandroid.data.repository.UserDataStoreRepository
import javax.inject.Inject

class GetCurrencyCodeUseCase  @Inject constructor(private val repository: UserDataStoreRepository) {

    suspend operator fun invoke(): String {
        return repository.getCurrencyCode()
    }
}