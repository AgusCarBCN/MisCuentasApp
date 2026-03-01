package carnerero.agustin.cuentaappandroid.presentation.ui.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelDialog
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.AccountCSV
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.EntryCSV
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.SettingsDialog
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.utils.Utils
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Date
import kotlin.collections.forEach

@Composable
fun ExportLauncher(
    showDialog: Boolean,
    onUriCreated: (Uri) -> Unit,
    onShowDialogChange:(Boolean)->Unit
                  )
{

    val pickerExportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                onUriCreated(uri)
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
            onShowDialogChange(false)
        },
        onDismiss = {
            onShowDialogChange(false)
        })
}
