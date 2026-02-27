package carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.components.AccountSearchRecordsSelector
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.components.DatePickerSearchRecords
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.components.RadioButtonRecordsSearch
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.utils.navigateTopLevel
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp


@Composable
fun RecordSearchScreen(
    searchRecordsViewModel: SearchRecordsViewModel,
    navController: NavController
) {
    val isPortrait = orientation == OrientationApp.Portrait
    val state by searchRecordsViewModel.uiState.collectAsStateWithLifecycle()

    val searchFilter = state.searchFilter
    val enableSearchButton = state.enableSearchButton
    val messageAmountError = stringResource(R.string.amountfromoverdateto)
    val messageDateError = stringResource(R.string.datefromoverdateto)
    LaunchedEffect(Unit) {
        searchRecordsViewModel.effect.collect { effect ->
            when (effect) {
                SearchEffects.NavToRecordsScreen -> {
                    navController.navigateTopLevel(state.route)
                    Log.d("NAVIGATION", "Route from Search:${state.route}")
                }
                SearchEffects.MessageInvalidAmounts ->
                    SnackBarController.sendEvent(SnackBarEvent(messageAmountError))
                SearchEffects.MessageInvalidDates ->
                    SnackBarController.sendEvent(SnackBarEvent(messageDateError))
                else -> Unit
            }
        }
    }

    if (isPortrait)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.backgroundPrimary)
        ) {
            val fieldModifier = Modifier
                .fillMaxWidth(0.85f)
                .heightIn(min = 48.dp)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextFieldComponent(
                    modifier = fieldModifier,
                    stringResource(R.string.searchentries),
                    searchFilter.description ?: "",
                    onTextChange = {
                        searchRecordsViewModel.onEvent(
                            SearchUiEvent.OnRecordDescriptionChange(
                                it
                            )
                        )
                    },
                    BoardType.TEXT,
                    false
                )

                HeadSetting(
                    title = stringResource(R.string.daterange),
                    MaterialTheme.typography.headlineSmall
                )

                Row(
                    modifier = fieldModifier,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DatePickerSearchRecords(
                        modifier = Modifier.weight(1f),
                        R.string.fromdate,
                        state.showDatePickerFrom,
                        state.searchFilter.dateFrom ,
                        { date ->
                            searchRecordsViewModel.onEvent(
                                SearchUiEvent.OnSelectDate(DateField.FROM, date)
                            )
                        },
                        { visible ->
                            searchRecordsViewModel.onEvent(
                                SearchUiEvent.OnShowDatePicker(DateField.FROM, visible)
                            )
                        },
                        dateField = DateField.FROM
                    )
                    DatePickerSearchRecords(
                        modifier = Modifier.weight(1f),
                        R.string.todate,
                        state.showDatePickerTo,
                        state.searchFilter.dateTo,
                        { date ->
                            searchRecordsViewModel.onEvent(
                                SearchUiEvent.OnSelectDate(DateField.TO, date)
                            )
                        },
                        { visible ->
                            searchRecordsViewModel.onEvent(
                                SearchUiEvent.OnShowDatePicker(DateField.TO, visible)
                            )
                        },
                        dateField = DateField.TO
                    )
                }
                AccountSearchRecordsSelector(
                    R.string.selectanaccount,
                    state.accounts,
                    state.currencyCode,
                    { searchRecordsViewModel.onEvent(SearchUiEvent.OnAccountSelect(it)) },
                    false,
                    fieldModifier
                )

                TextFieldComponent(
                    modifier = fieldModifier,
                    stringResource(R.string.fromamount),
                    searchFilter.amountMin.toString(),
                    onTextChange = {
                        searchRecordsViewModel.onEvent(
                            SearchUiEvent.OnAmountsChanges(
                                it,
                                searchFilter.amountMax.toString()
                            )
                        )
                    },
                    BoardType.DECIMAL,
                    false
                )

                TextFieldComponent(
                    modifier = fieldModifier,
                    stringResource(R.string.toamount),
                    searchFilter.amountMax.toString(),
                    onTextChange = {
                        searchRecordsViewModel.onEvent(
                            SearchUiEvent.OnAmountsChanges(
                                searchFilter.amountMin.toString(),
                                it
                            )
                        )
                    },
                    BoardType.DECIMAL,
                    false
                )

                RadioButtonRecordsSearch(
                    searchFilter.selectedOption,
                    { searchRecordsViewModel.onEvent(SearchUiEvent.OnTransactionSelect(it)) },
                    fieldModifier
                )
                ModelButton(
                    text = stringResource(R.string.search),
                    MaterialTheme.typography.labelLarge,
                    fieldModifier,
                    enableSearchButton
                ) { searchRecordsViewModel.onEvent(SearchUiEvent.ConfirmSearch)

                }
            }

        } else {
        val fieldModifierLandscape = Modifier
            .fillMaxWidth(0.85f)
            .heightIn(min = 48.dp)

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp)
        ) {
            // IZQUIERDA → CRITERIOS
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HeadSetting(
                    title = stringResource(R.string.daterange),
                    MaterialTheme.typography.headlineSmall
                )
                Column(
                    modifier = fieldModifierLandscape,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DatePickerSearchRecords(
                        modifier = fieldModifierLandscape,
                        R.string.fromdate,
                        state.showDatePickerFrom,
                        state.searchFilter.dateFrom,
                        { date ->
                            searchRecordsViewModel.onEvent(
                                SearchUiEvent.OnSelectDate(DateField.FROM, date)
                            )
                        },
                        { visible ->
                            searchRecordsViewModel.onEvent(
                                SearchUiEvent.OnShowDatePicker(DateField.FROM, visible)
                            )
                        },
                        dateField = DateField.FROM
                    )
                    DatePickerSearchRecords(
                        modifier = fieldModifierLandscape,
                        R.string.todate,
                        state.showDatePickerTo,
                        state.searchFilter.dateTo,
                        { date ->
                            searchRecordsViewModel.onEvent(
                                SearchUiEvent.OnSelectDate(DateField.TO, date)
                            )
                        },
                        { visible ->
                            searchRecordsViewModel.onEvent(
                                SearchUiEvent.OnShowDatePicker(DateField.TO, visible)
                            )
                        },
                        dateField = DateField.TO
                    )
                    TextFieldComponent(
                        modifier = fieldModifierLandscape,
                        stringResource(R.string.searchentries),
                        searchFilter.description ?: "",
                        onTextChange = {
                            searchRecordsViewModel.onEvent(
                                SearchUiEvent.OnRecordDescriptionChange(
                                    it
                                )
                            )
                        },
                        BoardType.TEXT,
                        false
                    )
                }
            }
            Spacer(Modifier.width(32.dp))

            // DERECHA → IMPORTES + ACCIÓN
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AccountSearchRecordsSelector(
                    R.string.selectanaccount,
                    state.accounts,
                    state.currencyCode,
                    { searchRecordsViewModel.onEvent(SearchUiEvent.OnAccountSelect(it)) },
                    false,
                    fieldModifierLandscape
                )
                RadioButtonRecordsSearch(
                    searchFilter.selectedOption,
                    { searchRecordsViewModel.onEvent(SearchUiEvent.OnTransactionSelect(it)) },
                    fieldModifierLandscape
                )
                Row(
                    modifier = fieldModifierLandscape,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextFieldComponent(
                        modifier = Modifier.weight(1f),
                        stringResource(R.string.fromamount),
                        searchFilter.amountMin.toString(),
                        onTextChange = {
                            searchRecordsViewModel.onEvent(
                                SearchUiEvent.OnAmountsChanges(
                                    it,
                                    searchFilter.amountMax.toString()
                                )
                            )
                        },
                        BoardType.DECIMAL,
                        false
                    )

                    TextFieldComponent(
                        modifier = Modifier.weight(1f),
                        stringResource(R.string.toamount),
                        searchFilter.amountMax.toString(),
                        onTextChange = {
                            searchRecordsViewModel.onEvent(
                                SearchUiEvent.OnAmountsChanges(
                                    searchFilter.amountMin.toString(),
                                    it
                                )
                            )
                        },
                        BoardType.DECIMAL,
                        false
                    )

                }
                ModelButton(
                    text = stringResource(R.string.search),
                    MaterialTheme.typography.labelLarge,
                    fieldModifierLandscape,
                    enableSearchButton
                ) { searchRecordsViewModel.onEvent(SearchUiEvent.ConfirmSearch) }

            }

        }


    }
}







