package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository

import javax.inject.Inject

class UpdateCheckedAccountUseCase  @Inject constructor(private val repository: AccountRepository){

    suspend operator fun invoke(accountId:Int,newValue:Boolean) {
        repository.updateCheckedAccount(accountId, newValue)
    }
}