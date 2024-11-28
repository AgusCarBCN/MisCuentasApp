package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountNameUseCase @Inject constructor(private val repository: AccountRepository){

    suspend operator fun invoke(accountId: Int, newName: String) {
        return repository.updateAccountName(accountId, newName)
    }
}

