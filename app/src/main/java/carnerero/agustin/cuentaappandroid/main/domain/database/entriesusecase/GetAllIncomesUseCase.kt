package carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.main.data.database.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.main.data.database.repository.EntryRepository
import javax.inject.Inject

class GetAllIncomesUseCase   @Inject constructor(private val repository: EntryRepository){

    suspend operator fun invoke():List<EntryDTO> =
        repository.getAllIncomesDTO()

}