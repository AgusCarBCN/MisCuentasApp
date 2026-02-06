package carnerero.agustin.cuentaappandroid.presentation.ui.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.DatePickerSearch
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting

@Composable
fun SearchPrimaryFields2(
    searchViewModel: SearchViewModel,
    entryDescription: String,
    modifier: Modifier
) {


    HeadSetting(
        title = stringResource(R.string.daterange),
        MaterialTheme.typography.headlineSmall
    )
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DatePickerSearch(
            modifier = modifier,
            R.string.fromdate,
            searchViewModel,
            true
        )
        DatePickerSearch(
            modifier ,
            R.string.todate,
            searchViewModel,
            false
        )

    }
    TextFieldComponent(
        modifier = modifier,
        stringResource(R.string.searchentries),
        entryDescription,
        onTextChange = searchViewModel::onEntryDescriptionChanged,
        BoardType.TEXT,
        false
    )
}
