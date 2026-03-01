package carnerero.agustin.cuentaappandroid.presentation.ui.setting

sealed class SettingEffects {

    object MessageExport: SettingEffects()
    object ErrorExport: SettingEffects()
    object ErrorImport: SettingEffects()
    object MessageNoRecords: SettingEffects()
    object MessageImport: SettingEffects()
    object MessageNoValidAccountFile: SettingEffects()
    object MessageNoValidRecordFile: SettingEffects()
}