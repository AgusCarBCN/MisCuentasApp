package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountBalanceUseCase   @Inject constructor(private val repository: AccountRepository){

    suspend operator fun invoke(accountId: Int, newBalance: Double) {
        return repository.updateAccountBalance(accountId, newBalance)
    }
}
