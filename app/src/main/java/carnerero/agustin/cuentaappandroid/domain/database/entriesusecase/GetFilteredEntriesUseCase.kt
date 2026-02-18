package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import carnerero.agustin.cuentaappandroid.presentation.ui.search.model.SearchFilter
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.util.Date
import javax.inject.Inject

class GetFilteredEntriesUseCase @Inject constructor(private val repository: EntryRepository) {

    operator fun invoke(
        searchFilter: SearchFilter
    ): Flow<List<EntryDTO>> =
        repository.getEntriesFiltered(searchFilter)
}g