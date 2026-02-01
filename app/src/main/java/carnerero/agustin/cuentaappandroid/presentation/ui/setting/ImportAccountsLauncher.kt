package carnerero.agustin.cuentaappandroid.presentation.ui.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelDialog
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@Composable

fun ImportAccountsLauncher(
                           accountsViewModel: AccountsViewModel,
                           settingViewModel: SettingViewModel){

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val messageImport = stringResource(id = R.string.loadbackup)
    val messageNoValidAccountsFile = stringResource(id = R.string.novalidaccountscsv)
    val errorImport = stringResource(id = R.string.errorimport)
    val showImportDialog by settingViewModel.showImportAccountsDialog.observeAsState(false)
    val accounts by accountsViewModel.listOfAccounts.observeAsState()

    val pickerImportLauncherAccounts = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                val fileName = uri.path.toString()
                // Lanzamiento de una corutina en un contexto de IO
                scope.launch(Dispatchers.IO) {
                    try {
                        if (fileName.contains("Accounts")) {
                            val accountsToRead =
                                Utils.readCsvAccountsFile(context, uri)
                            for (account in accountsToRead) {
                                accountsViewModel.addAccount(account)
                            }
                            // Cambiamos al hilo principal para mostrar el SnackBar
                            withContext(Dispatchers.Main) {
                                SnackBarController.sendEvent(event = SnackBarEvent(messageImport))
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        messageNoValidAccountsFile
                                    )
                                )
                            }
                        }
                    } catch (_: IOException) {
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
    ModelDialog (
        R.string.loadbackupaccount,
        R.string.desloadaccount,
        showDialog =showImportDialog,
        onConfirm = {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            pickerImportLauncherAccounts.launch(intent)
            settingViewModel.onChangeShowImportAccountsDialog(false)
        },
        onDismiss = {
            settingViewModel.onChangeShowImportAccountsDialog(false)
        })
}