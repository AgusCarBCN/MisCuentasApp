package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import androidx.room.Insert
import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import javax.inject.Inject

class GetAllEntriesByDateUseCase @Inject constructor(private val repository: EntryRepository ) {
    suspend fun getEntriesByDate(accountId: Int,
                                 fromDate: String,
                                 toDate:String)=repository.getEntriesByDate(accountId,fromDate,toDate)
}