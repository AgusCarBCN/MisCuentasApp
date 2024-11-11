package carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.EntryRepository
import javax.inject.Inject

class GetSumTotalExpensesByDateUseCase @Inject constructor(private val repository: EntryRepository){

    suspend operator fun invoke(accountId:Int,
                                fromDate:String,
                                toDate:String
    ):Double = repository.getSumExpensesByDate(accountId,
        fromDate,
        toDate)

}