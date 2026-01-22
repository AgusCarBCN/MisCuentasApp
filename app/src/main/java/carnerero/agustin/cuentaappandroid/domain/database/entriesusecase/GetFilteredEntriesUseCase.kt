package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.math.BigDecimal
import java.util.Date
import javax.inject.Inject

class GetFilteredEntriesUseCase @Inject constructor(private val repository: EntryRepository) {

    suspend operator fun invoke(
        accountId: Int,
        description: String,
        dateFrom: String = Date().dateFormat(),
        dateTo: String = Date().dateFormat(),
        amountMin: BigDecimal = BigDecimal.ZERO,
        amountMax: BigDecimal = BigDecimal.valueOf(Double.MAX_VALUE),
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