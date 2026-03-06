package carnerero.agustin.cuentaappandroid.presentation.ui.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelDialog

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
