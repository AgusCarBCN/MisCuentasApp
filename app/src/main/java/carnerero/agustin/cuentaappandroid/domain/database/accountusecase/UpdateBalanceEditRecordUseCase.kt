package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import jakarta.inject.Inject
import java.math.BigDecimal

class UpdateBalanceEditRecordUseCase  @Inject constructor(private val repository: AccountRepository){

    suspend operator fun invoke(accountId: Int, amount: BigDecimal) {
        return repository.updateBalanceEditRecord(accountId, amount)
    }
}