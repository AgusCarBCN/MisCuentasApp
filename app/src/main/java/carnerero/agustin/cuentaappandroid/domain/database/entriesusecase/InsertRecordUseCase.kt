package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase


import carnerero.agustin.cuentaappandroid.data.db.entities.Records
import carnerero.agustin.cuentaappandroid.data.repository.RecordRepository
import javax.inject.Inject

class InsertRecordUseCase @Inject constructor(private val repository: RecordRepository){

    suspend operator fun invoke(newEntry: Records) {
        repository.insertEntry(newEntry)
    }
}
