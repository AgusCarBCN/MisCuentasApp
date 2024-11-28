package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase


import carnerero.agustin.cuentaappandroid.data.db.entities.Entry
import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import javax.inject.Inject

class InsertEntryUseCase @Inject constructor(private val repository: EntryRepository){

    suspend operator fun invoke(newEntry: Entry) {
        repository.insertEntry(newEntry)
    }
}
