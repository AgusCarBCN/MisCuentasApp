package carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.AccountRepository
import javax.inject.Inject

class UpdateSpendingLimitAccountUseCase @Inject constructor(private val repository: AccountRepository){
    suspend operator fun invoke(accountId:Int,newLimit:Double) {
        repository.updateSpendingLimitAccount(accountId, newLimit)
    }
}