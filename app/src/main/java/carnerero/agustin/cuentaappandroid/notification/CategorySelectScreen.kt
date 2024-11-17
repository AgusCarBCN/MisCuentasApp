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

            }
        }
    }
}





