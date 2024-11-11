package carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.EntryRepository
import javax.inject.Inject

class GetSumTotalIncomesByDate @Inject constructor(private val repository: EntryRepository){

    suspend operator fun invoke(accountId:Int,
                                fromDate:String,
                                toDate:String
                                ):Double = repository.getSumIncomesByDate(accountId,
                                    fromDate,
                                    toDate)

}