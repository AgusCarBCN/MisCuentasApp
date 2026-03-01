package carnerero.agustin.cuentaappandroid.presentation.ui.setting

data class SettingUiState(
    val switchTutorial: Boolean =true,
    val switchDarkTheme:Boolean=true,
    val switchNotifications:Boolean=true,
    val showExportDialog:Boolean=false,
    val showImportAccountDialog:Boolean=false,
    val showImportRecordsDialog:Boolean=false
)
