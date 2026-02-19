package carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.DatePickerSearch
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.components.DatePickerSearchRecords
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import carnerero.agustin.cuentaappandroid.utils.navigateTopLevel
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp
import java.util.Date


@Composable
fun RecordSearchScreen(
    searchRecordsViewModel: SearchRecordsViewModel,
    navController: NavController
) {
    val isPortrait=orientation == OrientationApp.Portrait
    val state by searchRecordsViewModel.uiState.collectAsStateWithLifecycle()
    val effects by searchRecordsViewModel.effect.collectAsStateWithLifecycle(initialValue = SearchEffects.Idle)

    val searchFilter=state.searchFilter
    val enableSearchButton=state.enableSearchButton
    val messageAmountError = stringResource(R.string.amountfromoverdateto)
    val messageDateError = stringResource(R.string.datefromoverdateto)

    LaunchedEffect(effects) {
        when(effects){
            SearchEffects.NavToRecordsScreen -> {
                navController.navigateTopLevel(state.route)
                Log.d("NAVIGATION", "Route from Search:${state.route}")
            }
            SearchEffects.MessageInvalidAmounts -> {
                SnackBarController.sendEvent(SnackBarEvent(messageAmountError))
            }
            SearchEffects.MessageInvalidDates -> {
                SnackBarController.sendEvent(SnackBarEvent(messageDateError))
            }
            else->{
            }
        }
    }
    if(isPortrait)
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
                    searchFilter?.description ?: "",
                    onTextChange = { searchRecordsViewModel.onEvent(SearchUiEvent.OnRecordDescriptionChange(it)) },
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
                        state.searchFilter?.dateFrom ?: Date().dateFormat(),
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
                        state.searchFilter?.dateTo ?: Date().dateFormat(),
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
                ModelButton(
                    text = stringResource(R.string.search),
                    MaterialTheme.typography.labelLarge,
                    fieldModifier,
                    enableSearchButton
                ) { searchRecordsViewModel.onEvent(SearchUiEvent.ConfirmSearch) }
                /* AccountSelector(
                    stringResource(R.string.selectanaccount),
                    accountViewModel,
                    modifier = modifier
                )

                RadioButtonSearch(searchRecordsViewModel, modifier)
                ModelButton(
                    text = stringResource(R.string.search),
                    MaterialTheme.typography.labelLarge,
                    fieldModifier,
                    enableSearchButton
                ) { searchRecordsViewModel.onEvent(SearchUiEvent.ConfirmSearch) }
            }*/
            }
        }
    }


