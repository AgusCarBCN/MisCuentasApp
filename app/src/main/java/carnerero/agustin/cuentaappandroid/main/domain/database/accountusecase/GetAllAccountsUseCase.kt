package carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.main.data.database.entities.Account
import carnerero.agustin.cuentaappandroid.main.data.database.repository.AccountRepository
import javax.inject.Inject

class GetAllAccountsUseCase @Inject constructor(private val repository: AccountRepository){

    suspend operator fun invoke(): List<Account> {
        return repository.getAllAccounts()
    }
}
