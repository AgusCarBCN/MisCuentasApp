package carnerero.agustin.cuentaappandroid.domain.database.accountusecase

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.repository.AccountRepository
import java.math.BigDecimal
import javax.inject.Inject

class DeleteRecordAndUpdateBalanceUseCase @Inject constructor(val repository: AccountRepository) {
    suspend operator fun invoke(record: RecordDTO) {
        return repository.deleteRecordAndUpdateBalance(record)
    }
}