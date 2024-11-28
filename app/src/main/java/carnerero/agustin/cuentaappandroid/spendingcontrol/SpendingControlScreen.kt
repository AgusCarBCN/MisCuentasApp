package carnerero.agustin.cuentaappandroid.spendingcontrol

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.components.HeadSetting
import carnerero.agustin.cuentaappandroid.components.RowComponent
import carnerero.agustin.cuentaappandroid.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.main.view.MainViewModel

@Composable
fun SpendingControlScreen(mainViewModel: MainViewModel)
{

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 25.dp)

    )

    {
        HeadSetting(title = stringResource(id = R.string.expensemanagement), MaterialTheme.typography.headlineSmall)

        RowComponent(title = stringResource(id = R.string.accountscontrol),
            description = stringResource(id = R.string.accountscontroldes),
            iconResource = R.drawable.ic_selectaccounts,
            onClick = {
                mainViewModel.selectScreen(IconOptions.SELECT_ACCOUNTS)
            })

        RowComponent(title = stringResource(id = R.string.expensecontrol),
            description = stringResource(id = R.string.desexpensecontrol),
            iconResource = R.drawable.ic_expensetotal,
            onClick = {
                mainViewModel.selectScreen(IconOptions.ACCOUNT_EXPENSE_CONTROL)
            })

        RowComponent(title = stringResource(id = R.string.selectcategories),
            description = stringResource(id = R.string.choosecategories),
            iconResource = R.drawable.ic_selectcategories,
            onClick = {
                mainViewModel.selectScreen(IconOptions.SELECT_CATEGORIES)
            })
        RowComponent(title = stringResource(id = R.string.categorycontrol),
            description = stringResource(id = R.string.descategorycontrol),
            iconResource = R.drawable.ic_categorycontrol,
            onClick = {
                mainViewModel.selectScreen(IconOptions.CATEGORY_EXPENSE_CONTROL)
            })
    }
}