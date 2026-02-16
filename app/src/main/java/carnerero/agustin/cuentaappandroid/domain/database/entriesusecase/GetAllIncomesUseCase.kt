package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllIncomesUseCase   @Inject constructor(private val repository: EntryRepository){

    operator fun invoke(): Flow<List<EntryDTO>> =
        repository.getAllIncomesDTO()

}