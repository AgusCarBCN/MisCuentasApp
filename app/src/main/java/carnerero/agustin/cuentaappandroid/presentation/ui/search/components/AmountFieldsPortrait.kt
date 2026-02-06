package carnerero.agustin.cuentaappandroid.presentation.ui.search.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel

@Composable
 fun SearchAmountFields(
    searchViewModel: SearchViewModel,
    fromAmount:String,
    toAmount:String,
    modifier: Modifier
) {

    TextFieldComponent(
        modifier = modifier,
        stringResource(R.string.fromamount),
        fromAmount,
        onTextChange = { searchViewModel.onAmountsFieldsChange(it, toAmount) },
        BoardType.DECIMAL,
        false
    )

    TextFieldComponent(
        modifier = modifier,
        stringResource(R.string.toamount),
        toAmount,
        onTextChange = { searchViewModel.onAmountsFieldsChange(fromAmount, it) },
        BoardType.DECIMAL,
        false
    )
}
