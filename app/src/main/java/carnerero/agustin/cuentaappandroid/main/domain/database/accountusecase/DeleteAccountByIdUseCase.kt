package carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.AccountRepository
import javax.inject.Inject

class DeleteAccountByIdUseCase @Inject constructor(private val repository: AccountRepository){

    suspend operator fun invoke(accountId: Int) {
        return repository.deleteAccount(repository.getAccountById(accountId)!!)
    }

}