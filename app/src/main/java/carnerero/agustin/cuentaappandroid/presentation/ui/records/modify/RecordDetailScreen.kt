package carnerero.agustin.cuentaappandroid.presentation.ui.records.modify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
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
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.IconAnimated
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.dimens
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.orientation
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.components.DatePickerSearchRecords
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField
import com.kapps.differentscreensizesyt.ui.theme.OrientationApp

@Composable
fun ModifyRecordScreen(
    recordDTO: RecordDTO,
    recordDetailViewModel: RecordDetailViewModel
) {
    val messageModify = stringResource(id = R.string.modifyentrymsg)
    val state by recordDetailViewModel.uiState.collectAsStateWithLifecycle()

    // Sincroniza los datos iniciales del ViewModel
    LaunchedEffect(recordDTO) {
        recordDetailViewModel.getInitValues(recordDTO)
    }

    LaunchedEffect(Unit) {
        recordDetailViewModel.effect.collect { effect ->
            when (effect) {
                RecordDetailEffects.MessageUpdateRecord -> SnackBarController.sendEvent(
                    SnackBarEvent(messageModify)
                )
            }
        }
    }


    val initColor =
        if (recordDTO.categoryType == CategoryType.INCOME) colors.iconIncomeInit
        else colors.iconExpenseInit
    val targetColor = if (recordDTO.categoryType == CategoryType.INCOME) colors.iconIncomeTarget
    else colors.iconExpenseTarget
    val isLandscape =
        orientation == OrientationApp.Landscape

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxWidthDp = maxWidth
        val fieldModifier = Modifier
            .fillMaxWidth(0.85f)
            .heightIn(min = 48.dp)
            .padding(dimens.small)
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                /** -------- LEFT (FORM) -------- */
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconAnimated(recordDTO.iconResource, 120, initColor, targetColor)
                    HeadSetting(
                        title = stringResource(R.string.modifydes),
                        MaterialTheme.typography.headlineMedium
                    )
                    TextFieldComponent(
                        modifier = fieldModifier,
                        stringResource(id = R.string.desamount),
                        state.description,
                        onTextChange = {
                           recordDetailViewModel.onUserEvent(RecordDetailUiEvent.OnDescriptionChange(it)
                            )
                        },

                        BoardType.TEXT,
                        false
                    )
                }
                /** -------- RIGHT (ACTIONS) -------- */
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(0.5f)
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HeadSetting(
                        title = stringResource(R.string.modifydate),
                        MaterialTheme.typography.headlineMedium
                    )
                    DatePickerSearchRecords(
                        modifier = fieldModifier,
                        R.string.fromdate,
                        state.showDatePicker,
                        state.date,
                        {
                            recordDetailViewModel.onUserEvent(RecordDetailUiEvent.OnDateChange(it))
                        },
                        {
                            recordDetailViewModel.onUserEvent(RecordDetailUiEvent.OnShowDatePicker(it))
                        },
                        dateField = DateField.FROM
                    )
                    HeadSetting(
                        title = stringResource(R.string.modifyamount),
                        MaterialTheme.typography.headlineMedium
                    )
                    TextFieldComponent(
                        modifier = fieldModifier,
                        stringResource(id = R.string.modifyamount),
                        state.amount,
                        onTextChange = {
                            recordDetailViewModel.onUserEvent(RecordDetailUiEvent.OnAmountChange(it))
                        },
                        BoardType.DECIMAL,
                        false
                    )
                    Row(
                        modifier = fieldModifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.medium)
                    ) {
                        ModelButton(
                            text = stringResource(id = R.string.modifyButton),
                            MaterialTheme.typography.labelSmall,
                            modifier = fieldModifier.weight(1f),
                            true,
                            onClickButton = {
                                recordDetailViewModel.onUserEvent(RecordDetailUiEvent.Modify(recordDTO))
                                /*val amountBefore = recordDTO.amount
                                val updateBalanceIncome = amountEntry.toBigDecimal() - amountBefore
                                val updateBalanceExpense =
                                    amountEntry.toBigDecimal().negate().add(amountBefore)

                                val entryDTOUpdated = RecordDTO(

                                    recordDTO.id,
                                    descriptionEntry,
                                    if (recordDTO.categoryType == CategoryType.INCOME) amountEntry.toBigDecimal()
                                    else amountEntry.toBigDecimal().negate(),
                                    dateSelected,
                                    recordDTO.iconResource,
                                    recordDTO.nameResource,
                                    recordDTO.accountId,
                                    recordDTO.name,
                                    recordDTO.categoryId,
                                    recordDTO.categoryType
                                )
                                entriesViewModel.updateEntry(
                                    recordDTO.id,
                                    descriptionEntry,
                                    if (recordDTO.categoryType == CategoryType.INCOME) amountEntry.toBigDecimal()
                                    else amountEntry.toBigDecimal().negate(),
                                    dateSelected
                                )
                                entriesViewModel.updateEntries(recordDTO.id, entryDTOUpdated)
                                //mainViewModel.selectScreen(IconOptions.ENTRIES_TO_UPDATE)
                                //Actualiza balance de cuenta
                                accountsViewModel.updateAccountBalance(
                                    recordDTO.accountId,
                                    if (recordDTO.categoryType == CategoryType.INCOME) updateBalanceIncome
                                    else updateBalanceExpense,
                                    false
                                )


                                entriesViewModel.getTotal()
                                scope.launch(Dispatchers.Main) {
                                    SnackBarController.sendEvent(event = SnackBarEvent(messageModify))
                                }*/
                            }


                        )


                    }
                }
            }

        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .verticalScroll(
                        rememberScrollState()
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconAnimated(recordDTO.iconResource, 120, initColor, targetColor)
                HeadSetting(
                    title = stringResource(R.string.modifydes),
                    MaterialTheme.typography.headlineMedium
                )
                TextFieldComponent(
                    modifier = fieldModifier,
                    stringResource(id = R.string.desamount),
                    state.description,
                    onTextChange = { recordDetailViewModel.onUserEvent(RecordDetailUiEvent.OnDescriptionChange(it))},
                    BoardType.TEXT,
                    false
                )
                HeadSetting(
                    title = stringResource(R.string.modifydate),
                    MaterialTheme.typography.headlineMedium
                )
                DatePickerSearchRecords(
                    modifier = fieldModifier,
                    R.string.fromdate,
                    state.showDatePicker,
                    state.date,
                    {
                        recordDetailViewModel.onUserEvent(RecordDetailUiEvent.OnDateChange(it))
                    },
                    {
                        recordDetailViewModel.onUserEvent(RecordDetailUiEvent.OnShowDatePicker(it))
                    },
                    dateField = DateField.FROM
                )
                HeadSetting(
                    title = stringResource(R.string.modifyamount),
                    MaterialTheme.typography.headlineMedium
                )
                TextFieldComponent(
                    modifier = fieldModifier,
                    stringResource(id = R.string.modifyamount),
                    state.amount,
                    onTextChange = {
                        recordDetailViewModel.onUserEvent(RecordDetailUiEvent.OnAmountChange(it))
                    },

                    BoardType.DECIMAL,
                    false
                )
                ModelButton(
                    text = stringResource(id = R.string.modifyButton),
                    MaterialTheme.typography.labelLarge,
                    modifier = fieldModifier,
                    true,
                    onClickButton = {
                        recordDetailViewModel.onUserEvent(RecordDetailUiEvent.Modify(recordDTO))
                    }
                )


            }
        }
    }

}