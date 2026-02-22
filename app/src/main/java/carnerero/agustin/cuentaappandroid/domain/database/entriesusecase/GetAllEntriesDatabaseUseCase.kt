package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllEntriesDatabaseUseCase   @Inject constructor(private val repository: RecordRepository){

    suspend operator fun invoke(): Flow<List<RecordDTO>> =
        repository.getAllEntriesDTO()

}