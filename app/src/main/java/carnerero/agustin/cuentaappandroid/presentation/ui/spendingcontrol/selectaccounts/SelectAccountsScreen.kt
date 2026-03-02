package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectaccounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.components.DialogAccountsSpendingControl
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.components.DialogCategoriesSpendingControl
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.components.DialogSpendingControl
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectaccounts.components.AccountWithCheckbox
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectaccounts.model.SelectAccountsUiEvent

@Composable
fun SelectAccountsScreen(
    viewModel: SelectAccountsSpendingControlViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val accounts=state.accounts

    BoxWithConstraints(Modifier.fillMaxSize()) {

        val maxWidthDp = maxWidth * 0.85f

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeadSetting(
                title = stringResource(id = R.string.selectaccounts),
                MaterialTheme.typography.titleLarge
            )

            LazyColumn(
                modifier = Modifier
                    .width(maxWidthDp)
                    .weight(1f)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(16.dp)
            ) {
                items(accounts) { account ->
                    AccountWithCheckbox(
                        account,
                        state.currencyCode,
                        onCheckBoxChange = { checked ->
                            viewModel.onUserEvent(
                                SelectAccountsUiEvent.OnCheckedChange(
                                    account.id,
                                    checked
                                )
                            )
                            if (checked) {
                                viewModel.onUserEvent(
                                    SelectAccountsUiEvent.OnOpenDialog(account)
                                )
                            }
                        }
                    )

                }
            }
                // 🔥 Dialog fuera del LazyColumn
                if (state.dialogUiState.showDialog) {

                    val selectedAccount = state.accounts
                        .firstOrNull { it.id == state.dialogUiState.id }

                    selectedAccount?.let { account ->
                        DialogSpendingControl(
                            name = account.name,
                            dialogState = state.dialogUiState,
                            onUserEvent = viewModel::onUserEvent,
                            onConfirm = { SelectAccountsUiEvent.OnConfirm },
                            onClose = { SelectAccountsUiEvent.OnCloseDialog },
                            onSpendLimitChange = { SelectAccountsUiEvent.OnSpendLimitChange(it) },
                            onSelectDate = { field, date -> SelectAccountsUiEvent.OnSelectDate(field, date) },
                            onShowDatePicker = { field, visible -> SelectAccountsUiEvent.OnShowDatePicker(field, visible) }
                        )
                    }
                }
            }
        }
    }
