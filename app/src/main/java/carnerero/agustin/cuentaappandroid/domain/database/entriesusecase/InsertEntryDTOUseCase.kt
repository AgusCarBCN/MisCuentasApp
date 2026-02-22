package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.repository.RecordRepository
import javax.inject.Inject

class InsertEntryDTOUseCase @Inject constructor(private val repository: RecordRepository){

    suspend operator fun invoke(newEntry: RecordDTO) {
        repository.insertEntryDTO(newEntry)
    }
}