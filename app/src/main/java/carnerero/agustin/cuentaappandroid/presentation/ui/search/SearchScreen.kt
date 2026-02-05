package carnerero.agustin.cuentaappandroid.presentation.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.EntriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.SearchViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.search.layouts.SearchLandscapeLayout
import carnerero.agustin.cuentaappandroid.presentation.ui.search.layouts.SearchPortraitLayout
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.math.BigDecimal
import java.util.Date

@Composable
fun SearchScreen(
    accountViewModel: AccountsViewModel,
    searchViewModel: SearchViewModel,
    entriesViewModel: EntriesViewModel,
    typeOfSearch: TypeOfSearch,
    navController: NavController
) {
    val configuration = LocalConfiguration.current
    val isTwoPane = configuration.screenWidthDp >= 600

    val fromAmount by searchViewModel.fromAmount.observeAsState("0.0")
    val toAmount by searchViewModel.toAmount.observeAsState("0.0")
    val toDate by searchViewModel.selectedToDate.observeAsState(Date().dateFormat())
    val fromDate by searchViewModel.selectedFromDate.observeAsState(Date().dateFormat())
    val entryDescription by searchViewModel.entryDescription.observeAsState("")
    val enableSearchButton by searchViewModel.enableSearchButton.observeAsState(false)
    val selectedAccount by accountViewModel.accountSelected.observeAsState()
    val selectedOption by searchViewModel.selectedOptionIndex.observeAsState()

    val id = selectedAccount?.id ?: 0
    val scope = rememberCoroutineScope()

    val messageAmountError = stringResource(R.string.amountfromoverdateto)
    val messageDateError = stringResource(R.string.datefromoverdateto)

    searchViewModel.onEnableSearchButton()

    LaunchedEffect(
        id, entryDescription, fromDate, toDate,
        fromAmount, toAmount, selectedOption
    ) {
        entriesViewModel.getFilteredEntries(
            accountId = id,
            description = entryDescription,
            dateFrom = fromDate,
            dateTo = toDate,
            amountMin = fromAmount.toBigDecimalOrNull() ?: BigDecimal.ZERO,
            amountMax = toAmount.toBigDecimalOrNull() ?: BigDecimal("1E10"),
            selectedOptions = selectedOption ?: 0
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
    ) {
        if (isTwoPane) {
            SearchLandscapeLayout(
                accountViewModel,
                searchViewModel,
                fromAmount,
                toAmount,
                entryDescription,
                enableSearchButton,
                scope,
                messageAmountError,
                messageDateError,
                typeOfSearch,
                navController
            )
        } else {
            SearchPortraitLayout(
                accountViewModel,
                searchViewModel,
                fromAmount,
                toAmount,
                entryDescription,
                enableSearchButton,
                scope,
                messageAmountError,
                messageDateError,
                typeOfSearch,
                navController
            )
        }
    }
}



/*
@Composable
fun SearchScreen(
    accountViewModel: AccountsViewModel,
    searchViewModel: SearchViewModel,
    entriesViewModel: EntriesViewModel,
    typeOfSearch: TypeOfSearch,
    navController: NavController
) {
    val fromAmount by searchViewModel.fromAmount.observeAsState("0.0")
    val toAmount by searchViewModel.toAmount.observeAsState("0.0")
    val toDate by searchViewModel.selectedToDate.observeAsState(Date().dateFormat())
    val fromDate by searchViewModel.selectedFromDate.observeAsState(Date().dateFormat())
    val entryDescription by searchViewModel.entryDescription.observeAsState("")
    val enableSearchButton by searchViewModel.enableSearchButton.observeAsState(false)
    val selectedAccount by accountViewModel.accountSelected.observeAsState()
    val selectedOption by searchViewModel.selectedOptionIndex.observeAsState()
    val id=selectedAccount?.id?:0
    val scope = rememberCoroutineScope()
    val messageAmountError = stringResource(id = R.string.amountfromoverdateto)
    val messageDateError = stringResource(id = R.string.datefromoverdateto)
    searchViewModel.onEnableSearchButton()

    LaunchedEffect(id, entryDescription, fromDate, toDate, fromAmount, toAmount, selectedOption) {
        // Esto solo se ejecutará cuando cambie cualquiera de los valores clave
        entriesViewModel.getFilteredEntries(
            accountId = id,
            description = entryDescription,
            dateFrom = fromDate,
            dateTo = toDate,
            amountMin = fromAmount.toBigDecimalOrNull() ?: BigDecimal.ZERO,
            amountMax = toAmount.toBigDecimalOrNull() ?: BigDecimal("1E10"),
            selectedOptions = selectedOption?:0
        )
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxWidthDp = maxWidth
        val maxHeightDp = maxHeight
        val fieldModifier = Modifier
            .width(maxWidthDp*0.85f) // mismo ancho para TODOS
            .heightIn(min = 48.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .background(colors.backgroundPrimary)
                .verticalScroll(
                    rememberScrollState()
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextFieldComponent(
                modifier = fieldModifier,
                stringResource(id = R.string.searchentries),
                entryDescription,
                onTextChange = { searchViewModel.onEntryDescriptionChanged(it) },
                BoardType.TEXT,
                false
            )
            HeadSetting(
                title = stringResource(id = R.string.daterange),
                androidx.compose.material3.MaterialTheme.typography.headlineSmall
            )
            Row(fieldModifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp))
            {
                DatePickerSearch(
                    modifier = Modifier
                        .weight(1f),
                    R.string.fromdate,
                    searchViewModel,
                    true
                )
                DatePickerSearch(
                    modifier = Modifier
                        .weight(1f),
                    R.string.todate,
                    searchViewModel,
                    false
                )
            }
            AccountSelector(


                stringResource(id = R.string.selectanaccount),
                accountViewModel,
                modifier = Modifier.width(300.dp)
            )
            RadioButtonSearch(searchViewModel,
                fieldModifier)
            TextFieldComponent(
                modifier = fieldModifier,
                stringResource(id = R.string.fromamount),
                fromAmount,
                onTextChange = {
                    searchViewModel.onAmountsFieldsChange(it, toAmount)

                },
                BoardType.DECIMAL,
                false
            )
            TextFieldComponent(
                modifier = fieldModifier,
                stringResource(id = R.string.toamount),
                toAmount,
                onTextChange = {
                    searchViewModel.onAmountsFieldsChange(fromAmount, it)
                },
                BoardType.DECIMAL,
                false
            )
            ModelButton(
                text = stringResource(id = R.string.search),
                MaterialTheme.typography.labelLarge,
                fieldModifier,
                enableSearchButton,
                onClickButton = {
                    if (!searchViewModel.validateAmounts(fromAmount, toAmount)) {
                        scope.launch(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    messageAmountError
                                )
                            )
                        }
                    } else if (!searchViewModel.validateDates()) {
                        scope.launch(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    messageDateError
                                )
                            )
                        }

                    } else {
                        when (typeOfSearch) {
                            TypeOfSearch.SEARCH -> navController.navigateTopLevel(Routes.Records.route)
                            TypeOfSearch.DELETE -> navController.navigateTopLevel(Routes.RecordsToDelete.route)
                            TypeOfSearch.UPDATE -> navController.navigateTopLevel(Routes.RecordsToModify.route)
                        }
                    }
                }
            )
        }
    }
}*/