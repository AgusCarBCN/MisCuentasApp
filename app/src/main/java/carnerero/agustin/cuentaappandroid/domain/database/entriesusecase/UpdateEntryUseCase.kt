package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import java.math.BigDecimal
import javax.inject.Inject

class UpdateEntryUseCase  @Inject constructor(private val repository: EntryRepository){

    suspend operator fun invoke(id: Long, description: String, amount: BigDecimal, date: String) {
        repository.updateEntry(id, description, amount, date)
    }
}