package carnerero.agustin.cuentaappandroid.components

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
import androidx.compose.ui.unit.sp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.search.SearchViewModel
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
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
    val messageDateError= stringResource(id = R.string.datefromoverdateto)


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
                        contentDescription = "Select date"
                    )
                }
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = LocalCustomColorsPalette.current.textColor,
                focusedIndicatorColor = Color.Transparent,  // Sin borde cuando está enfocado
                unfocusedIndicatorColor = Color.Transparent,  // Sin borde cuando no está enfocado
                focusedContainerColor = LocalCustomColorsPalette.current.focusedContainerTextField,
                unfocusedContainerColor = LocalCustomColorsPalette.current.unfocusedContainerTextField,
                unfocusedTextColor = LocalCustomColorsPalette.current.textColor,
                focusedLabelColor = LocalCustomColorsPalette.current.textColor,
                unfocusedLabelColor = LocalCustomColorsPalette.current.textColor,
                disabledLabelColor = LocalCustomColorsPalette.current.textColor,
                disabledTextColor = LocalCustomColorsPalette.current.textColor,
                unfocusedTrailingIconColor = LocalCustomColorsPalette.current.textColor,
                focusedTrailingIconColor = LocalCustomColorsPalette.current.textColor
            )
        )

        if (showDatePicker) {

            DatePickerDialog(
                onDismissRequest = { searchViewModel.onShowDatePicker(false, isDateFrom) },
                colors = DatePickerColors(
                    containerColor = LocalCustomColorsPalette.current.drawerColor,
                    titleContentColor = LocalCustomColorsPalette.current.textHeadColor,
                    headlineContentColor = LocalCustomColorsPalette.current.textHeadColor,
                    weekdayContentColor = LocalCustomColorsPalette.current.textHeadColor,
                    subheadContentColor = LocalCustomColorsPalette.current.textColor,
                    navigationContentColor = LocalCustomColorsPalette.current.textHeadColor,
                    yearContentColor = LocalCustomColorsPalette.current.textHeadColor,
                    disabledYearContentColor = LocalCustomColorsPalette.current.disableButton,
                    dayContentColor = LocalCustomColorsPalette.current.textColor,
                    currentYearContentColor = LocalCustomColorsPalette.current.textHeadColor,
                    selectedYearContentColor = LocalCustomColorsPalette.current.textHeadColor,
                    disabledSelectedYearContentColor = LocalCustomColorsPalette.current.disableButton,
                    disabledSelectedYearContainerColor = LocalCustomColorsPalette.current.disableButton,
                    selectedYearContainerColor = LocalCustomColorsPalette.current.focusedContainerTextField,
                    selectedDayContainerColor = LocalCustomColorsPalette.current.textColor,
                    disabledSelectedDayContainerColor = LocalCustomColorsPalette.current.disableButton,
                    selectedDayContentColor = LocalCustomColorsPalette.current.textButtonColorPressed,
                    disabledSelectedDayContentColor = LocalCustomColorsPalette.current.disableButton,
                    disabledDayContentColor = LocalCustomColorsPalette.current.disableButton,
                    todayContentColor = LocalCustomColorsPalette.current.textHeadColor,
                    todayDateBorderColor = LocalCustomColorsPalette.current.disableButton,
                    dayInSelectionRangeContainerColor = LocalCustomColorsPalette.current.buttonColorPressed,
                    dayInSelectionRangeContentColor = LocalCustomColorsPalette.current.textButtonColorPressed,
                    dividerColor = LocalCustomColorsPalette.current.disableButton,
                    dateTextFieldColors = TextFieldDefaults.colors(
                        focusedTextColor = LocalCustomColorsPalette.current.textColor,
                        focusedIndicatorColor = Color.Transparent,  // Sin borde cuando está enfocado
                        unfocusedIndicatorColor = Color.Transparent,  // Sin borde cuando no está enfocado
                        focusedContainerColor = LocalCustomColorsPalette.current.focusedContainerTextField,
                        unfocusedContainerColor = LocalCustomColorsPalette.current.unfocusedContainerTextField,
                        unfocusedTextColor = LocalCustomColorsPalette.current.textColor,
                        focusedLabelColor = LocalCustomColorsPalette.current.textColor,
                        unfocusedLabelColor = LocalCustomColorsPalette.current.textColor,
                        disabledLabelColor = LocalCustomColorsPalette.current.textColor,
                        disabledTextColor = LocalCustomColorsPalette.current.textColor,
                        unfocusedTrailingIconColor = LocalCustomColorsPalette.current.textColor,
                        focusedTrailingIconColor = LocalCustomColorsPalette.current.textColor
                    )
                ),
                confirmButton = {
                    TextButton(
                        colors = ButtonColors(
                            containerColor = LocalCustomColorsPalette.current.drawerColor,
                            contentColor = LocalCustomColorsPalette.current.textColor,
                            disabledContainerColor = LocalCustomColorsPalette.current.disableButton,
                            disabledContentColor = LocalCustomColorsPalette.current.disableButton,

                        ),
                        onClick = {
                        datePickerState.selectedDateMillis?.let { selectedMillis ->
                            val dateString = Utils.convertMillisToDate(selectedMillis)
                            searchViewModel.onSelectedDate(dateString, isDateFrom)
                        }
                        searchViewModel.onShowDatePicker(false, isDateFrom)
                    }) {
                        Text(stringResource(id = R.string.confirmButton),
                            fontSize = 16.sp
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        colors = ButtonColors(
                            containerColor = LocalCustomColorsPalette.current.drawerColor,
                            contentColor = LocalCustomColorsPalette.current.textColor,
                            disabledContainerColor = LocalCustomColorsPalette.current.disableButton,
                            disabledContentColor = LocalCustomColorsPalette.current.disableButton,

                            ),
                        onClick = {
                        searchViewModel.onShowDatePicker(
                            false,
                            isDateFrom
                        )
                    }) {
                        Text(stringResource(id = R.string.cancelButton),
                            fontSize = 16.sp)
                    }
                }
            ) {

                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                        colors = DatePickerColors(
                            containerColor = LocalCustomColorsPalette.current.drawerColor,
                            titleContentColor = LocalCustomColorsPalette.current.textHeadColor,
                            headlineContentColor = LocalCustomColorsPalette.current.textHeadColor,
                            weekdayContentColor = LocalCustomColorsPalette.current.textHeadColor,
                            subheadContentColor = LocalCustomColorsPalette.current.textColor,
                            navigationContentColor = LocalCustomColorsPalette.current.textHeadColor,
                            yearContentColor = LocalCustomColorsPalette.current.textHeadColor,
                            disabledYearContentColor = LocalCustomColorsPalette.current.disableButton,
                            dayContentColor = LocalCustomColorsPalette.current.textColor,
                            currentYearContentColor = LocalCustomColorsPalette.current.textHeadColor,
                            selectedYearContentColor = LocalCustomColorsPalette.current.textHeadColor,
                            disabledSelectedYearContentColor = LocalCustomColorsPalette.current.disableButton,
                            disabledSelectedYearContainerColor = LocalCustomColorsPalette.current.disableButton,
                            selectedYearContainerColor = LocalCustomColorsPalette.current.focusedContainerTextField,
                            selectedDayContainerColor = LocalCustomColorsPalette.current.textColor,
                            disabledSelectedDayContainerColor = LocalCustomColorsPalette.current.disableButton,
                            selectedDayContentColor = LocalCustomColorsPalette.current.textButtonColorPressed,
                            disabledSelectedDayContentColor = LocalCustomColorsPalette.current.disableButton,
                            disabledDayContentColor = LocalCustomColorsPalette.current.disableButton,
                            todayContentColor = LocalCustomColorsPalette.current.textHeadColor,
                            todayDateBorderColor = LocalCustomColorsPalette.current.disableButton,
                            dayInSelectionRangeContainerColor = LocalCustomColorsPalette.current.buttonColorPressed,
                            dayInSelectionRangeContentColor = LocalCustomColorsPalette.current.textButtonColorPressed,
                            dividerColor = LocalCustomColorsPalette.current.disableButton,
                            dateTextFieldColors = TextFieldDefaults.colors(
                                focusedTextColor = LocalCustomColorsPalette.current.textColor,
                                focusedIndicatorColor = Color.Transparent,  // Sin borde cuando está enfocado
                                unfocusedIndicatorColor = Color.Transparent,  // Sin borde cuando no está enfocado
                                focusedContainerColor = LocalCustomColorsPalette.current.focusedContainerTextField,
                                unfocusedContainerColor = LocalCustomColorsPalette.current.unfocusedContainerTextField,
                                unfocusedTextColor = LocalCustomColorsPalette.current.textColor,
                                focusedLabelColor = LocalCustomColorsPalette.current.textColor,
                                unfocusedLabelColor = LocalCustomColorsPalette.current.textColor,
                                disabledLabelColor = LocalCustomColorsPalette.current.textColor,
                                disabledTextColor = LocalCustomColorsPalette.current.textColor,
                                unfocusedTrailingIconColor = LocalCustomColorsPalette.current.textColor,
                                focusedTrailingIconColor = LocalCustomColorsPalette.current.textColor
                            )
                        )
                    )

                }
            }
        }
    }








