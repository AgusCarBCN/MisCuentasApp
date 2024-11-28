package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import javax.inject.Inject

class TransferUseCase  @Inject constructor(private val repository: AccountRepository) {

    suspend operator fun invoke(fromAccountId: Int, toAccountId: Int, newBalance: Double) {
        repository.transferFunds(fromAccountId, toAccountId, newBalance)
    }
}