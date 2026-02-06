package carnerero.agustin.cuentaappandroid.presentation.ui.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.AccountSelector
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel

@Composable

fun AmountsFields2(accountViewModel: AccountsViewModel,
                   searchViewModel: SearchViewModel,
                   fromAmount:String,
                   toAmount:String,
                   modifier: Modifier){
    AccountSelector(
        stringResource(R.string.selectanaccount),
        accountViewModel,
        modifier = modifier
    )

    RadioButtonSearch(searchViewModel, modifier)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextFieldComponent(
            modifier = Modifier.weight(1f),
            stringResource(R.string.fromamount),
            fromAmount,
            onTextChange = { searchViewModel.onAmountsFieldsChange(it, toAmount) },
            BoardType.DECIMAL,
            false
        )

        TextFieldComponent(
            modifier = Modifier.weight(1f),
            stringResource(R.string.toamount),
            toAmount,
            onTextChange = { searchViewModel.onAmountsFieldsChange(fromAmount, it) },
            BoardType.DECIMAL,
            false
        )
    }
}