package carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.EntryRepository
import javax.inject.Inject

class GetSumIncomesByMonthUseCase @Inject constructor(private val repository: EntryRepository) {

    suspend operator fun invoke(
        accountId: Int,
        month: String,
        year:String
    ): Double = repository.getSumOfIncomeEntriesForMonth(accountId, month, year)
}