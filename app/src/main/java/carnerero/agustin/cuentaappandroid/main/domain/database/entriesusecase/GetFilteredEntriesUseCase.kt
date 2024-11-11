package carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.main.data.database.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.main.data.database.repository.EntryRepository
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.util.Date
import javax.inject.Inject

class GetFilteredEntriesUseCase @Inject constructor(private val repository: EntryRepository) {

    suspend operator fun invoke(
        accountId: Int,
        description: String,
        dateFrom: String = Date().dateFormat(),
        dateTo: String = Date().dateFormat(),
        amountMin: Double = 0.0,
        amountMax: Double = Double.MAX_VALUE,
        selectedOptions: Int = 0
    ): List<EntryDTO> =
        repository.getEntriesFiltered(
            accountId,
            description,
            dateFrom,
            dateTo,
            amountMin,
            amountMax,
            selectedOptions
        )
}