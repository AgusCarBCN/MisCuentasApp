package carnerero.agustin.cuentaappandroid.presentation.ui.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.documentfile.provider.DocumentFile
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelDialog
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.AccountCSV
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.EntryCSV
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.utils.Utils
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Date
import kotlin.collections.forEach

@Composable
fun ExportLauncher(
                   entriesViewModel: EntriesViewModel,
                   accountsViewModel: AccountsViewModel,
                   settingViewModel: SettingViewModel) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showDialog by settingViewModel.showExportDialog.observeAsState(false)
    val entries by entriesViewModel.listOfEntriesDTO.collectAsState()
    val accounts by accountsViewModel.listOfAccounts.observeAsState(mutableListOf())
    val entriesCSV = toEntryCSV(entries)
    val accountsCSV = toAccountCSV(accounts)
    val date = Date().dateFormat()
    val fileRecordsName = "backupRecords$date"
    val fileAccountsName = "backupAccounts$date"
    val messageExport = stringResource(id = R.string.exportData)
    val errorExport = stringResource(id = R.string.errorexport)
    val messageNoEntries = stringResource(id = R.string.noentries)
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

                        } catch (_: IOException) {
                            withContext(Dispatchers.Main) {
                                SnackBarController.sendEvent(event = SnackBarEvent(errorExport))
                            }
                        }
                    }

                }
            }
        }
    }
    ModelDialog (
        R.string.createbackup,
        R.string.desbackup,
        showDialog =showDialog,
        onConfirm = {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            pickerExportLauncher.launch(intent)
            settingViewModel.onChangeShowExportDialog(false)
        },
        onDismiss = {
            settingViewModel.onChangeShowExportDialog(false)
        })
}
@Composable
private fun toEntryCSV(entries: List<EntryDTO>): MutableList<EntryCSV> {

    val entriesCSV = mutableListOf<EntryCSV>()
    entries.forEach { entry ->
        entriesCSV.add(
            EntryCSV(
                entry.description,
                stringResource(id = entry.nameResource),
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

@Composable
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