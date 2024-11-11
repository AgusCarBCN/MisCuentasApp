package carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.EntryRepository
import javax.inject.Inject

class UpdateAmountUseCase  @Inject constructor(private val repository: EntryRepository){

    suspend operator fun invoke(accountId:Long,newBalance:Double) {
        repository.updateAmountEntry(accountId,newBalance)
    }
}