package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.RecordRepository
import javax.inject.Inject

class GetAllEntriesByDateUseCase @Inject constructor(private val repository: RecordRepository ) {
    suspend fun getEntriesByDate(accountId: Int,
                                 fromDate: String,
                                 toDate:String)=repository.getEntriesByDate(accountId,fromDate,toDate)
}