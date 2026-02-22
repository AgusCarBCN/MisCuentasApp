package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import java.math.BigDecimal
import javax.inject.Inject

class WithDrawUseCase @Inject constructor(private val repository: AccountRepository) {

    suspend operator fun invoke(accountId: Int, amount: BigDecimal) {
        return repository.withDraw(accountId, amount)
    }
}