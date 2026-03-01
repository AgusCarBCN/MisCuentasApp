package carnerero.agustin.cuentaappandroid.presentation.ui.setting

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Account

data class SettingUiState(
    val accounts:List<Account> =emptyList(),
    val records: List<RecordDTO> =emptyList(),
    val switchTutorial: Boolean =true,
    val switchDarkTheme:Boolean=true,
    val switchNotifications:Boolean=true,
    val showExportDialog:Boolean=false,
    val showImportAccountDialog:Boolean=false,
    val showImportRecordsDialog:Boolean=false
)
