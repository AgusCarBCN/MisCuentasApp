package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectaccounts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.utils.Utils

@Composable
fun AccountWithCheckbox(
    account: Account,
    currencyCode:String,
    onCheckBoxChange: (Boolean) -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardColors(
            containerColor = colors.drawerColor,
            contentColor = colors.incomeColor,
            disabledContainerColor = colors.drawerColor,
            disabledContentColor = colors.incomeColor
        ),
        modifier = Modifier
            .size(width = 360.dp, height = 120.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                ,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = account.name,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.6f),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textColor
            )
            Spacer(modifier = Modifier.height(12.dp)) // Espacio entre el texto y el botón
            Text(
                text = Utils.numberFormat(account.balance, currencyCode),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.titleSmall
            )
        }
        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre el texto y el botón
        Row(
            modifier = Modifier.padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (account.isChecked) stringResource(id = R.string.accountchecked)
                else stringResource(id = R.string.accountunchecked),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.6f),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textColor
            )
            Checkbox(
                modifier = Modifier.weight(0.2f), // Ajuste proporcional para el checkbox
                checked = account.isChecked,
                onCheckedChange = onCheckBoxChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = colors.drawerColor,
                    uncheckedColor = colors.textColor,
                    checkmarkColor = colors.incomeColor
                )
            )
        }
    }
}