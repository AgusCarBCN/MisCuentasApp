package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import javax.inject.Inject

class InsertAccountUseCase @Inject constructor(private val repository: AccountRepository){

    suspend operator fun invoke(newAccount: Account) {
         repository.insertAccount(newAccount)
    }
}