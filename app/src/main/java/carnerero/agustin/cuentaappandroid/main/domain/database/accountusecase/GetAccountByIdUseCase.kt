package carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.main.data.database.entities.Account
import carnerero.agustin.cuentaappandroid.main.data.database.repository.AccountRepository
import javax.inject.Inject

class GetAccountByIdUseCase @Inject constructor(private val repository: AccountRepository)
{
    suspend operator fun invoke(id: Int): Account? {
        return repository.getAccountById(id)
    }
}