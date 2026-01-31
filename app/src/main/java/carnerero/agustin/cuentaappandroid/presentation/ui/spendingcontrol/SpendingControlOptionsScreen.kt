package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.model.RowComponentItem
import carnerero.agustin.cuentaappandroid.presentation.navigation.Routes
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.RowComponent
import carnerero.agustin.cuentaappandroid.utils.navigateTopLevel

@Composable
fun SpendingControlOptionsScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 25.dp)
    )

    {
        val optionList = listOf(
            RowComponentItem(Routes.SelectAccounts, R.string.accountscontroldes),
            RowComponentItem(Routes.SpendingControlByAccount, R.string.desexpensecontrol),
            RowComponentItem(Routes.SelectCategories, R.string.choosecategories),
            RowComponentItem(Routes.SpendingControlByCategory, R.string.descategorycontrol),
        )
        HeadSetting(
            title = stringResource(id = R.string.expensemanagement),
            MaterialTheme.typography.headlineSmall
        )


        optionList.forEach { item ->
            RowComponent(
                stringResource(item.itemRoute.labelResource!!),
                stringResource(item.description),
                item.itemRoute.iconResource!!,
                onClick = {
                    navController.navigateTopLevel(item.itemRoute.route)
                })
        }
    }
}