package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import java.math.BigDecimal
import javax.inject.Inject

class UpdateAmountUseCase  @Inject constructor(private val repository: EntryRepository){

     suspend operator fun invoke(accountId:Long,newBalance: BigDecimal) {
        repository.updateAmountEntry(accountId,newBalance)
    }

}