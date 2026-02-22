package carnerero.agustin.cuentaappandroid.presentation.ui.records.modify.model

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO

data class EntryWithCheckBox(val entry: RecordDTO, var checkbox:Boolean)