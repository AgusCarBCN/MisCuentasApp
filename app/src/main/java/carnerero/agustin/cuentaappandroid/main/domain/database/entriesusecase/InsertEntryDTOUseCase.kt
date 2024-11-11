package carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.main.data.database.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.main.data.database.repository.EntryRepository
import javax.inject.Inject

class InsertEntryDTOUseCase @Inject constructor(private val repository: EntryRepository){

    suspend operator fun invoke(newEntry: EntryDTO) {
        repository.insertEntryDTO(newEntry)
    }
}