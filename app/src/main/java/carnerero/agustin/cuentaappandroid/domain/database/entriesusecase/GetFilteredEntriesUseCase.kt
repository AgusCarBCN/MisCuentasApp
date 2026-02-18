package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.repository.EntryRepository
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.SearchFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredEntriesUseCase @Inject constructor(private val repository: EntryRepository) {

    operator fun invoke(
        searchFilter: SearchFilter
    ): Flow<List<EntryDTO>> =
        repository.getEntriesFiltered(searchFilter)
}