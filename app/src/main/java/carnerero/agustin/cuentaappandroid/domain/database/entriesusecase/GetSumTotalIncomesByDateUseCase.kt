package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import javax.inject.Inject

class GetSumTotalIncomesByDate @Inject constructor(private val repository: RecordRepository){

    operator fun invoke(accountId:Int,
                                fromDate:String,
                                toDate:String
                                ): Flow<BigDecimal?> = repository.getSumIncomesByDate(accountId,
                                    fromDate,
                                    toDate)

}