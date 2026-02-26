package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.components.DatePickerSearchRecords
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.model.DialogUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectcategories.model.SelectCategoriesUiEvent
import carnerero.agustin.cuentaappandroid.utils.dateFormatByLocale

@Composable
fun DialogSpendingControl(
    name: String,
    dialogState: DialogUiState,
    onUserEvent: (SelectCategoriesUiEvent) -> Unit
) {
    AlertDialog(
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

/*@Composable
fun DialogSpendingControl(
    name:String,
    dialogState: DialogUiState,
    categoryId:Int,
    onUserEvent:(SelectCategoriesUiEvent)->Unit
) {
    if (dialogState.showDialog) {
        AlertDialog(
            containerColor = colors.backgroundPrimary,
            onDismissRequest = { onUserEvent(SelectCategoriesUiEvent.OnCloseDialog) },
            title = {
                Text(
                    name,
                    color = colors.textHeadColor,
                    style=MaterialTheme.typography.titleSmall
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center, // Espacio entre DatePickers
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    TextFieldComponent(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(id = R.string.limitMax),
                        dialogState.spendLimit.toString(),
                        onTextChange = {onUserEvent(SelectCategoriesUiEvent.OnSpendLimitChange(it))},
                        BoardType.DECIMAL,
                        false
                    )
                    DatePickerSearchRecords(
                        Modifier.width(240.dp)
                        .padding(bottom = dimens.medium),
                        R.string.fromdate,
                        dialogState.showFromDatePicker,
                        dialogState.fromDate,
                        { date ->
                            onUserEvent(
                                SelectCategoriesUiEvent.OnSelectDate(DateField.FROM, date)
                            )
                        },
                        { visible ->
                            onUserEvent(
                                SelectCategoriesUiEvent.OnShowDatePicker(DateField.FROM, visible)
                            )
                        },
                        dateField = DateField.FROM
                    )
                    DatePickerSearchRecords(
                        Modifier.width(240.dp)
                            .padding(bottom = dimens.medium),
                        R.string.todate,
                        dialogState.showToDatePicker,
                        dialogState.toDate,
                        { date ->
                            onUserEvent(
                                SelectCategoriesUiEvent.OnSelectDate(DateField.TO, date)
                            )
                        },
                        { visible ->
                            onUserEvent(
                                SelectCategoriesUiEvent.OnShowDatePicker(DateField.TO, visible)
                            )
                        },
                        dateField = DateField.FROM
                    )
                }


            },
            confirmButton = {
                ModelButton(
                    text = stringResource(id = R.string.confirmButton),
                    MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(130.dp),
                    true,
                    onClickButton = {
                        onUserEvent(SelectCategoriesUiEvent.OnConfirm)
                    }
                )
            },
            dismissButton = {
                ModelButton(
                    text = stringResource(id = R.string.cancelButton),
                    MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(130.dp),
                    true,
                    onClickButton = {
                        onUserEvent(SelectCategoriesUiEvent.OnCloseDialog)
                    }
                )
            }
        )
    }
}*/
