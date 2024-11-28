package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import javax.inject.Inject

class GetAccountByIdUseCase @Inject constructor(private val repository: AccountRepository)
{
    suspend operator fun invoke(id: Int): Account? {
        return repository.getAccountById(id)
    }
}