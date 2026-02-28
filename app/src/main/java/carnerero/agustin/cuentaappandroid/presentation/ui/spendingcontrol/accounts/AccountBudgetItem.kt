package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.accounts

import android.R.attr.contentDescription
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.model.BudgetUiState
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.math.BigDecimal


@Composable
    fun AccountBudgetItemControl(
    account: Account,
    currencyCode:String,
    uiState: BudgetUiState?

    ) {

       // val currencyCode by accountsViewModel.currencyCodeSelected.observeAsState("USD")
        val expensePercent= stringResource(id = R.string.percentexpense)
        val currentExpense = stringResource(id = R.string.currentexpense)
        val expenseControlText= stringResource(id = R.string.expenseControl)
        val spendingLimitMessage= stringResource(id = R.string.limitMax)

        val spendingLimit = account.spendingLimit
        val spendingPercentage = uiState?.spendingPercentage?:0.0f
        val spendingPercent = uiState?.spendingPercent?:0.0f
        val expensesByAccount = uiState?.expenses?: BigDecimal.ZERO

        // Color de la barra de progreso según el porcentaje
        val progressColor = when {
            spendingPercentage < 0.5f -> colors.progressBar50
            spendingPercentage < 0.8f -> colors.progressBar80
            else -> colors.progressBar100
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = account.name,
                modifier = Modifier
                    .padding(start = 5.dp) // Ajusta el espacio entre el icono y el texto
                , // Hace que el texto ocupe espacio disponible
                color = colors.textHeadColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )

            Row(
                modifier = Modifier
                    .padding(start = 15.dp, end = 20.dp, top = 5.dp, bottom = 15.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center, // Centrar los elementos en el Row
                verticalAlignment = Alignment.CenterVertically // Alinear verticalmente al centro
            ) {
                Text(
                    text = "${stringResource(id = R.string.fromdate)}: ${account.fromDate}",
                    modifier = Modifier
                        .padding(start = 5.dp) // Ajusta el espacio entre el icono y el texto
                    , // Hace que el texto ocupe espacio disponible
                    color = colors.textColor,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${stringResource(id = R.string.todate)}: ${account.toDate}",
                    modifier = Modifier
                        .padding(start = 5.dp) // Ajusta el espacio entre el icono y el texto
                    , // Hace que el texto ocupe espacio disponible
                    color = colors.textColor,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Barra de progreso de gasto actual
            Text(
                text = "$currentExpense: ${
                    Utils.numberFormat(
                        expensesByAccount,
                        currencyCode
                    )
                } ",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textColor,
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
            )
            Text(
                text ="$spendingLimitMessage: ${Utils.numberFormat(spendingLimit, currencyCode)}",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textColor,
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
            )

            Text(
                text = "$expensePercent $spendingPercent %" ,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textColor,
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            // Barra de progreso con color dinámico
            // Barra de progreso con color dinámico
            Box(
                modifier = Modifier
                    .width(320.dp)
                    .height(10.dp)
                    .background(colors.drawerColor)
                    .clip(RoundedCornerShape(5.dp))
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


