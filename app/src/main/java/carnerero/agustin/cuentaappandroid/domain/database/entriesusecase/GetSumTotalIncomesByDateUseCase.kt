package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.RecordRepository
import java.math.BigDecimal
import javax.inject.Inject

class GetSumTotalIncomesByDate @Inject constructor(private val repository: RecordRepository){

    suspend operator fun invoke(accountId:Int,
                                fromDate:String,
                                toDate:String
                                ): BigDecimal = repository.getSumIncomesByDate(accountId,
                                    fromDate,
                                    toDate)

}