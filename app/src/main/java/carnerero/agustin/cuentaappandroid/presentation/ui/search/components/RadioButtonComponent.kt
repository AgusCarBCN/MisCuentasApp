package carnerero.agustin.cuentaappandroid.presentation.ui.search.components


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette


@Composable
fun RadioButtonSearch(searchViewModel: SearchViewModel,
                      modifier: Modifier) {
    val options = searchViewModel.options
    val selectedOptionIndex by searchViewModel.selectedOptionIndex.observeAsState(0)
    stringResource(id = R.string.selected)
    stringResource(id = R.string.isunchecked)
    Row(modifier) {
        options.forEachIndexed { index, option ->
            val radioButtonContentDescription= stringResource(option)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .selectable(
                        selected = (index == selectedOptionIndex),
                        onClick = { searchViewModel.onOptionSelected(index) }
                    )
                    .padding(6.dp)
                    .semantics {
                        contentDescription = radioButtonContentDescription
                        selected = index == selectedOptionIndex
                    }
            ) {
                RadioButton(
                    selected = (index == selectedOptionIndex),
                    onClick = { searchViewModel.onOptionSelected(index) },
                    modifier = Modifier.semantics {
                        contentDescription = radioButtonContentDescription
                        selected = index == selectedOptionIndex
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = LocalCustomColorsPalette.current.buttonColorPressed,
                        unselectedColor = LocalCustomColorsPalette.current.textColor,
                        disabledSelectedColor = LocalCustomColorsPalette.current.disableButton,
                        disabledUnselectedColor = LocalCustomColorsPalette.current.disableButton
                    )
                )
                Text(
                    text = stringResource(id = option),
                    color = LocalCustomColorsPalette.current.textColor,
                    style=MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

