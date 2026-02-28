package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import javax.inject.Inject

class GetSumExpensesByMonthUseCase @Inject constructor(private val repository: RecordRepository) {

    operator fun invoke(
        accountId: Int,
        month: String,
        year: String
    ): Flow<BigDecimal?> = repository.getSumOfExpensesEntriesForMonth(accountId, month, year)
}