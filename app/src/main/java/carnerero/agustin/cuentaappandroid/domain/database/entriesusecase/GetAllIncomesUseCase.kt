package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllIncomesUseCase   @Inject constructor(private val repository: RecordRepository){

    operator fun invoke(): Flow<List<RecordDTO>> =
        repository.getAllIncomesDTO()

}