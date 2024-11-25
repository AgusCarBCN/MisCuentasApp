package carnerero.agustin.cuentaappandroid.components

import carnerero.agustin.cuentaappandroid.main.data.database.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Entry

data class EntryWithCheckBox(val entry: EntryDTO, var checkbox:Boolean)