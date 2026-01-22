package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import java.math.BigDecimal
import javax.inject.Inject

class UpdateSpendingLimitAccountUseCase @Inject constructor(private val repository: AccountRepository){
    suspend operator fun invoke(accountId:Int,newLimit: BigDecimal) {
        repository.updateSpendingLimitAccount(accountId, newLimit)
    }
}