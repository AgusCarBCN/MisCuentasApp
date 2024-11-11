package carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.AccountRepository
import javax.inject.Inject

class DeleteAllAccountsUseCase @Inject constructor(private val repository: AccountRepository){

    suspend operator fun invoke() {
        return repository.deleteAllAccounts()
    }
}