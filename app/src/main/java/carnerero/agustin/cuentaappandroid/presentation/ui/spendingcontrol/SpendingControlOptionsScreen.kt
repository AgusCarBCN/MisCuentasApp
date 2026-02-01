package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxWidthDp = maxWidth
        val maxHeightDp = maxHeight
        val fieldModifier = Modifier
            .width(maxWidthDp * 0.85f) // mismo ancho para TODOS
            .heightIn(min = 48.dp)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                    modifier = fieldModifier,
                    stringResource(item.itemRoute.labelResource!!),
                    stringResource(item.description),
                    item.itemRoute.iconResource!!,
                    onClick = {
                        navController.navigateTopLevel(item.itemRoute.route)
                    })
            }

        }
    }
}