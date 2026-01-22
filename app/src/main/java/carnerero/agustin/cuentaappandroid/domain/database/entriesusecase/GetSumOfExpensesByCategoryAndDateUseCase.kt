package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import java.math.BigDecimal
import javax.inject.Inject

class GetSumOfExpensesByCategoryAndDateUseCase @Inject constructor(private val repository: EntryRepository) {

    suspend operator fun invoke(
        categoryId: Int,
        fromDate: String,
        toDate: String
    ): BigDecimal = repository.getSumOfExpensesByCategoryAndDate(categoryId,fromDate,toDate)
}