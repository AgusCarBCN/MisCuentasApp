package carnerero.agustin.cuentaappandroid.presentation.ui.records.get

import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO

data class GetRecordsUiState (
    val listOfRecords:List<EntryDTO> = emptyList(),
    val enableByDate: Boolean=false,
    val currencyCode: String = "EUR"
)