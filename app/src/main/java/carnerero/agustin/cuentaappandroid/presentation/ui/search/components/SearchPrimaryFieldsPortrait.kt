package carnerero.agustin.cuentaappandroid.presentation.ui.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.AccountSelector
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.DatePickerSearch
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting

@Composable
 fun SearchPrimaryFields(
    accountViewModel: AccountsViewModel,
    searchViewModel: SearchViewModel,
    entryDescription: String,
    modifier: Modifier
) {
    TextFieldComponent(
        modifier = modifier,
        stringResource(R.string.searchentries),
        entryDescription,
        onTextChange = searchViewModel::onEntryDescriptionChanged,
        BoardType.TEXT,
        false
    )

    HeadSetting(
        title = stringResource(R.string.daterange),
        MaterialTheme.typography.headlineSmall
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DatePickerSearch(
            modifier = Modifier.weight(1f),
            R.string.fromdate,
            searchViewModel,
            true
        )
        DatePickerSearch(
            modifier = Modifier.weight(1f),
            R.string.todate,
            searchViewModel,
            false
        )
    }

    AccountSelector(
        stringResource(R.string.selectanaccount),
        accountViewModel,
        modifier = modifier
    )

    RadioButtonSearch(searchViewModel, modifier)
}
