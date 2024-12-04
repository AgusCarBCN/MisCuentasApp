package carnerero.agustin.cuentaappandroid.presentation.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.RowComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.SwitchComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.AccountCSV
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.EntryCSV
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.utils.Utils
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Date


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable

fun SettingScreen(
    settingViewModel: SettingViewModel,
    mainViewModel: MainViewModel,
    accountsViewModel: AccountsViewModel,
    entriesViewModel: EntriesViewModel,
    navToCreateAccounts: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        entriesViewModel.getAllEntriesDTO()
    }

    val scope = rememberCoroutineScope()
    val permissionNotificationGranted by mainViewModel.notificationPermissionGranted.collectAsState()
    val switchTutorial by settingViewModel.switchTutorial.observeAsState(true)
    val switchDarkTheme by settingViewModel.switchDarkTheme.observeAsState(false)
    val switchNotifications by settingViewModel.switchNotifications.observeAsState(false)
    val entries by entriesViewModel.listOfEntriesDTO.collectAsState()
    val accounts by accountsViewModel.listOfAccounts.observeAsState(mutableListOf())
    val entriesCSV = toEntryCSV(entries)
    val accountsCSV = toAccountCSV(accounts)
    val date = Date().dateFormat()
    val fileRecordsName = "backupRecords$date"
    val fileAccountsName = "backupAccounts$date"
    val messageExport = stringResource(id = R.string.exportData)
    val messageImport = stringResource(id = R.string.loadbackup)
    val messageNoEntries = stringResource(id = R.string.noentries)
    val messageNoAccounts = stringResource(id = R.string.noaccounts)
    val messageNoValidEntriesFile=stringResource(id = R.string.novalidrecordcsv)
    val messageEntriesWithoutAccounts= stringResource(id = R.string.loadentrieswithoutaccount)
    val errorExport = stringResource(id = R.string.errorexport)
    val errorImport = stringResource(id = R.string.errorimport)

    val pickerExportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                val directory = DocumentFile.fromTreeUri(context, uri) // Direct assignment
                if (directory != null && directory.isDirectory) {
                    scope.launch(Dispatchers.IO) {
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
                                        withContext(Dispatchers.Main) {
                                            SnackBarController.sendEvent(
                                                event = SnackBarEvent(
                                                    messageExport
                                                )
                                            )
                                        }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        SnackBarController.sendEvent(
                                            event = SnackBarEvent(
                                                messageNoEntries
                                            )
                                        )
                                    }
                                }

                        } catch (e: IOException) {
                            withContext(Dispatchers.Main) {
                                SnackBarController.sendEvent(event = SnackBarEvent(errorExport))
                            }
                        }
                    }

                }
            }
        }
    }
    val pickerImportLauncherEntries = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {

        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                val fileName=uri.path.toString()
                // Lanzamiento de una corutina en un contexto de IO
                scope.launch(Dispatchers.IO) {
                    try {
                        if(fileName.contains("Records")) {
                            val entriesToRead =
                                Utils.readCsvEntriesFile(context, uri)
                            for (entry in entriesToRead) {
                                entriesViewModel.addEntry(entry)
                            }
                            // Cambiamos al hilo principal para mostrar el SnackBar
                            withContext(Dispatchers.Main) {
                                SnackBarController.sendEvent(event = SnackBarEvent(messageImport))
                            }
                        }else{
                            withContext(Dispatchers.Main) {
                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        messageNoValidEntriesFile
                                    )
                                )
                            }
                        }
                    } catch (e: IOException) {
                        withContext(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    errorImport
                                )
                            )
                        }
                    }
                }
            }
        }
    }
    val pickerImportLauncherAccounts = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                // Lanzamiento de una corutina en un contexto de IO
                scope.launch(Dispatchers.IO) {
                    try {
                        val accountsToRead =
                            Utils.readCsvAccountsFile(context, uri)
                        for (account in accountsToRead) {
                            accountsViewModel.addAccount(account)
                        }
                        // Cambiamos al hilo principal para mostrar el SnackBar
                        withContext(Dispatchers.Main) {
                            SnackBarController.sendEvent(event = SnackBarEvent(messageImport))
                        }
                    } catch (e: IOException) {
                        withContext(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    errorImport
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 25.dp)
            .verticalScroll(
                rememberScrollState()
            )
    )
    {
        HeadSetting(
            title = stringResource(id = R.string.appsettings),
            MaterialTheme.typography.headlineSmall
        )
        SwitchComponent(
            title = stringResource(id = R.string.theme),
            description = stringResource(id = R.string.destheme),
            switchDarkTheme,
            onClickSwitch = { settingViewModel.onSwitchDarkThemeClicked(it) }
        )
        SwitchComponent(
            title = stringResource(id = R.string.enableonboarding),
            description = stringResource(id = R.string.desenableonboarding),
            switchTutorial,
            onClickSwitch = { settingViewModel.onSwitchTutorialClicked(it) }
        )
        SwitchComponent(
            title = stringResource(id = R.string.enablenotifications),
            description = (if (permissionNotificationGranted) stringResource(id = R.string.desenablenotifications)
            else stringResource(id = R.string.permissiondeny)),
            isChecked = if (permissionNotificationGranted) switchNotifications
            else false,
            onClickSwitch = {
                settingViewModel.onSwitchNotificationsClicked(it)

            }
        )

        SpacerApp()

        HeadSetting(
            title = stringResource(id = R.string.backup),
            MaterialTheme.typography.headlineSmall
        )

        RowComponent(title = stringResource(id = R.string.createbackup),
            description = stringResource(id = R.string.desbackup),
            iconResource = R.drawable.backup,
            onClick = {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                pickerExportLauncher.launch(intent)
            })
        RowComponent(title = stringResource(id = R.string.loadbackup),
            description = stringResource(id = R.string.desload),
            iconResource = R.drawable.download,
            onClick = {
                if(accounts.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = "*/*"
                    pickerImportLauncherEntries.launch(intent)
                }else{
                    scope.launch(Dispatchers.Main) {
                        SnackBarController.sendEvent(event = SnackBarEvent(messageEntriesWithoutAccounts))
                    }
                }
            })
        RowComponent(title = stringResource(id = R.string.loadbackupaccount),
            description = stringResource(id = R.string.desloadaccount),
            iconResource = R.drawable.download,
            onClick = {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                pickerImportLauncherAccounts.launch(intent)

            })


        SpacerApp()

        HeadSetting(
            title = stringResource(id = R.string.accountsetting),
            MaterialTheme.typography.headlineSmall
        )

        RowComponent(title = stringResource(id = R.string.add_an_account),
            description = stringResource(id = R.string.desadd_an_account),
            iconResource = R.drawable.add,
            onClick = {
                navToCreateAccounts()
                accountsViewModel.onDisableCurrencySelector()
            })
        RowComponent(title = stringResource(id = R.string.edit_account),
            description = stringResource(id = R.string.desedit_account),
            iconResource = R.drawable.edit,
            onClick = {
                settingViewModel.onSelectAccountOption(false)
                mainViewModel.selectScreen(IconOptions.SETTING_ACCOUNTS)
            })
        RowComponent(title = stringResource(id = R.string.delete_an_account),
            description = stringResource(id = R.string.desdelete_an_account),
            iconResource = R.drawable.baseline_delete_24,
            onClick = {
                settingViewModel.onSelectAccountOption(true)
                mainViewModel.selectScreen(IconOptions.SETTING_ACCOUNTS)
            }
        )
        RowComponent(title = stringResource(id = R.string.deleteentry),
            description = stringResource(id = R.string.deleteentrydes),
            iconResource = R.drawable.ic_databasedelete,
            onClick = {
                mainViewModel.selectScreen(IconOptions.SEARCH_DELETE)
            }
        )
        RowComponent(title = stringResource(id = R.string.modifyentry),
            description = stringResource(id = R.string.modifyentrydes),
            iconResource = R.drawable.ic_databaseupdate,
            onClick = {
                mainViewModel.selectScreen(IconOptions.SEARCH_UPDATE)
            }
        )
        RowComponent(title = stringResource(id = R.string.changecurrency),
            description = stringResource(id = R.string.deschangecurrency),
            iconResource = R.drawable.exchange,
            onClick = { mainViewModel.selectScreen(IconOptions.CHANGE_CURRENCY) })
    }
}

@Composable
fun SpacerApp() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(LocalCustomColorsPalette.current.textColor.copy(alpha = 0.2f)) // Ajusta el valor alpha para la opacidad
            .height(1.dp) // Cambié a height para que la línea sea horizontal, ajusta si es necesario
    )
}

@Composable
fun toEntryCSV(entries: List<EntryDTO>): MutableList<EntryCSV> {

    val entriesCSV = mutableListOf<EntryCSV>()
    entries.forEach { entry ->
        entriesCSV.add(
            EntryCSV(
                entry.description,
                stringResource(id = entry.nameResource),
                entry.amount,
                entry.date,
                entry.name,
                entry.categoryId,
                entry.accountId,
            )
        )
    }
    return entriesCSV
}

@Composable
fun toAccountCSV(accounts: List<Account>): MutableList<AccountCSV> {

    val accountsCSV = mutableListOf<AccountCSV>()
    accounts.forEach { account ->
        accountsCSV.add(
            AccountCSV(
                account.name,
                account.balance,
                account.id
            )
        )
    }
    return accountsCSV
}