package carnerero.agustin.cuentaappandroid.presentation.ui.records

import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO

data class RecordsUiState (
    val listOfRecords:List<EntryDTO> = emptyList(),
    val enableByDate: Boolean=false,
    val currencyCode: String = "EUR"
)