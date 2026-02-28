package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import javax.inject.Inject

class GetSumOfExpensesByCategoryAndDateUseCase @Inject constructor(private val repository: RecordRepository) {

    operator fun invoke(
        categoryId: Int,
        fromDate: String,
        toDate: String
    ): Flow<BigDecimal?> = repository.getSumOfExpensesByCategoryAndDate(categoryId,fromDate,toDate)
}