package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import java.math.BigDecimal
import javax.inject.Inject

class GetSumTotalExpensesByDateUseCase @Inject constructor(private val repository: EntryRepository){

    suspend operator fun invoke(accountId:Int,
                                fromDate:String,
                                toDate:String
    ): BigDecimal = repository.getSumExpensesByDate(accountId,
        fromDate,
        toDate)

}