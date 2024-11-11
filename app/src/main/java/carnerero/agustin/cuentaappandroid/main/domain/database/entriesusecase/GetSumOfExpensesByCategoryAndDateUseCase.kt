package carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.EntryRepository
import javax.inject.Inject

class GetSumOfExpensesByCategoryAndDateUseCase @Inject constructor(private val repository: EntryRepository) {

    suspend operator fun invoke(
        categoryId: Int,
        fromDate: String,
        toDate: String
    ): Double = repository.getSumOfExpensesByCategoryAndDate(categoryId,fromDate,toDate)
}