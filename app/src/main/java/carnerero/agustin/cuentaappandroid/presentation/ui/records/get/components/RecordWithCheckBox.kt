package carnerero.agustin.cuentaappandroid.presentation.ui.records.get.components

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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.math.BigDecimal

@Composable
fun RecordWithCheckBox(
    entry: RecordDTO,
    currencyCode: String,
    isChecked:Boolean,
    onSelectionChange: () -> Unit
){

    val entryDescription=entry.description
    val amount= stringResource(id = R.string.amountentrie)
    val checked="$entryDescription ${stringResource(R.string.ischeckedtodelete)}"
    val unchecked = stringResource(R.string.isuncheckedtodelete)
    val checkbox=stringResource(id = R.string.checkbox)
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
            .padding(bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 15.dp, end = 20.dp, top = 5.dp)

            ,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.description,
                modifier = Modifier
                    .weight(0.6f),
                textAlign = TextAlign.Start,
                style=MaterialTheme.typography.bodyLarge,
                color = colors.textHeadColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = Utils.numberFormat(entry.amount, currencyCode),
                modifier = Modifier
                    .weight(0.4f),
                color = if (entry.amount >= BigDecimal.ZERO) colors.incomeColor
                else colors.expenseColor,
                textAlign = TextAlign.End,
                style=MaterialTheme.typography.bodyLarge
            )

        }
        Row(
            modifier = Modifier
                .padding(start = 15.dp, end = 20.dp, top = 5.dp)
                ,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = entry.iconResource),
                contentDescription = null,
                tint = colors.textColor
            )
            Spacer(modifier = Modifier.height(20.dp)) // Espacio entre el texto y el botón
            Text(
                text = stringResource(id = entry.nameResource),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f)
                ,
                color = colors.textColor,
                textAlign = TextAlign.Start,
                style=MaterialTheme.typography.bodyLarge

            )
            Checkbox(
                modifier = Modifier
                    .weight(0.2f)
                ,checked = isChecked,
                onCheckedChange = {
                    onSelectionChange()
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = colors.drawerColor,
                    uncheckedColor = colors.textColor,
                    checkmarkColor = colors.incomeColor
                )
            )
        }
    }
}