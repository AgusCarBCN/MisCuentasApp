package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.utils.SnackBarController
import carnerero.agustin.cuentaappandroid.utils.SnackBarEvent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.CurrencySelector
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.IconAnimated
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.message
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.AccountsViewModel
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.presentation.theme.LocalCustomColorsPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal


//Mapa de divisas y simbolos


@Composable

fun CreateAccountsComponent(
    accountsViewModel: AccountsViewModel,
    enableSelector:Boolean,
    navToLogin: () -> Unit,
    navToBack: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val currencyShowedCode by accountsViewModel.currencyCodeShowed.observeAsState("EUR")
    val listOfAccounts by accountsViewModel.listOfAccounts.observeAsState(emptyList())
    val isCurrencyExpanded by accountsViewModel.isCurrencyExpanded.observeAsState(false)
    val isEnableButton by accountsViewModel.isEnableButton.observeAsState(false)
    val accountName by accountsViewModel.name.observeAsState("")
    val accountBalance by accountsViewModel.amount.observeAsState("")
    val enableCurrencySelector by accountsViewModel.enableCurrencySelector.observeAsState(
        enableSelector
    )
    val messageSuccess = message(resource = R.string.newaccountcreated)
    val messageError = message(resource = R.string.erroraccountcreated)
    val errorWritingDataStore = message(resource = R.string.errorwritingdatastore)
    LaunchedEffect(Unit) {
        accountsViewModel.getListOfCurrencyCode()
        accountsViewModel.getAllAccounts()

    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val contentHeight = maxHeight * 1f
        val fieldModifier = Modifier
            .fillMaxWidth(0.85f) // mismo ancho para TODOS
            .heightIn(min = 48.dp)

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalCustomColorsPalette.current.backgroundPrimary) // Reemplaza con tu color de fondo
        ) {
            Column(
                modifier = if (enableSelector)
                    Modifier.fillMaxSize()
                        .padding(top = 30.dp)
                else Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .verticalScroll(
                        rememberScrollState()
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isCurrencyExpanded) {
                    Text(
                        modifier = Modifier
                            .padding(50.dp),
                        text = stringResource(id = R.string.createAccount),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = LocalCustomColorsPalette.current.textColor
                    )
                    if (!enableCurrencySelector) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(60.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconAnimated(
                                iconResource = R.drawable.configaccountoption, sizeIcon = 120,
                                LocalCustomColorsPalette.current.imageTutorialInit,
                                LocalCustomColorsPalette.current.imageTutorialTarget
                            )
                        }
                    }

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.createaccountmsg),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = LocalCustomColorsPalette.current.textColor
                    )

                    TextFieldComponent(
                        modifier = fieldModifier,
                        stringResource(id = R.string.amountName),
                        accountName,
                        onTextChange = {
                            accountsViewModel.onTextFieldsChanged(
                                it,
                                accountBalance
                            )
                        },
                        BoardType.TEXT,
                        false
                    )
                    TextFieldComponent(
                        modifier = fieldModifier,
                        stringResource(id = R.string.enteramount),
                        accountBalance,
                        onTextChange = {
                            accountsViewModel.onTextFieldsChanged(accountName, it)
                        },
                        BoardType.DECIMAL,
                        false
                    )
                    ModelButton(
                        text = stringResource(id = R.string.addAccount),
                        MaterialTheme.typography.labelLarge,
                        modifier = fieldModifier,
                        isEnableButton,
                        onClickButton = {
                            accountsViewModel.createNewAccount(accountBalance,
                                accountName,
                                messageSuccess,
                                messageError)

                        }
                    )
                    ModelButton(
                        text = stringResource(id = R.string.backButton),
                        MaterialTheme.typography.labelLarge,
                        modifier = fieldModifier,
                        true,
                        onClickButton = {
                            accountsViewModel.resetFields()
                            navToBack()
                        }
                    )

                }
                if (enableCurrencySelector) {
                    CurrencySelector(accountsViewModel
                    )
                }
                if (!isCurrencyExpanded) {
                    if (enableCurrencySelector) {
                        ModelButton(
                            text = stringResource(id = R.string.confirmButton),
                            MaterialTheme.typography.labelLarge,
                            modifier = fieldModifier,
                            enableSelector,
                            onClickButton = {
                                /*scope.launch(Dispatchers.IO) {
                                    try {
                                        accountsViewModel.setCurrencyCode(currencyShowedCode)

                                    } catch (_: Exception) {
                                        withContext(Dispatchers.Main) {
                                            SnackBarController.sendEvent(
                                                event = SnackBarEvent(
                                                    errorWritingDataStore
                                                )
                                            )
                                        }
                                    }
                                }*/
                                accountsViewModel.setCurrencyCode(currencyShowedCode)
                                navToLogin()
                            }
                        )
                    }
                }

            }
        }
    }
}
