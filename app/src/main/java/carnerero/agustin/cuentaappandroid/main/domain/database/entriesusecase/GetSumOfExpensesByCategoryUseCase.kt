package carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.EntryRepository
import javax.inject.Inject

class GetSumOfExpensesByCategoryUseCase @Inject constructor(private val repository: EntryRepository) {

    suspend operator fun invoke(
        categoryId: Int
    ): Double = repository.getSumOfExpensesEntriesByCategory(categoryId)
}