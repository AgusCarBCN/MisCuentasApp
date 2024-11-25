package carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.main.data.database.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Entry
import carnerero.agustin.cuentaappandroid.main.data.database.repository.EntryRepository
import javax.inject.Inject

class DeleteEntryUseCase @Inject constructor(private val repository: EntryRepository){

    suspend operator fun invoke(entryDTO: EntryDTO) {
        repository.deleteEntry(entryDTO)
    }
}
