package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import javax.inject.Inject

class UpdateSpendingLimitAccountUseCase @Inject constructor(private val repository: AccountRepository){
    suspend operator fun invoke(accountId:Int,newLimit:Double) {
        repository.updateSpendingLimitAccount(accountId, newLimit)
    }
}