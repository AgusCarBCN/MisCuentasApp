package carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.EntryRepository
import javax.inject.Inject

class GetSumTotalExpensesUseCase  @Inject constructor(private val repository: EntryRepository){

    suspend operator fun invoke():Double = repository.getSumExpenses()

}