package carnerero.agustin.cuentaappandroid.presentation.ui.setting

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.InsertAccountUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetAllEntriesDatabaseUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.InsertRecordUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetEnableDarkThemUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetEnableNotificationsUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetEnableTutorialUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetEnableDarkThemeUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetEnableNotificationsUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.SetEnableTutorialUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.AccountCSV
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.EntryCSV
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.SettingsDialog
import carnerero.agustin.cuentaappandroid.utils.Utils
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Date
import javax.inject.Inject

@HiltViewModel

class SettingViewModel @Inject constructor(
    private val allAccounts: GetAllAccountsUseCase,
    private val allRecords: GetAllEntriesDatabaseUseCase,
    private val getSwitchTutorial: GetEnableTutorialUseCase,
    private val changeSwitchTutorial: SetEnableTutorialUseCase,
    private val getSwitchDarkTheme: GetEnableDarkThemUseCase,
    private val changeSwitchDarkTheme: SetEnableDarkThemeUseCase,
    private val getNotificationsTutorial: GetEnableNotificationsUseCase,
    private val changeSwitchNotifications: SetEnableNotificationsUseCase,
    private val addAccount: InsertAccountUseCase,
    private val addRecord: InsertRecordUseCase,
    @param:ApplicationContext private val context: Context
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState

    private val _effect = MutableSharedFlow<SettingEffects>()
    val effect = _effect.asSharedFlow()

    init {
        observeInitialData()
    }

    private fun observeInitialData() {
        viewModelScope.launch {
            combine(
                getSwitchDarkTheme.invoke(),
                getSwitchTutorial.invoke(),
                getNotificationsTutorial.invoke(),
                allAccounts.invoke(),
                allRecords.invoke()
            ) { enableDarkTheme, showTutorial, enableNotifications,accounts,records ->
                _uiState.value.copy(
                    switchDarkTheme = enableDarkTheme,
                    switchTutorial = showTutorial,
                    switchNotifications = enableNotifications,
                    accounts = accounts,
                    records = records
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun onEvenUser(event: SettingsUserEvents) {
        when (event) {
            is SettingsUserEvents.OnShowLaunchersDialog -> onShowLauncherDialog(event.field,
                event.visible)
            is SettingsUserEvents.OnSwitchDarkThemeChange -> onSwitchDarkTheme(event.newValue)
            is SettingsUserEvents.OnSwitchShowNotificationsChange -> onSwitchNotifications(event.newValue)
            is SettingsUserEvents.OnSwitchShowTutorialChange -> onSwitchShowTutorial(event.newValue)
            is SettingsUserEvents.OnConfirmAccountImport -> importAccountsData(event.uri)
            is SettingsUserEvents.OnConfirmExport -> exportData(event.uri)
            is SettingsUserEvents.OnConfirmRecordsImport -> importRecordsDate(event.uri)
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
    fun onSwitchDarkTheme(newValue:Boolean){
        viewModelScope.launch {
            changeSwitchDarkTheme.invoke(newValue)
            _uiState.update { current ->
                current.copy(
                    switchDarkTheme = newValue
                )
            }
        }
    }
    fun onSwitchNotifications(newValue:Boolean){
        viewModelScope.launch {
            changeSwitchNotifications.invoke(newValue)
            _uiState.update { current ->
                current.copy(
                    switchNotifications = newValue
                )
            }
        }
    }
    fun onSwitchShowTutorial(newValue:Boolean){
        viewModelScope.launch {
            changeSwitchTutorial.invoke(newValue)
            _uiState.update { current ->
                current.copy(
                    switchTutorial = newValue
                )
            }
        }
    }
    fun exportData(
        uri: Uri
    ) {
        val accounts=_uiState.value.accounts
        val records=_uiState.value.records
        val date = Date().dateFormat()
        val fileRecordsName = "backupRecords$date"
        val fileAccountsName = "backupAccounts$date"
        val entriesCSV = toEntryCSV(records,context)
        val accountsCSV = toAccountCSV(accounts)

        val directory = DocumentFile.fromTreeUri(context, uri) // Direct assignment
        if (directory != null && directory.isDirectory) {
            viewModelScope.launch(Dispatchers.IO) {

                try {
                    if (records.isNotEmpty()) {
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
                    if (records.isNotEmpty() || accounts.isNotEmpty()) {
                        _effect.emit(SettingEffects.MessageExport)
                        /*withContext(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    messageExport
                                )
                            )
                        }*/
                    } else {
                        _effect.emit(SettingEffects.MessageNoRecords)
                        /* withContext(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    messageNoEntries
                                )
                            )
                        }*/
                    }

                } catch (_: IOException) {
                    _effect.emit(SettingEffects.ErrorExport)
                    /*withContext(Dispatchers.Main) {
                        SnackBarController.sendEvent(event = SnackBarEvent(errorExport))
                    }*/
                }
            }

        }
    }
    fun importAccountsData(uri:Uri){
        val fileName = uri.path.toString()
        // Lanzamiento de una corutina en un contexto de IO
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (fileName.contains("Accounts")) {
                    val accountsToRead =
                        Utils.readCsvAccountsFile(context, uri)
                    for (account in accountsToRead) {
                        addAccount(account)
                    }
                    _effect.emit(SettingEffects.MessageImport)
                    // Cambiamos al hilo principal para mostrar el SnackBar
                   /* withContext(Dispatchers.Main) {
                        SnackBarController.sendEvent(event = SnackBarEvent(messageImport))
                    }*/
                } else {
                    _effect.emit(SettingEffects.MessageNoValidAccountFile)
                   /* withContext(Dispatchers.Main) {
                        SnackBarController.sendEvent(
                            event = SnackBarEvent(
                                messageNoValidAccountsFile
                            )
                        )
                    }*/

                }
            } catch (_: IOException) {
                _effect.emit(SettingEffects.ErrorImport)

                /*withContext(Dispatchers.Main) {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            errorImport
                        )
                    )
                }*/
            }
        }
    }

    fun importRecordsDate(uri:Uri){
        val fileName = uri.path.toString()
        // Lanzamiento de una corutina en un contexto de IO
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (fileName.contains("Records")) {
                    val entriesToRead =
                        Utils.readCsvEntriesFile(context, uri)
                    for (entry in entriesToRead) {
                       addRecord(entry)
                    }
                    _effect.emit(SettingEffects.MessageImport)
                    // Cambiamos al hilo principal para mostrar el SnackBar
                    /*withContext(Dispatchers.Main) {
                        SnackBarController.sendEvent(event = SnackBarEvent(messageImport))
                    }*/
                } else {
                    _effect.emit(SettingEffects.MessageNoValidRecordFile)
                  /*  withContext(Dispatchers.Main) {
                        SnackBarController.sendEvent(
                            event = SnackBarEvent(
                                messageNoValidEntriesFile
                            )
                        )
                    }*/
                }
            } catch (_: IOException) {
                _effect.emit(SettingEffects.ErrorImport)
               /* withContext(Dispatchers.Main) {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            errorImport
                        )
                    )
                }*/
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

