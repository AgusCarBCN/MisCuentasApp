package carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.utils.Utils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerSearch(
    modifier: Modifier,
    label: Int,
    searchViewModel: SearchViewModel,
    isDateFrom:Boolean
) {

    val showDatePicker by if(isDateFrom) searchViewModel.showDatePickerFrom.observeAsState(false)
    else searchViewModel.showDatePickerTo.observeAsState(false)
    // Observa la fecha seleccionada según si es "From" o "To"
    val selectedDate = if (isDateFrom) {
        searchViewModel.selectedFromDate.observeAsState("")
    } else {
        searchViewModel.selectedToDate.observeAsState("")
    }.value
    //Mensaje de error de fechas
    stringResource(id = R.string.datefromoverdateto)
    val dateFromText= "${stringResource(id = R.string.fromdate)} $selectedDate"
    val dateToText= "${stringResource(id = R.string.todate)} $selectedDate"


// Estado del DatePicker
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = null) // Reiniciamos el estado para cada nueva apertura
    // Actualiza la fecha en el ViewModel al cambiar la selección en DatePicker
    datePickerState.selectedDateMillis?.let { selectedMillis ->
        val dateString = Utils.convertMillisToDate(selectedMillis)
        searchViewModel.onSelectedDate(dateString, isDateFrom)
    }

    Box(modifier = modifier) {
        TextField(
            value = selectedDate,
            onValueChange = { searchViewModel.onSelectedDate(selectedDate, isDateFrom) },
            label = { Text(stringResource(label)) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { searchViewModel.onShowDatePicker(true, isDateFrom) }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = (if(isDateFrom) dateFromText else dateToText)
                    )
                }
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = colors.textColor,
                focusedIndicatorColor = Color.Transparent,  // Sin borde cuando está enfocado
                unfocusedIndicatorColor = Color.Transparent,  // Sin borde cuando no está enfocado
                focusedContainerColor = colors.focusedContainerTextField,
                unfocusedContainerColor = colors.unfocusedContainerTextField,
                unfocusedTextColor = colors.textColor,
                focusedLabelColor = colors.textColor,
                unfocusedLabelColor = colors.textColor,
                disabledLabelColor = colors.textColor,
                disabledTextColor = colors.textColor,
                unfocusedTrailingIconColor = colors.textColor,
                focusedTrailingIconColor = colors.textColor
            )
        )

        if (showDatePicker) {

            DatePickerDialog(
                onDismissRequest = { searchViewModel.onShowDatePicker(false, isDateFrom) },
                colors = DatePickerColors(
                    containerColor = colors.drawerColor,
                    titleContentColor = colors.textHeadColor,
                    headlineContentColor = colors.textHeadColor,
                    weekdayContentColor = colors.textHeadColor,
                    subheadContentColor = colors.textColor,
                    navigationContentColor = colors.textHeadColor,
                    yearContentColor = colors.textHeadColor,
                    disabledYearContentColor = colors.disableButton,
                    dayContentColor = colors.textColor,
                    currentYearContentColor = colors.textHeadColor,
                    selectedYearContentColor = colors.textHeadColor,
                    disabledSelectedYearContentColor = colors.disableButton,
                    disabledSelectedYearContainerColor = colors.disableButton,
                    selectedYearContainerColor = colors.focusedContainerTextField,
                    selectedDayContainerColor = colors.textColor,
                    disabledSelectedDayContainerColor = colors.disableButton,
                    selectedDayContentColor = colors.textButtonColorPressed,
                    disabledSelectedDayContentColor = colors.disableButton,
                    disabledDayContentColor = colors.disableButton,
                    todayContentColor = colors.textHeadColor,
                    todayDateBorderColor = colors.disableButton,
                    dayInSelectionRangeContainerColor = colors.buttonColorPressed,
                    dayInSelectionRangeContentColor = colors.textButtonColorPressed,
                    dividerColor = colors.disableButton,
                    dateTextFieldColors = TextFieldDefaults.colors(
                        focusedTextColor = colors.textColor,
                        focusedIndicatorColor = Color.Transparent,  // Sin borde cuando está enfocado
                        unfocusedIndicatorColor = Color.Transparent,  // Sin borde cuando no está enfocado
                        focusedContainerColor = colors.focusedContainerTextField,
                        unfocusedContainerColor = colors.unfocusedContainerTextField,
                        unfocusedTextColor = colors.textColor,
                        focusedLabelColor = colors.textColor,
                        unfocusedLabelColor = colors.textColor,
                        disabledLabelColor = colors.textColor,
                        disabledTextColor = colors.textColor,
                        unfocusedTrailingIconColor = colors.textColor,
                        focusedTrailingIconColor = colors.textColor
                    )
                ),
                confirmButton = {
                    TextButton(
                        colors = ButtonColors(
                            containerColor = colors.drawerColor,
                            contentColor = colors.textColor,
                            disabledContainerColor = colors.disableButton,
                            disabledContentColor = colors.disableButton,

                        ),
                        onClick = {
                        datePickerState.selectedDateMillis?.let { selectedMillis ->
                            val dateString = Utils.convertMillisToDate(selectedMillis)
                            searchViewModel.onSelectedDate(dateString, isDateFrom)
                        }
                        searchViewModel.onShowDatePicker(false, isDateFrom)
                    }) {
                        Text(
                            stringResource(id = R.string.confirmButton),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        colors = ButtonColors(
                            containerColor = colors.drawerColor,
                            contentColor = colors.textColor,
                            disabledContainerColor = colors.disableButton,
                            disabledContentColor = colors.disableButton,

                            ),
                        onClick = {
                        searchViewModel.onShowDatePicker(
                            false,
                            isDateFrom
                        )
                    }) {
                        Text(stringResource(id = R.string.cancelButton),
                            style = MaterialTheme.typography.labelMedium
                                   )
                    }
                }
            ) {

                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                        colors = DatePickerColors(
                            containerColor = colors.drawerColor,
                            titleContentColor = colors.textHeadColor,
                            headlineContentColor = colors.textHeadColor,
                            weekdayContentColor = colors.textHeadColor,
                            subheadContentColor = colors.textColor,
                            navigationContentColor = colors.textHeadColor,
                            yearContentColor = colors.textHeadColor,
                            disabledYearContentColor = colors.disableButton,
                            dayContentColor = colors.textColor,
                            currentYearContentColor = colors.textHeadColor,
                            selectedYearContentColor = colors.textHeadColor,
                            disabledSelectedYearContentColor = colors.disableButton,
                            disabledSelectedYearContainerColor = colors.disableButton,
                            selectedYearContainerColor = colors.focusedContainerTextField,
                            selectedDayContainerColor = colors.textColor,
                            disabledSelectedDayContainerColor = colors.disableButton,
                            selectedDayContentColor = colors.textButtonColorPressed,
                            disabledSelectedDayContentColor = colors.disableButton,
                            disabledDayContentColor = colors.disableButton,
                            todayContentColor = colors.textHeadColor,
                            todayDateBorderColor = colors.disableButton,
                            dayInSelectionRangeContainerColor = colors.buttonColorPressed,
                            dayInSelectionRangeContentColor = colors.textButtonColorPressed,
                            dividerColor = colors.disableButton,
                            dateTextFieldColors = TextFieldDefaults.colors(
                                focusedTextColor = colors.textColor,
                                focusedIndicatorColor = Color.Transparent,  // Sin borde cuando está enfocado
                                unfocusedIndicatorColor = Color.Transparent,  // Sin borde cuando no está enfocado
                                focusedContainerColor = colors.focusedContainerTextField,
                                unfocusedContainerColor = colors.unfocusedContainerTextField,
                                unfocusedTextColor = colors.textColor,
                                focusedLabelColor = colors.textColor,
                                unfocusedLabelColor = colors.textColor,
                                disabledLabelColor = colors.textColor,
                                disabledTextColor = colors.textColor,
                                unfocusedTrailingIconColor = colors.textColor,
                                focusedTrailingIconColor = colors.textColor
                            )
                        )
                    )

                }
            }
        }
    }








