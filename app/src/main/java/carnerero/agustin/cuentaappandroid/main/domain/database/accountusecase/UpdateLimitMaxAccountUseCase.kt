package carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.AccountRepository

import javax.inject.Inject

class UpdateLimitMaxAccountUseCase @Inject constructor(private val repository: AccountRepository){

    suspend operator fun invoke(accountId:Int,newLimitMax:Float) {
        repository.updateLimitMaxAccount(accountId, newLimitMax)
    }
}