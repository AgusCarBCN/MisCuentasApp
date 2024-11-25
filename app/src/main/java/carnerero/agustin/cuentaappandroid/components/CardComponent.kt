package carnerero.agustin.cuentaappandroid.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import coil.compose.rememberAsyncImagePainter
import carnerero.agustin.cuentaappandroid.SnackBarController
import carnerero.agustin.cuentaappandroid.SnackBarEvent
import carnerero.agustin.cuentaappandroid.createaccounts.view.AccountsViewModel
import carnerero.agustin.cuentaappandroid.createaccounts.view.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.main.data.database.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Account
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Category
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Entry
import carnerero.agustin.cuentaappandroid.search.SearchViewModel
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
import carnerero.agustin.cuentaappandroid.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable

fun UserImage(uri: Uri, size: Int) {
    val profileImage= stringResource(id = R.string.profileimage)
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
            contentDescription = profileImage,
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
            style=MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(10.dp))
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
                    textAlign = TextAlign.Center,
                    color = LocalCustomColorsPalette.current.textColor,
                    style = MaterialTheme.typography.bodyLarge
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
    val contentText=stringResource(textButton)
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
                color = LocalCustomColorsPalette.current.textColor,
                style=MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(12.dp)) // Espacio entre el texto y el botón
            Text(
                text = Utils.numberFormat(account.balance, currencyCode),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f),
                textAlign = TextAlign.End,
                style=MaterialTheme.typography.headlineSmall
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
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "$contentText ${account.name}"
                        },
                    text = stringResource(id = textButton),
                    textAlign = TextAlign.Start,
                    color = LocalCustomColorsPalette.current.textColor,
                    style=MaterialTheme.typography.bodyLarge
                )
            }
        )
    }
}

@Composable
fun CategoryCardWithCheckbox(category: Category,
                             categoriesViewModel: CategoriesViewModel,
                             searchViewModel: SearchViewModel,
                             onCheckBoxChange: (Boolean) -> Unit
                             )
{
    val toDate by searchViewModel.selectedToDate.observeAsState(category.fromDate)
    val fromDate by searchViewModel.selectedFromDate.observeAsState(category.toDate)
    val showDialog by categoriesViewModel.enableDialog.observeAsState(false)
    val spendingLimit by categoriesViewModel.spendingLimit.observeAsState(category.spendingLimit.toString())
    val messageDateError = stringResource(id = R.string.datefromoverdateto)
    val scope = rememberCoroutineScope()
    val categoryName= stringResource(category.nameResource)
    val checked=stringResource(R.string.ischecked)
    val unchecked = stringResource(R.string.isunchecked)
    val item= stringResource(id = R.string.item)
    val iconItem= stringResource(id = R.string.itemicon)

    ElevatedCard(

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardColors(
            containerColor = LocalCustomColorsPalette.current.drawerColor,
            contentColor = LocalCustomColorsPalette.current.incomeColor,
            disabledContainerColor = LocalCustomColorsPalette.current.drawerColor,
            disabledContentColor = LocalCustomColorsPalette.current.incomeColor
        ),   modifier = Modifier
            .size(width = 360.dp, height = 80.dp)

    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 10.dp, start = 10.dp)
                .semantics {
                    contentDescription = "$item $categoryName"
                },
            horizontalArrangement = Arrangement.SpaceBetween, // Cambia a SpaceBetween
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Contenedor para el icono y el texto para que se agrupen en un extremo
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = category.iconResource),
                    contentDescription = "$iconItem $categoryName",
                    modifier = Modifier
                        .size(42.dp)
                        .padding(end = 10.dp),
                    tint = LocalCustomColorsPalette.current.textHeadColor
                )
                Column {
                    Text(
                        text = stringResource(id = category.nameResource),
                        modifier = Modifier.padding(bottom = 5.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalCustomColorsPalette.current.textColor
                    )
                    Text(
                        text = if (category.isChecked) stringResource(id = R.string.categorychecked)

                        else stringResource(id = R.string.categoryunchecked),
                        modifier = Modifier.semantics {

                            contentDescription = if(category.isChecked) "$categoryName $checked"
                            else "$categoryName $unchecked"
                        },
                        color = LocalCustomColorsPalette.current.textColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Checkbox al extremo opuesto
            Checkbox(modifier = Modifier.semantics {
                // Descripción única del Checkbox
                contentDescription = "${category.nameResource}: ${if (category.isChecked) checked else unchecked}"
            },
                checked = category.isChecked,
                onCheckedChange = onCheckBoxChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = LocalCustomColorsPalette.current.drawerColor,
                    uncheckedColor = LocalCustomColorsPalette.current.textColor,
                    checkmarkColor = LocalCustomColorsPalette.current.incomeColor
                )
            )
        }

        if (category.isChecked) {
                ModelDialogWithTextField(
                    stringResource(category.nameResource),
                    showDialog,
                    spendingLimit,
                    onValueChange = {categoriesViewModel.onChangeSpendingLimit(it) },
                    onConfirm = {
                        if (!searchViewModel.validateDates()) {
                            scope.launch(Dispatchers.Main) {
                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        messageDateError
                                    )
                                )
                            }
                            categoriesViewModel.updateCheckedCategory(category.id,false)
                        } else {
                            categoriesViewModel.upDateSpendingLimitCategory(
                                category.id,
                                spendingLimit.toDouble()
                            )
                            categoriesViewModel.onEnableDialogChange(false)
                            categoriesViewModel.upDateCategoryDates(category.id, fromDate, toDate)
                        }
                    },
                    onDismiss = { categoriesViewModel.onEnableDialogChange(false)
                        categoriesViewModel.updateCheckedCategory(category.id,false) }
                    ,searchViewModel)

            }
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
    val spendingLimit by accountsViewModel.spendingLimit.observeAsState(account.spendingLimit.toString())
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
                style=MaterialTheme.typography.headlineSmall,
                color = LocalCustomColorsPalette.current.textColor
            )
            Spacer(modifier = Modifier.height(12.dp)) // Espacio entre el texto y el botón
            Text(
                text = Utils.numberFormat(account.balance, currencyCode),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f),
                textAlign = TextAlign.End,
                style=MaterialTheme.typography.headlineSmall
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
                style = MaterialTheme.typography.bodyMedium,
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
                    spendingLimit,
                    onValueChange = {accountsViewModel.onChangeSpendingLimit(it) },
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
                            accountsViewModel.upDateSpendingLimitAccount(
                                account.id,
                                spendingLimit.toDouble()
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
@Composable
fun EntryCardWithCheckBox(
    entry: EntryDTO,
    currencyCode: String,
    isChecked:Boolean,
    onSelectionChange: () -> Unit
){

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
            .padding(bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 15.dp, end = 20.dp, top = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.description,
                modifier = Modifier
                    .weight(0.6f),
                textAlign = TextAlign.Start,
                style=MaterialTheme.typography.bodyLarge,
                color = LocalCustomColorsPalette.current.textHeadColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = Utils.numberFormat(entry.amount, currencyCode),
                modifier = Modifier
                    .weight(0.4f),
                color = if (entry.amount >= 0) LocalCustomColorsPalette.current.incomeColor
                else LocalCustomColorsPalette.current.expenseColor,
                textAlign = TextAlign.End,
                style=MaterialTheme.typography.bodyLarge
            )

        }
        Row(
            modifier = Modifier.padding(start = 15.dp, end = 20.dp, top = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = entry.iconResource),
                contentDescription = null,
                tint = LocalCustomColorsPalette.current.textColor
            )
            Spacer(modifier = Modifier.height(20.dp)) // Espacio entre el texto y el botón
            Text(
                text = stringResource(id = entry.nameResource),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f)
                ,
                color = LocalCustomColorsPalette.current.textColor,
                textAlign = TextAlign.Start,
                style=MaterialTheme.typography.bodyLarge

            )
            Checkbox(
                modifier = Modifier.weight(0.2f), // Ajuste proporcional para el checkbox
                checked = isChecked,
                onCheckedChange = {
                    onSelectionChange()
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = LocalCustomColorsPalette.current.drawerColor,
                    uncheckedColor = LocalCustomColorsPalette.current.textColor,
                    checkmarkColor = LocalCustomColorsPalette.current.incomeColor
                )
            )
        }
    }
}
@Composable
fun EntryCardWithIcon(
    entry: EntryDTO,
    currencyCode: String
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
            .padding(bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 15.dp, end = 20.dp, top = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.description,
                modifier = Modifier
                    .weight(0.6f),
                textAlign = TextAlign.Start,
                style=MaterialTheme.typography.bodyLarge,
                color = LocalCustomColorsPalette.current.textHeadColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = Utils.numberFormat(entry.amount, currencyCode),
                modifier = Modifier
                    .weight(0.4f),
                color = if (entry.amount >= 0) LocalCustomColorsPalette.current.incomeColor
                else LocalCustomColorsPalette.current.expenseColor,
                textAlign = TextAlign.End,
                style=MaterialTheme.typography.bodyLarge
            )

        }
        Row(
            modifier = Modifier.padding(start = 15.dp, end = 20.dp, top = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = entry.iconResource),
                contentDescription = null,
                tint = LocalCustomColorsPalette.current.textColor
            )
            Spacer(modifier = Modifier.height(20.dp)) // Espacio entre el texto y el botón
            Text(
                text = stringResource(id = entry.nameResource),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.4f)
                ,
                color = LocalCustomColorsPalette.current.textColor,
                textAlign = TextAlign.Start,
                style=MaterialTheme.typography.bodyLarge
            )
            Icon(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "indicator",
                tint = LocalCustomColorsPalette.current.textColor,
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        // To modify Screen
                    }
            )



        }
    }

}
