package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.components.DatePickerSearchRecords
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.model.DialogUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectaccounts.model.SelectAccountsUiEvent
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectcategories.model.SelectCategoriesUiEvent

@Composable
fun DialogCategoriesSpendingControl(
    name: String,
    dialogState: DialogUiState,
    onUserEvent: (SelectCategoriesUiEvent) -> Unit
) {
    AlertDialog(
        containerColor = colors.backgroundPrimary,
        onDismissRequest = {
            onUserEvent(SelectCategoriesUiEvent.OnCloseDialog)
        },
        title = {
            Text(name)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextFieldComponent(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(id = R.string.limitMax),
                    inputText = dialogState.spendLimit,
                    onTextChange = {
                        onUserEvent(
                            SelectCategoriesUiEvent.OnSpendLimitChange(it)
                        )
                    },
                    type = BoardType.DECIMAL,
                    textInvisible = false
                )
                DatePickerSearchRecords(
                    modifier = Modifier.width(240.dp),
                    labelResource = R.string.fromdate,
                    showDatePicker = dialogState.showFromDatePicker,
                    selectedDate = dialogState.fromDate,
                    onSelectedDate = { date ->
                        onUserEvent(
                            SelectCategoriesUiEvent.OnSelectDate(
                                DateField.FROM,
                                date
                            )
                        )
                    },
                    onShowDatePicker = { visible ->
                        onUserEvent(
                            SelectCategoriesUiEvent.OnShowDatePicker(
                                DateField.FROM,
                                visible
                            )
                        )
                    },
                    dateField = DateField.FROM
                )

                DatePickerSearchRecords(
                    modifier = Modifier.width(240.dp),
                    labelResource = R.string.todate,
                    showDatePicker = dialogState.showToDatePicker,
                    selectedDate = dialogState.toDate,
                    onSelectedDate = { date ->
                        onUserEvent(
                            SelectCategoriesUiEvent.OnSelectDate(
                                DateField.TO,
                                date
                            )
                        )
                    },
                    onShowDatePicker = { visible ->
                        onUserEvent(
                            SelectCategoriesUiEvent.OnShowDatePicker(
                                DateField.TO,
                                visible
                            )
                        )
                    },
                    dateField = DateField.TO
                )
            }
        },
        confirmButton = {
            ModelButton(
                text = stringResource(id = R.string.confirmButton),
                textStyle = MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(130.dp),
                enabledButton = true,
                onClickButton = {
                    onUserEvent(SelectCategoriesUiEvent.OnConfirm)
                }
            )
        },
        dismissButton = {
            ModelButton(
                text = stringResource(id = R.string.cancelButton),
                textStyle =  MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(130.dp),
                enabledButton = true,
                onClickButton = {
                    onUserEvent(SelectCategoriesUiEvent.OnCloseDialog)
                }
            )
        }
    )
}
@Composable
fun DialogAccountsSpendingControl(
    name: String,
    dialogState: DialogUiState,
    onUserEvent: (SelectAccountsUiEvent) -> Unit
) {
    AlertDialog(
        containerColor = colors.backgroundPrimary,
        onDismissRequest = {
            onUserEvent(SelectAccountsUiEvent.OnCloseDialog)
        },
        title = {
            Text(name)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextFieldComponent(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(id = R.string.limitMax),
                    inputText = dialogState.spendLimit,
                    onTextChange = {
                        onUserEvent(
                            SelectAccountsUiEvent.OnSpendLimitChange(it)
                        )
                    },
                    type = BoardType.DECIMAL,
                    textInvisible = false
                )
                DatePickerSearchRecords(
                    modifier = Modifier.width(240.dp),
                    labelResource = R.string.fromdate,
                    showDatePicker = dialogState.showFromDatePicker,
                    selectedDate = dialogState.fromDate,
                    onSelectedDate = { date ->
                        onUserEvent(
                            SelectAccountsUiEvent.OnSelectDate(
                                DateField.FROM,
                                date
                            )
                        )
                    },
                    onShowDatePicker = { visible ->
                        onUserEvent(
                            SelectAccountsUiEvent.OnShowDatePicker(
                                DateField.FROM,
                                visible
                            )
                        )
                    },
                    dateField = DateField.FROM
                )

                DatePickerSearchRecords(
                    modifier = Modifier.width(240.dp),
                    labelResource = R.string.todate,
                    showDatePicker = dialogState.showToDatePicker,
                    selectedDate = dialogState.toDate,
                    onSelectedDate = { date ->
                        onUserEvent(
                            SelectAccountsUiEvent.OnSelectDate(
                                DateField.TO,
                                date
                            )
                        )
                    },
                    onShowDatePicker = { visible ->
                        onUserEvent(
                            SelectAccountsUiEvent.OnShowDatePicker(
                                DateField.TO,
                                visible
                            )
                        )
                    },
                    dateField = DateField.TO
                )
            }
        },
        confirmButton = {
            ModelButton(
                text = stringResource(id = R.string.confirmButton),
                textStyle = MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(130.dp),
                enabledButton = true,
                onClickButton = {
                    onUserEvent(SelectAccountsUiEvent.OnConfirm)
                }
            )
        },
        dismissButton = {
            ModelButton(
                text = stringResource(id = R.string.cancelButton),
                textStyle =  MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(130.dp),
                enabledButton = true,
                onClickButton = {
                    onUserEvent(SelectAccountsUiEvent.OnCloseDialog)
                }
            )
        }
    )
}
@Composable
fun <T> DialogSpendingControl(
    name: String,
    dialogState: DialogUiState,
    onUserEvent: (T) -> Unit,
    onConfirm: () -> T,
    onClose: () -> T,
    onSpendLimitChange: (String) -> T,
    onSelectDate: (DateField, String) -> T,
    onShowDatePicker: (DateField, Boolean) -> T
) {
    AlertDialog(
        containerColor = colors.backgroundPrimary,
        onDismissRequest = { onUserEvent(onClose()) },
        title = { Text(name) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextFieldComponent(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(id = R.string.limitMax),
                    inputText = dialogState.spendLimit,
                    onTextChange = { onUserEvent(onSpendLimitChange(it)) },
                    type = BoardType.DECIMAL,
                    textInvisible = false
                )

                DatePickerSearchRecords(
                    modifier = Modifier.width(240.dp),
                    labelResource = R.string.fromdate,
                    showDatePicker = dialogState.showFromDatePicker,
                    selectedDate = dialogState.fromDate,
                    onSelectedDate = { date -> onUserEvent(onSelectDate(DateField.FROM, date)) },
                    onShowDatePicker = { visible -> onUserEvent(onShowDatePicker(DateField.FROM, visible)) },
                    dateField = DateField.FROM
                )

                DatePickerSearchRecords(
                    modifier = Modifier.width(240.dp),
                    labelResource = R.string.todate,
                    showDatePicker = dialogState.showToDatePicker,
                    selectedDate = dialogState.toDate,
                    onSelectedDate = { date -> onUserEvent(onSelectDate(DateField.TO, date)) },
                    onShowDatePicker = { visible -> onUserEvent(onShowDatePicker(DateField.TO, visible)) },
                    dateField = DateField.TO
                )
            }
        },
        confirmButton = {
            ModelButton(
                text = stringResource(id = R.string.confirmButton),
                textStyle = MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(130.dp),
                enabledButton = true,
                onClickButton = { onUserEvent(onConfirm()) }
            )
        },
        dismissButton = {
            ModelButton(
                text = stringResource(id = R.string.cancelButton),
                textStyle = MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(130.dp),
                enabledButton = true,
                onClickButton = { onUserEvent(onClose()) }
            )
        }
    )
}

