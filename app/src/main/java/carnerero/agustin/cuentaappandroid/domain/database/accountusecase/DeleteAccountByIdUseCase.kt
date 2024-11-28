package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import javax.inject.Inject

class DeleteAccountByIdUseCase @Inject constructor(private val repository: AccountRepository){

    suspend operator fun invoke(accountId: Int) {
        return repository.deleteAccount(repository.getAccountById(accountId)!!)
    }

}