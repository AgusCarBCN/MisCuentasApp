package carnerero.agustin.cuentaappandroid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.createaccounts.view.AccountsViewModel
import carnerero.agustin.cuentaappandroid.createaccounts.view.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Account
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Category
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.text.DecimalFormat
import kotlin.math.abs


@Composable
fun CategoryBudgetItemControl(
    category: Category,
    categoriesViewModel: CategoriesViewModel,
    accountsViewModel: AccountsViewModel
) {

    val currencyCode by accountsViewModel.currencyCodeSelected.observeAsState("USD")
    val currentExpense = stringResource(id = R.string.currentexpense)
    val expensePercent= stringResource(id = R.string.percentexpense)
    // Estado para almacenar el total de gastos por categoría
    var expensesByCategory by remember { mutableDoubleStateOf(0.0) }
    val categoryName = stringResource(category.nameResource)
    val expenseControlText= stringResource(id = R.string.expenseControl)

    // Cargar el total de gastos de la categoría cuando cambie el composable o la categoría
    LaunchedEffect(category.id) {
        expensesByCategory = categoriesViewModel.sumOfExpensesByCategory(
            category.id,
            category.fromDate,
            category.toDate
        ) ?: 0.0
    }

    // Variables de estado para el límite de gasto y porcentaje
    val spendingLimit=category.spendingLimit  // Límite de gasto inicial

    val spendingPercentage = (abs(expensesByCategory.toFloat() / spendingLimit.toFloat()).coerceIn(
        0.0f,
        1.0f
    ))
    val spendingPercent = Math.round(spendingPercentage * 100)


    // Color de la barra de progreso según el porcentaje
    val progressColor = when {
        spendingPercentage < 0.5f -> LocalCustomColorsPalette.current.progressBar50
        spendingPercentage < 0.8f -> LocalCustomColorsPalette.current.progressBar80
        else -> LocalCustomColorsPalette.current.progressBar100
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .width(320.dp)
           /* .semantics {
                contentDescription = "$expenseControlText $categoryName"
            }*/,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(start = 15.dp, end = 20.dp, top = 5.dp, bottom = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center, // Centrar los elementos en el Row
            verticalAlignment = Alignment.CenterVertically // Alinear verticalmente al centro
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(category.iconResource),
                contentDescription = "icon",
                tint = LocalCustomColorsPalette.current.expenseColor
            )
            Text(
                text = stringResource(category.nameResource),
                modifier = Modifier
                    .padding(start = 5.dp) // Ajusta el espacio entre el icono y el texto
                , // Hace que el texto ocupe espacio disponible
                color = LocalCustomColorsPalette.current.textHeadColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Row(
            modifier = Modifier
                .padding(start = 15.dp, end = 20.dp, top = 5.dp, bottom = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center, // Centrar los elementos en el Row
            verticalAlignment = Alignment.CenterVertically // Alinear verticalmente al centro
        ) {
            Text(
                text = "${stringResource(id = R.string.fromdate)}: ${category.fromDate}",
                modifier = Modifier
                    .padding(start = 5.dp) // Ajusta el espacio entre el icono y el texto
                , // Hace que el texto ocupe espacio disponible
                color = LocalCustomColorsPalette.current.textColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${stringResource(id = R.string.todate)}: ${category.toDate}",
                modifier = Modifier
                    .padding(start = 5.dp) // Ajusta el espacio entre el icono y el texto
                , // Hace que el texto ocupe espacio disponible
                color = LocalCustomColorsPalette.current.textColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium

            )
        }


        Text(
            text = "$currentExpense: ${
                Utils.numberFormat(
                    expensesByCategory,
                    currencyCode
                )
            } / ${Utils.numberFormat(spendingLimit, currencyCode)}",

            style = MaterialTheme.typography.bodyMedium,
            color = LocalCustomColorsPalette.current.textColor,
            modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
        )

        Text(
            text = "$expensePercent $spendingPercent %" ,
            style = MaterialTheme.typography.bodyMedium,
            color = LocalCustomColorsPalette.current.textColor,
            modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        // Barra de progreso con color dinámico
        Box(
            modifier = Modifier
                .width(320.dp)
                .height(10.dp)
                .background(LocalCustomColorsPalette.current.drawerColor)
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

    @Composable
    fun AccountBudgetItemControl(
        account: Account,
        accountsViewModel: AccountsViewModel
    ) {

        val currencyCode by accountsViewModel.currencyCodeSelected.observeAsState("USD")
        val expensePercent= stringResource(id = R.string.percentexpense)
        val currentExpense = stringResource(id = R.string.currentexpense)
        val expenseControlText= stringResource(id = R.string.expenseControl)

        // Estado para almacenar el total de gastos por categoría
        var expensesByAccount by remember { mutableDoubleStateOf(0.0) }

        // Cargar el total de gastos de la categoría cuando cambie el composable o la categoría
        LaunchedEffect(account.id) {
            expensesByAccount = accountsViewModel.sumOfExpensesByAccount(
                account.id,
                account.fromDate,
                account.toDate
            ) ?: 0.0
        }

        // Variables de estado para el límite de gasto y porcentaje
        val spendingLimit by remember { mutableDoubleStateOf(account.spendingLimit) }  // Límite de gasto inicial

        val spendingPercentage =
            (abs(expensesByAccount.toFloat()) / spendingLimit.toFloat()).coerceIn(
                0.0f,
                1.0f
            ) // Porcentaje de gasto
        val spendingPercent = Math.round(spendingPercentage * 100)
        // Color de la barra de progreso según el porcentaje
        val progressColor = when {
            spendingPercentage < 0.5f -> LocalCustomColorsPalette.current.progressBar50
            spendingPercentage < 0.8f -> LocalCustomColorsPalette.current.progressBar80
            else -> LocalCustomColorsPalette.current.progressBar100
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .semantics {
                    contentDescription = "$expenseControlText ${account.name}"
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = account.name,
                modifier = Modifier
                    .padding(start = 5.dp) // Ajusta el espacio entre el icono y el texto
                , // Hace que el texto ocupe espacio disponible
                color = LocalCustomColorsPalette.current.textHeadColor,
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
                    color = LocalCustomColorsPalette.current.textColor,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${stringResource(id = R.string.todate)}: ${account.toDate}",
                    modifier = Modifier
                        .padding(start = 5.dp) // Ajusta el espacio entre el icono y el texto
                    , // Hace que el texto ocupe espacio disponible
                    color = LocalCustomColorsPalette.current.textColor,
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
                } / ${Utils.numberFormat(spendingLimit, currencyCode)}",
                style = MaterialTheme.typography.bodyMedium,
                color = LocalCustomColorsPalette.current.textColor,
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
            )
            Text(
                text = "$expensePercent $spendingPercent %" ,
                style = MaterialTheme.typography.bodyMedium,
                color = LocalCustomColorsPalette.current.textColor,
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            // Barra de progreso con color dinámico
            // Barra de progreso con color dinámico
            Box(
                modifier = Modifier
                    .width(320.dp)
                    .height(10.dp)
                    .background(LocalCustomColorsPalette.current.drawerColor)
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


