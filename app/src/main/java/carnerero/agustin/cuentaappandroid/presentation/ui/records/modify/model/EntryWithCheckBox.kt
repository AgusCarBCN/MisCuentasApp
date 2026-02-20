package carnerero.agustin.cuentaappandroid.presentation.ui.records.modify.model

import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO

data class EntryWithCheckBox(val entry: EntryDTO, var checkbox:Boolean)