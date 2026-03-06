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
fun ImportEntriesLauncher(
    showDialog: Boolean,
    onUriCreated: (Uri) -> Unit,
    onShowDialogChange:(Boolean)->Unit
) {

    val pickerImportLauncherEntries = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {

            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
            onUriCreated(uri)
            }
        }
    }


    ModelDialog (
        R.string.loadbackup,
        R.string.desload,
        showDialog =showDialog,
        onConfirm = {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                pickerImportLauncherEntries.launch(intent)
                onShowDialogChange(false)
        },
        onDismiss = {
           onShowDialogChange(false)
        })
}