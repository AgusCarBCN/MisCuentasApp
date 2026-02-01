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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.RowComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.SwitchComponent
import carnerero.agustin.cuentaappandroid.presentation.common.model.RowComponentItem
import carnerero.agustin.cuentaappandroid.utils.navigateTopLevel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable

fun SettingScreen(
    settingViewModel: SettingViewModel ,
    mainViewModel: MainViewModel,
    accountsViewModel: AccountsViewModel,
    entriesViewModel: EntriesViewModel,
    navController: NavHostController
) {


    val configureAccountsItems = listOf(
        RowComponentItem(Routes.AddAccount, R.string.desadd_an_account),
        RowComponentItem(Routes.DeleteAccount, R.string.desdelete_an_account),
        RowComponentItem(Routes.ModifyAccount, R.string.desedit_account),
        RowComponentItem(Routes.DeleteRecords, R.string.deleteentrydes),
        RowComponentItem(Routes.ModifyRecords, R.string.modifyentrydes),
        RowComponentItem(Routes.ChangeCurrency, R.string.deschangecurrency)
    )
    LaunchedEffect(Unit) {
        entriesViewModel.getAllEntriesDTO()
    }

    val permissionNotificationGranted by mainViewModel.notificationPermissionGranted.collectAsState()
    val switchTutorial by settingViewModel.switchTutorial.observeAsState(true)
    val switchDarkTheme by settingViewModel.switchDarkTheme.observeAsState(false)
    val switchNotifications by settingViewModel.switchNotifications.observeAsState(false)
    stringResource(id = R.string.noaccounts)
    ExportLauncher(entriesViewModel,accountsViewModel,settingViewModel)
    ImportEntriesLauncher(accountsViewModel,entriesViewModel,settingViewModel)
    ImportAccountsLauncher(accountsViewModel,settingViewModel)
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
                switchDarkTheme,
                onClickSwitch = { settingViewModel.onSwitchDarkThemeClicked(it) }
            )
            SwitchComponent(
                fieldModifier,
                title = stringResource(id = R.string.enableonboarding),
                description = stringResource(id = R.string.desenableonboarding),
                switchTutorial,
                onClickSwitch = { settingViewModel.onSwitchTutorialClicked(it) }
            )
            SwitchComponent(
                fieldModifier,
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

            RowComponent(
                fieldModifier,
                title = stringResource(id = R.string.createbackup),
                description = stringResource(id = R.string.desbackup),
                iconResource = R.drawable.backup,
                onClick = {
                    settingViewModel.onChangeShowExportDialog(true)
                })
            RowComponent(
                fieldModifier,
                title = stringResource(id = R.string.loadbackup),
                description = stringResource(id = R.string.desload),
                iconResource = R.drawable.download,
                onClick = {
                    settingViewModel.onChangeShowImportEntriesDialog(true)
                })
            RowComponent(
                fieldModifier,
                title = stringResource(id = R.string.loadbackupaccount),
                description = stringResource(id = R.string.desloadaccount),
                iconResource = R.drawable.download,
                onClick = {
                    settingViewModel.onChangeShowImportAccountsDialog(true)
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
            .background(LocalCustomColorsPalette.current.textColor.copy(alpha = 0.2f)) // Ajusta el valor alpha para la opacidad
            .height(1.dp) // Cambié a height para que la línea sea horizontal, ajusta si es necesario
    )
}
