package carnerero.agustin.cuentaappandroid.notification


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.components.CategoryCardWithCheckbox
import carnerero.agustin.cuentaappandroid.components.HeadSetting
import carnerero.agustin.cuentaappandroid.createaccounts.view.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.main.data.database.entities.CategoryType
import carnerero.agustin.cuentaappandroid.search.SearchViewModel


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

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadSetting(title = stringResource(id = R.string.selectcategoriescontrol),
            androidx.compose.material3.MaterialTheme.typography.titleLarge)
        // Asegúrate de que la LazyColumn ocupa solo el espacio necesario
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Permite que la columna ocupe el espacio disponible
                .padding(bottom = 16.dp)            ,
            verticalArrangement = Arrangement.spacedBy(8.dp), // Espacio entre elementos
            contentPadding = PaddingValues(16.dp) // Padding alrededor del contenido,

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





