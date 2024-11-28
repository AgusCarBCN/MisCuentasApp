package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import javax.inject.Inject

class InsertEntryDTOUseCase @Inject constructor(private val repository: EntryRepository){

    suspend operator fun invoke(newEntry: EntryDTO) {
        repository.insertEntryDTO(newEntry)
    }
}