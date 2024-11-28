package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountDateFromUseCase  @Inject constructor(private val repository: AccountRepository) {

    suspend operator fun invoke(accountId: Int, newFromDate: String) {
        repository.updateFromDateAccount(accountId, newFromDate)
    }
}