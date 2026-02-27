package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.accounts


/*
    @Composable
    fun AccountBudgetItemControl(
        account: Account,
        accountsViewModel: AccountsViewModel
    ) {

        val currencyCode by accountsViewModel.currencyCodeSelected.observeAsState("USD")
        val expensePercent= stringResource(id = R.string.percentexpense)
        val currentExpense = stringResource(id = R.string.currentexpense)
        val expenseControlText= stringResource(id = R.string.expenseControl)
        val spendingLimitMessage= stringResource(id = R.string.limitMax)
        // Estado para almacenar el total de gastos por categoría
        var expensesByAccount by remember { mutableStateOf(BigDecimal.ZERO) }

        // Cargar el total de gastos de la categoría cuando cambie el composable o la categoría
        LaunchedEffect(Unit) {
            expensesByAccount = accountsViewModel.sumOfExpensesByAccount(
                account.id,
                account.fromDate,
                account.toDate
            ) ?: BigDecimal.ZERO
        }

        // Variables de estado para el límite de gasto y porcentaje
        val spendingLimit by remember { mutableStateOf(account.spendingLimit) }  // Límite de gasto inicial

        val spendingPercentage =
            (abs(expensesByAccount.toFloat()) / spendingLimit.toFloat()).coerceIn(
                0.0f,
                1.0f
            ) // Porcentaje de gasto
        val spendingPercent = (spendingPercentage * 100).roundToInt()
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
*/

