package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountBalanceByExchangeRateUseCase  @Inject constructor(private val repository: AccountRepository){

    suspend operator fun invoke(rate: Double) {
        return repository.updateAccountBalanceByExchangeRate (rate)
    }
}