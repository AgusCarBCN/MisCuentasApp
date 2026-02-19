package carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.TransactionOption
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.TransactionType


@Composable
fun RadioButtonRecordsSearch(
    selectedOptionType: TransactionType,
    onOptionSelected:(TransactionType)->Unit,
    modifier: Modifier) {
    val options = listOf(TransactionOption(R.string.incomeoption, TransactionType.INCOME),
        TransactionOption(R.string.expenseoption, TransactionType.EXPENSE),
        TransactionOption(R.string.alloption, TransactionType.ALL)
       )
    //val options = searchViewModel.options
    //val selectedOptionIndex by searchViewModel.selectedOptionIndex.observeAsState(0)
    stringResource(id = R.string.selected)
    stringResource(id = R.string.isunchecked)
    Row(modifier) {
        options.forEach { option ->
            val radioButtonContentDescription= stringResource(option.resourceString)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .selectable(
                        selected = (option.type == selectedOptionType),
                        onClick = { onOptionSelected(option.type)
                            /*searchViewModel.onOptionSelected(index)*/ }
                    )
                    .padding(6.dp)
                    .semantics {
                        contentDescription = radioButtonContentDescription
                        selected = option.type == selectedOptionType
                    }
            ) {
                RadioButton(
                    selected = (option.type == selectedOptionType),
                    onClick = { onOptionSelected(option.type)
                        /*searchViewModel.onOptionSelected(index)*/ },
                    modifier = Modifier.semantics {
                        contentDescription = radioButtonContentDescription
                        selected = option.type == selectedOptionType
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colors.buttonColorPressed,
                        unselectedColor = colors.textColor,
                        disabledSelectedColor = colors.disableButton,
                        disabledUnselectedColor = colors.disableButton
                    )
                )
                Text(
                    text = stringResource(id = option.resourceString),
                    color = colors.textColor,
                    style=MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

