package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import javax.inject.Inject

class UpdateAmountUseCase  @Inject constructor(private val repository: EntryRepository){

    suspend operator fun invoke(accountId:Long,newBalance:Double) {
        repository.updateAmountEntry(accountId,newBalance)
    }
}