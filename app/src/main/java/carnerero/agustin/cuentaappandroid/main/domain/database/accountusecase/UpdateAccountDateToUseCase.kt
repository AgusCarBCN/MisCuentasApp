package carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountDateToUseCase @Inject constructor(private val repository: AccountRepository) {

    suspend operator fun invoke(accountId: Int, newFromDate: String) {
        repository.updateToDateAccount(accountId, newFromDate)
    }
}