package carnerero.agustin.cuentaappandroid.domain.database.entriesusecase

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.repository.RecordRepository
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.SearchFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredEntriesUseCase @Inject constructor(private val repository: RecordRepository) {

    operator fun invoke(
        searchFilter: SearchFilter
    ): Flow<List<RecordDTO>> =
        repository.getEntriesFiltered(searchFilter)
}