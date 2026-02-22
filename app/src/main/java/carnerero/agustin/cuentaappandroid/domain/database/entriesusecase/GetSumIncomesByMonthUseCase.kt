package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.RecordRepository
import java.math.BigDecimal
import javax.inject.Inject

class GetSumIncomesByMonthUseCase @Inject constructor(private val repository: RecordRepository) {

    suspend operator fun invoke(
        accountId: Int,
        month: String,
        year:String
    ): BigDecimal= repository.getSumOfIncomeEntriesForMonth(accountId, month, year)
}