package carnerero.agustin.cuentaappandroid.presentation.ui.setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.RowComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.SwitchComponent
import carnerero.agustin.cuentaappandroid.presentation.common.model.RowComponentItem
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.model.SettingsDialog
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.utils.navigateTopLevel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable

fun SettingScreen(
    settingViewModel: SettingViewModel ,
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    val state by settingViewModel.uiState.collectAsStateWithLifecycle()
    val messageExport = stringResource(id = R.string.exportData)
    val errorExport = stringResource(id = R.string.errorexport)
    val messageNoRecords = stringResource(id = R.string.noentries)
    val messageImport = stringResource(id = R.string.loadbackup)
    val messageNoValidAccountsFile = stringResource(id = R.string.novalidaccountscsv)
    val errorImport = stringResource(id = R.string.errorimport)
    val messageNoValidRecordsFile = stringResource(id = R.string.novalidrecordcsv)

    val configureAccountsItems = listOf(
        RowComponentItem(Routes.AddAccount, R.string.desadd_an_account),
        RowComponentItem(Routes.DeleteAccount, R.string.desdelete_an_account),
        RowComponentItem(Routes.ModifyAccount, R.string.desedit_account),
        RowComponentItem(Routes.DeleteRecords, R.string.deleteentrydes),
        RowComponentItem(Routes.ModifyRecords, R.string.modifyentrydes),

    )
    LaunchedEffect(Unit) {
        settingViewModel.effect.collect { effect ->
            when (effect) {
                SettingEffects.ErrorExport -> SnackBarController.sendEvent(SnackBarEvent(errorExport))
                SettingEffects.ErrorImport -> SnackBarController.sendEvent(SnackBarEvent(errorImport))
                SettingEffects.MessageExport -> SnackBarController.sendEvent(SnackBarEvent(messageExport))
                SettingEffects.MessageImport -> SnackBarController.sendEvent(SnackBarEvent(messageImport))
                SettingEffects.MessageNoRecords -> SnackBarController.sendEvent(SnackBarEvent(messageNoRecords))
                SettingEffects.MessageNoValidAccountFile -> SnackBarController.sendEvent(SnackBarEvent(messageNoValidAccountsFile))
                SettingEffects.MessageNoValidRecordFile -> SnackBarController.sendEvent(SnackBarEvent(messageNoValidRecordsFile))
            }
        }
    }

    val permissionNotificationGranted by mainViewModel.notificationPermissionGranted.collectAsState()

    stringResource(id = R.string.noaccounts)
    ExportLauncher(state.showExportDialog,
        { uri ->
            settingViewModel.onEvenUser(
                SettingsUserEvents.OnConfirmExport(uri)
            )
        },
        {visible ->
            settingViewModel.onEvenUser(
                SettingsUserEvents.OnShowLaunchersDialog(SettingsDialog.EXPORT,visible)
            )
        })
    ImportEntriesLauncher(
        state.showImportRecordsDialog,
        { uri ->
            settingViewModel.onEvenUser(
                SettingsUserEvents.OnConfirmRecordsImport(uri)
            )
        },
        {visible ->
            settingViewModel.onEvenUser(
                SettingsUserEvents.OnShowLaunchersDialog(SettingsDialog.IMPORT_ENTRIES,visible)
            )
        }
    )
    ImportAccountsLauncher(
        state.showImportAccountDialog,
        { uri ->
            settingViewModel.onEvenUser(
                SettingsUserEvents.OnConfirmAccountImport(uri)
            )
        },
        {visible ->
            settingViewModel.onEvenUser(
                SettingsUserEvents.OnShowLaunchersDialog(SettingsDialog.IMPORT_ACCOUNT,visible)
            )
        }
    )
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxWidthDp = maxWidth

        val fieldModifier = Modifier
            .width(maxWidthDp * 0.85f) // mismo ancho para TODOS
            .heightIn(min = 48.dp)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 25.dp)
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            HeadSetting(
                title = stringResource(id = R.string.appsettings),
                MaterialTheme.typography.headlineSmall
            )
            SwitchComponent(
                fieldModifier,
                title = stringResource(id = R.string.theme),
                description = stringResource(id = R.string.destheme),
                state.switchDarkTheme,
                onClickSwitch = { settingViewModel.onEvenUser(SettingsUserEvents.OnSwitchDarkThemeChange(it))}
            )
            SwitchComponent(
                fieldModifier,
                title = stringResource(id = R.string.enableonboarding),
                description = stringResource(id = R.string.desenableonboarding),
                state.switchTutorial,
                onClickSwitch = { settingViewModel.onEvenUser(SettingsUserEvents.OnSwitchShowTutorialChange(it))}
            )
            SwitchComponent(
                fieldModifier,
                title = stringResource(id = R.string.enablenotifications),
                description = (if (permissionNotificationGranted) stringResource(id = R.string.desenablenotifications)
                else stringResource(id = R.string.permissiondeny)),
                isChecked = if (permissionNotificationGranted) state.switchNotifications
                else false,
                onClickSwitch = {
                    settingViewModel.onEvenUser(SettingsUserEvents.OnSwitchShowNotificationsChange(it))
                }
            )

            SpacerApp()

            HeadSetting(
                title = stringResource(id = R.string.backup),
                MaterialTheme.typography.headlineSmall
            )

            RowComponent(
                fieldModifier,
                title = stringResource(id = R.string.createbackup),
                description = stringResource(id = R.string.desbackup),
                iconResource = R.drawable.backup,
                onClick = {
                    settingViewModel.onEvenUser(SettingsUserEvents.OnShowLaunchersDialog(
                        SettingsDialog.EXPORT,true))
                })
            RowComponent(
                fieldModifier,
                title = stringResource(id = R.string.loadbackup),
                description = stringResource(id = R.string.desload),
                iconResource = R.drawable.download,
                onClick = {
                    settingViewModel.onEvenUser(SettingsUserEvents.OnShowLaunchersDialog(
                        SettingsDialog.IMPORT_ACCOUNT,true))
                })
            RowComponent(
                fieldModifier,
                title = stringResource(id = R.string.loadbackupaccount),
                description = stringResource(id = R.string.desloadaccount),
                iconResource = R.drawable.download,
                onClick = {
                    settingViewModel.onEvenUser(SettingsUserEvents.OnShowLaunchersDialog(
                        SettingsDialog.IMPORT_ENTRIES,true))
                })
            SpacerApp()
            HeadSetting(
                title = stringResource(id = R.string.accountsetting),
                MaterialTheme.typography.headlineSmall
            )
            configureAccountsItems.forEach { item ->
                RowComponent(
                    fieldModifier,
                    title = stringResource(id = item.itemRoute.labelResource!!),
                    description = stringResource(id = item.description),
                    iconResource = item.itemRoute.iconResource!!,
                    onClick = {
                        mainViewModel.updateTitle(item.itemRoute.labelResource)
                        navController.navigateTopLevel(item.itemRoute.route)
                    })
            }
        }
    }
}
@Composable
fun SpacerApp() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(colors.textColor.copy(alpha = 0.2f)) // Ajusta el valor alpha para la opacidad
            .height(1.dp) // Cambié a height para que la línea sea horizontal, ajusta si es necesario
    )
}
