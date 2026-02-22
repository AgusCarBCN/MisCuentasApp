package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.RecordRepository
import java.math.BigDecimal
import javax.inject.Inject

class GetSumOfExpensesByCategoryUseCase @Inject constructor(private val repository: RecordRepository) {

    suspend operator fun invoke(
        categoryId: Int
    ): BigDecimal = repository.getSumOfExpensesEntriesByCategory(categoryId)
}