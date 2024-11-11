package carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.EntryRepository
import javax.inject.Inject

class GetSumExpensesByMonthUseCase @Inject constructor(private val repository: EntryRepository) {

    suspend operator fun invoke(
        accountId: Int,
        month: String,
        year: String
    ): Double = repository.getSumOfExpensesEntriesForMonth(accountId, month, year)
}