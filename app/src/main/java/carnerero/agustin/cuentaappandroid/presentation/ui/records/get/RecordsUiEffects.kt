package carnerero.agustin.cuentaappandroid.presentation.ui.records.get

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO

sealed class RecordsUiEffects{

    data class NavigateToEdit(val record: RecordDTO) : RecordsUiEffects()


}