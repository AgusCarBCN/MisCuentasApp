package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import javax.inject.Inject

class UpdateEntriesAmountByExchangeRateUseCase @Inject constructor(private val repository: EntryRepository){

    suspend operator fun invoke(rate:Double) {
        repository.updateEntriesAmountByExchangeRate(rate)
    }
}