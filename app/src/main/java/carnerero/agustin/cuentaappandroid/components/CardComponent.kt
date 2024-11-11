package carnerero.agustin.cuentaappandroid.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carnerero.agustin.cuentaappandroid.R
import coil.compose.rememberAsyncImagePainter
import carnerero.agustin.cuentaappandroid.SnackBarController
import carnerero.agustin.cuentaappandroid.SnackBarEvent
import carnerero.agustin.cuentaappandroid.createaccounts.view.AccountsViewModel
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Account
import carnerero.agustin.cuentaappandroid.search.SearchViewModel
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable

fun UserImage(uri: Uri, size: Int) {
    Card(
        modifier = Modifier
            .size(size.dp)
            .padding(10.dp),
        shape = CircleShape
    )

    {
        Image(
            painter = if (uri == Uri.EMPTY) painterResource(id = R.drawable.contabilidad)
            else rememberAsyncImagePainter(uri), // Carga la imagen desde el Uri ,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun HeadCard(modifier: Modifier, amount: String, isIncome: Boolean, onClickCard: () -> Unit) {

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardColors(
            containerColor = LocalCustomColorsPalette.current.drawerColor,
            contentColor = if (isIncome) LocalCustomColorsPalette.current.incomeColor else LocalCustomColorsPalette.current.expenseColor,
            disabledContainerColor = LocalCustomColorsPalette.current.drawerColor,
            disabledContentColor = LocalCustomColorsPalette.current.incomeColor

        ),
        modifier = Modifier
            .size(width = 180.dp, height = 120.dp)
    ) {
        Text(
            text = amount,
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp)) // Espacio entre el texto y el botón

        TextButton(
            onClick = {
                onClickCard()
            },
            content = {
                Text(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = if (isIncome) R.string.seeincome else R.string.seeexpense),
                    fontSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_body_large).toSp() },
                    textAlign = TextAlign.Center,
                    color = LocalCustomColorsPalette.current.textColor
                )
            }
        )
    }
}

@Composable
fun AccountCard(
    account: Account,
    currencyCode: String,
    textButton: Int,
    onClickCard: () -> Unit
) {

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardColors(
            containerColor = LocalCustomColorsPalette.current.drawerColor,
            contentColor = LocalCustomColorsPalette.current.incomeColor,
            disabledContainerColor = LocalCustomColorsPalette.current.drawerColor,
            disabledContentColor = LocalCustomColorsPalette.current.incomeColor

        ),
        modifier = Modifier
            .size(width = 360.dp, height = 120.dp)
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = account.name,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.6f),
                textAlign = TextAlign.Start,
                fontSize = 22.sp,
                color = LocalCustomColorsPalette.current.textColor
            )
            Spacer(modifier = Modifier.height(12.dp)) // Espacio entre el texto y el botón
            Text(
                text = Utils.numberFormat(account.balance, currencyCode),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f),
                textAlign = TextAlign.End,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre el texto y el botón

        TextButton(
            onClick = {
                onClickCard()
            },
            content = {
                Text(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = textButton),
                    fontSize = with(LocalDensity.current) { dimensionResource(id = R.dimen.text_body_large).toSp() },
                    textAlign = TextAlign.Start,
                    color = LocalCustomColorsPalette.current.textColor
                )
            }
        )
    }
}

@Composable
fun AccountCardWithCheckbox(
    account: Account,
    currencyCode: String,
    accountsViewModel: AccountsViewModel,
    searchViewModel: SearchViewModel,
    onCheckBoxChange: (Boolean) -> Unit
) {
    val toDate by searchViewModel.selectedToDate.observeAsState(account.fromDate)
    val fromDate by searchViewModel.selectedFromDate.observeAsState(account.toDate)
    val showDialog by accountsViewModel.enableDialog.observeAsState(false)
    val limitMax by accountsViewModel.limitMax.observeAsState(account.limitMax.toString())
    val messageDateError = stringResource(id = R.string.datefromoverdateto)
    val scope = rememberCoroutineScope()

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardColors(
            containerColor = LocalCustomColorsPalette.current.drawerColor,
            contentColor = LocalCustomColorsPalette.current.incomeColor,
            disabledContainerColor = LocalCustomColorsPalette.current.drawerColor,
            disabledContentColor = LocalCustomColorsPalette.current.incomeColor
        ),
        modifier = Modifier
            .size(width = 360.dp, height = 120.dp)
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = account.name,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.6f),
                textAlign = TextAlign.Start,
                fontSize = 22.sp,
                color = LocalCustomColorsPalette.current.textColor
            )
            Spacer(modifier = Modifier.height(12.dp)) // Espacio entre el texto y el botón
            Text(
                text = Utils.numberFormat(account.balance, currencyCode),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f),
                textAlign = TextAlign.End,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre el texto y el botón
        Row(
            modifier = Modifier.padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if(account.isChecked) stringResource(id = R.string.accountchecked)
                else stringResource(id = R.string.accountunchecked)  ,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.6f),
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                color = LocalCustomColorsPalette.current.textColor
            )
            Checkbox(
                modifier = Modifier.weight(0.2f), // Ajuste proporcional para el checkbox
                checked = account.isChecked,
                onCheckedChange = onCheckBoxChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = LocalCustomColorsPalette.current.drawerColor,
                    uncheckedColor = LocalCustomColorsPalette.current.textColor,
                    checkmarkColor = LocalCustomColorsPalette.current.incomeColor
                )
            )
            if (account.isChecked) {
                ModelDialogWithTextField(
                    account.name,
                    showDialog,
                    limitMax,
                    onValueChange = {accountsViewModel.onChangeLimitMax(it) },
                    onConfirm = {
                        if (!searchViewModel.validateDates()) {
                            scope.launch(Dispatchers.Main) {
                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        messageDateError
                                    )
                                )
                            }
                            accountsViewModel.updateCheckedAccount(account.id,false)
                        } else {
                            accountsViewModel.upDateLimitMaxAccount(
                                account.id,
                                limitMax.toFloatOrNull() ?: 0f
                            )
                            accountsViewModel.onEnableDialogChange(false)
                            accountsViewModel.upDateAccountsDates(account.id, fromDate, toDate)
                        }
                    },
                    onDismiss = { accountsViewModel.onEnableDialogChange(false)
                                accountsViewModel.updateCheckedAccount(account.id,false) }
                    ,searchViewModel)

            }
        }
    }
}
