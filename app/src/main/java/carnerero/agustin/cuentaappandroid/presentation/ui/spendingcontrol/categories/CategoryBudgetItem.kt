package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.math.BigDecimal

@Composable
fun CategoryBudgetItemControl(
    category: Category,
    currencyCode: String,
    uiState: CategoryBudgetUiState?
) {

    val currentExpenseText = stringResource(id = R.string.currentexpense)
    val spendingLimitMessage = stringResource(id = R.string.limitMax)
    val percentText = stringResource(id = R.string.percentexpense)

    val spendingLimit = category.spendingLimit
    val spendingPercentage = uiState?.spendingPercentage?:0.0f
    val spendingPercent = uiState?.spendingPercent?:0.0f
    val expensesByCategory = uiState?.expenses?: BigDecimal.ZERO

    val progressColor = when {
        spendingPercentage < 0.5f -> colors.progressBar50
        spendingPercentage < 0.8f -> colors.progressBar80
        else -> colors.progressBar100
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .width(320.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔹 Título con icono
        Row(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(category.iconResource),
                contentDescription = null,
                tint = colors.expenseColor
            )

            Text(
                text = stringResource(category.nameResource),
                modifier = Modifier.padding(start = 5.dp),
                color = colors.textHeadColor,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        // 🔹 Fechas
        Row(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = "${stringResource(id = R.string.fromdate)}: ${
                    Utils.toDateShortFormatLocale(category.fromDate)
                }",
                color = colors.textColor,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "${stringResource(id = R.string.todate)}: ${
                    Utils.toDateShortFormatLocale(category.toDate)
                }",
                color = colors.textColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // 🔹 Gasto actual
        Text(
            text = "$currentExpenseText: ${
                Utils.numberFormat(expensesByCategory, currencyCode)
            }",
            style = MaterialTheme.typography.bodyLarge,
            color = colors.textColor,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // 🔹 Límite
        Text(
            text = "$spendingLimitMessage: ${
                Utils.numberFormat(spendingLimit, currencyCode)
            }",
            style = MaterialTheme.typography.bodyLarge,
            color = colors.textColor,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // 🔹 Porcentaje
        Text(
            text = "$percentText $spendingPercent %",
            style = MaterialTheme.typography.bodyLarge,
            color = colors.textColor,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Barra de progreso
        Box(
            modifier = Modifier
                .width(320.dp)
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(colors.drawerColor)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(spendingPercentage)
                    .fillMaxHeight()
                    .background(progressColor)
            )
        }
    }
}