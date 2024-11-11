package carnerero.agustin.cuentaappandroid.components


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.search.SearchViewModel
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette


@Composable
fun RadioButtonSearch(searchViewModel: SearchViewModel) {
    val options = searchViewModel.options
    val selectedOptionIndex by searchViewModel.selectedOptionIndex.observeAsState(0)

    Row {
        options.forEachIndexed { index, option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .selectable(
                        selected = (index == selectedOptionIndex),
                        onClick = { searchViewModel.onOptionSelected(index) }
                    )
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = (index == selectedOptionIndex),
                    onClick = { searchViewModel.onOptionSelected(index) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = LocalCustomColorsPalette.current.buttonColorPressed,
                        unselectedColor = LocalCustomColorsPalette.current.textColor,
                        disabledSelectedColor = LocalCustomColorsPalette.current.disableButton,
                        disabledUnselectedColor = LocalCustomColorsPalette.current.disableButton
                    )
                )
                Text(
                    text = stringResource(id = option),
                    color = LocalCustomColorsPalette.current.textColor
                )
            }
        }
    }
}

