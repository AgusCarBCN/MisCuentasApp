package carnerero.agustin.cuentaappandroid.presentation.ui.setting

import android.content.Context
import android.net.Uri
import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.SettingsDialog

sealed class SettingsUserEvents {

    data class OnSwitchDarkThemeChange(val newValue: Boolean) : SettingsUserEvents()
    data class OnSwitchShowTutorialChange(val newValue: Boolean) : SettingsUserEvents()
    data class OnSwitchShowNotificationsChange(val newValue: Boolean) : SettingsUserEvents()
    data class OnShowLaunchersDialog(
        val field: SettingsDialog,
        val visible: Boolean
    ) : SettingsUserEvents()

    data class OnConfirmExport(
        val uri: Uri,
        val accounts: List<Account>,
        val records: List<RecordDTO>,
        val context: Context
    ): SettingsUserEvents()
    data class OnConfirmAccountImport(
        val uri: Uri,
        val accounts: List<Account>,
        val context: Context
    ): SettingsUserEvents()
    data class OnConfirmRecordsImport(
        val uri: Uri,
        val accounts: List<RecordDTO>,
        val context: Context
    ): SettingsUserEvents()
}