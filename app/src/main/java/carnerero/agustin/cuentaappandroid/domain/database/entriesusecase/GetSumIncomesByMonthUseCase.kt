package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import javax.inject.Inject

class GetSumIncomesByMonthUseCase @Inject constructor(private val repository: EntryRepository) {

    suspend operator fun invoke(
        accountId: Int,
        month: String,
        year:String
    ): Double = repository.getSumOfIncomeEntriesForMonth(accountId, month, year)
}