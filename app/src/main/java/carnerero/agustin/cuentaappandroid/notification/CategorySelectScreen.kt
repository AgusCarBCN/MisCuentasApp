package carnerero.agustin.cuentaappandroid.notification


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.SnackBarController
import carnerero.agustin.cuentaappandroid.SnackBarEvent
import carnerero.agustin.cuentaappandroid.components.CategoryCardWithCheckbox
import carnerero.agustin.cuentaappandroid.components.HeadSetting
import carnerero.agustin.cuentaappandroid.components.ModelDialogWithTextField
import carnerero.agustin.cuentaappandroid.createaccounts.view.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Category
import carnerero.agustin.cuentaappandroid.main.data.database.entities.CategoryType
import carnerero.agustin.cuentaappandroid.search.SearchViewModel
import carnerero.agustin.cuentaappandroid.theme.LocalCustomColorsPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun EntryCategoryList(
    categoriesViewModel: CategoriesViewModel,
    searchViewModel: SearchViewModel
) {
    // Observa la lista de categorías desde el ViewModel
    val listOfCategories by categoriesViewModel.listOfCategories.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        categoriesViewModel.getAllCategoriesByType(CategoryType.EXPENSE)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadSetting(title = stringResource(id = R.string.selectcategories),
            androidx.compose.material3.MaterialTheme.typography.headlineSmall)
        // Asegúrate de que la LazyColumn ocupa solo el espacio necesario
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Permite que la columna ocupe el espacio disponible
                .padding(bottom = 16.dp), // Espacio en la parte inferior
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(listOfCategories) { category ->
                CategoryCardWithCheckbox(
                    category,
                    categoriesViewModel,
                    searchViewModel,
                    onCheckBoxChange = { checked ->
                        categoriesViewModel.updateCheckedCategory(
                            category.id,
                            checked
                        )
                        if (!category.isChecked) {
                            categoriesViewModel.onEnableDialogChange(true)
                        }
                    }

                )
                Spacer(modifier = Modifier.height(20.dp))
                /* ItemCategoryCheck(
                     category,
                     categoriesViewModel,
                     searchViewModel,
                     onCheckBoxChange = { checked ->
                         categoriesViewModel.updateCheckedCategory(category.id,
                             checked)
                         if(!category.isChecked){
                         categoriesViewModel.onEnableDialogChange(true)
                             }
                     }

                 )*/
            }
        }


    }
}


@Composable
fun ItemCategoryCheck(
    category: Category,
    categoriesViewModel: CategoriesViewModel,
    searchViewModel: SearchViewModel,
    onCheckBoxChange: (Boolean) -> Unit
) {
    val limitMax by categoriesViewModel.limitMax.observeAsState(category.limitMax.toString())
    val toDate by searchViewModel.selectedToDate.observeAsState(category.fromDate)
    val fromDate by searchViewModel.selectedFromDate.observeAsState(category.toDate)
    val showDialog by categoriesViewModel.enableDialog.observeAsState(false)
    val scope = rememberCoroutineScope()
    val messageDateError = stringResource(id = R.string.datefromoverdateto)
    val categoryName = stringResource(id = category.nameResource)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de la categoría
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .weight(0.1f), // Espacio proporcional para el ícono
                painter = painterResource(
                    id = category.iconResource
                ), // Reemplaza con un ícono predeterminado
                contentDescription = "Category icon",
                tint = LocalCustomColorsPalette.current.textColor
            )

            Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el ícono y el texto

            // Nombre de la categoría
            Text(
                text = stringResource(category.nameResource),
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 4.dp),
                color = LocalCustomColorsPalette.current.textColor,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge,

                )

            Checkbox(
                modifier = Modifier.weight(0.2f), // Ajuste proporcional para el checkbox
                checked = category.isChecked,
                onCheckedChange = onCheckBoxChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = LocalCustomColorsPalette.current.backgroundPrimary,
                    uncheckedColor = LocalCustomColorsPalette.current.textColor,
                    checkmarkColor = LocalCustomColorsPalette.current.incomeColor
                )
            )

        }
        if (category.isChecked) {

            ModelDialogWithTextField(
                categoryName,
                showDialog,
                limitMax,
                onValueChange = { categoriesViewModel.onChangeLimitMax(it) },
                onConfirm = {
                    if (!searchViewModel.validateDates()) {
                        scope.launch(Dispatchers.Main) {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    messageDateError
                                )
                            )
                        }
                        categoriesViewModel.updateCheckedCategory(category.id, false)
                    } else {
                        categoriesViewModel.upDateLimitMaxCategory(
                            category.id,
                            limitMax.toFloatOrNull() ?: 0f
                        )
                        categoriesViewModel.onEnableDialogChange(false)
                        categoriesViewModel.upDateCategoryDates(category.id, fromDate, toDate)
                    }
                },
                onDismiss = {
                    categoriesViewModel.onEnableDialogChange(false)
                    categoriesViewModel.updateCheckedCategory(category.id, false)
                }, searchViewModel)


        }
    }
}




