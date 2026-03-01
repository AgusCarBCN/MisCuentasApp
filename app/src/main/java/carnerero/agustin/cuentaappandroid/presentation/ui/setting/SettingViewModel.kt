package carnerero.agustin.cuentaappandroid.presentation.ui.setting

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.ui.res.stringResource
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.domain.datastore.GetEnableDarkThemUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetEnableNotificationsUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetEnableTutorialUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetShowTutorialUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetEnableDarkThemeUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetEnableNotificationsUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetEnableTutorialUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetShowTutorialUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.AccountCSV
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.EntryCSV
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.SettingsDialog
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.utils.Utils
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Date
import javax.inject.Inject

@HiltViewModel

class SettingViewModel @Inject constructor(
    private val getShowTutorial: GetShowTutorialUseCase,
    private val setShowTutorial: SetShowTutorialUseCase,
    private val getSwitchTutorial: GetEnableTutorialUseCase,
    private val changeSwitchTutorial: SetEnableTutorialUseCase,
    private val getSwitchDarkTheme: GetEnableDarkThemUseCase,
    private val changeSwitchDarkTheme: SetEnableDarkThemeUseCase,
    private val getNotificationsTutorial: GetEnableNotificationsUseCase,
    private val changeSwitchNotifications: SetEnableNotificationsUseCase

) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState

    private val _effect = MutableSharedFlow<SettingUiState>()
    val effect = _effect.asSharedFlow()

    init {
        observeInitialData()
    }

    private fun observeInitialData() {
        viewModelScope.launch {
            combine(
                getSwitchDarkTheme.invoke(),
                getSwitchTutorial.invoke(),
                getNotificationsTutorial.invoke()
            ) { enableDarkTheme, showTutorial, enableNotifications ->
                _uiState.value.copy(
                    switchDarkTheme = enableDarkTheme,
                    switchTutorial = showTutorial,
                    switchNotifications = enableNotifications
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun onEvenUser(event: SettingsUserEvents) {
        when (event) {
            is SettingsUserEvents.OnShowLaunchersDialog -> TODO()
            is SettingsUserEvents.OnSwitchDarkThemeChange -> TODO()
            is SettingsUserEvents.OnSwitchShowNotificationsChange -> TODO()
            is SettingsUserEvents.OnSwitchShowTutorialChange -> TODO()
            is SettingsUserEvents.OnConfirmAccountImport -> TODO()
            is SettingsUserEvents.OnConfirmExport -> TODO()
            is SettingsUserEvents.OnConfirmRecordsImport -> TODO()
        }
    }

    fun onShowLauncherDialog(field: SettingsDialog, visible: Boolean) {
        _uiState.update { current ->
            current.copy(
                showExportDialog = if (field == SettingsDialog.EXPORT) visible else current.showExportDialog,
                showImportAccountDialog = if (field == SettingsDialog.IMPORT_ACCOUNT) visible else current.showImportAccountDialog,
                showImportRecordsDialog = if (field == SettingsDialog.IMPORT_ENTRIES) visible else current.showImportRecordsDialog,
            )
        }
    }

    fun exportData(
        uri: Uri,
        entries: List<RecordDTO>,
        accounts: List<Account>,
        context: Context
    ) {
        val date = Date().dateFormat()
        val fileRecordsName = "backupRecords$date"
        val fileAccountsName = "backupAccounts$date"
        val entriesCSV = toEntryCSV(entries,context)
        val accountsCSV = toAccountCSV(accounts)
        val directory = DocumentFile.fromTreeUri(context, uri) // Direct assignment
        if (directory != null && directory.isDirectory) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    if (entries.isNotEmpty()) {
                        Utils.writeCsvEntriesFile(
                            entriesCSV,
                            context,
                            fileRecordsName,
                            directory
                        )
                    }
                    if (accounts.isNotEmpty()) {
                        Utils.writeCsvAccountsFile(
                            accountsCSV,
                            context,
                            fileAccountsName,
                            directory
                        )
                    }
                    if (entries.isNotEmpty() || accounts.isNotEmpty()) {
                        /*withContext(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    messageExport
                                )
                            )
                        }*/
                    } else {
                        /* withContext(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    messageNoEntries
                                )
                            )
                        }*/
                    }

                } catch (_: IOException) {
                    /*withContext(Dispatchers.Main) {
                        SnackBarController.sendEvent(event = SnackBarEvent(errorExport))
                    }*/
                }
            }

        }
    }

    private fun toEntryCSV(entries: List<RecordDTO>,context: Context): MutableList<EntryCSV> {

        val entriesCSV = mutableListOf<EntryCSV>()
        entries.forEach { entry ->
            entriesCSV.add(
                EntryCSV(
                    entry.description,
                    context.getString( entry.nameResource),
                    entry.amount.toDouble(),
                    entry.date,
                    entry.name,
                    entry.categoryId,
                    entry.accountId,
                )
            )
        }
        return entriesCSV
    }
    private fun toAccountCSV(accounts: List<Account>): MutableList<AccountCSV> {

        val accountsCSV = mutableListOf<AccountCSV>()
        accounts.forEach { account ->
            accountsCSV.add(
                AccountCSV(
                    account.name,
                    account.balance.toDouble(),
                    account.id
                )
            )
        }
        return accountsCSV
    }
}

