package carnerero.agustin.cuentaappandroid.presentation.ui.records.get

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO

data class GetRecordsUiState (
    val listOfRecords:List<RecordDTO> = emptyList(),
    val enableByDate: Boolean=false,
    val currencyCode: String = "EUR"
)