package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import javax.inject.Inject

class GetSumTotalIncomesUseCase @Inject constructor(private val repository: EntryRepository){

    operator fun invoke(): Flow<BigDecimal> = repository.getSumIncomes()

}