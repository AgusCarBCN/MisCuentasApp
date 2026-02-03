package carnerero.agustin.cuentaappandroid.presentation.ui.entries

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.CategoryEntries
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.common.sharedviewmodels.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType


@Composable
fun CategorySelector(
    categoriesViewModel: CategoriesViewModel,
    type: CategoryType,
    navToNewEntry: () -> Unit
) {
    val listOfCategories by categoriesViewModel.listOfCategories.observeAsState(listOf())

    LaunchedEffect(type) {
        categoriesViewModel.getAllCategoriesByType(type)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Título
        HeadSetting(
            title = (if (type == CategoryType.INCOME) stringResource(id = R.string.chooseincome) else stringResource(
                id = R.string.chooseexpense
            )), MaterialTheme.typography.headlineSmall
        )

        // Grid de categorías ocupa el resto del espacio
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            items(listOfCategories.size) { index ->
                CategoryEntries(
                    category = listOfCategories[index],
                    modifier = Modifier.padding(10.dp),
                    onClickItem = {
                        categoriesViewModel.onCategorySelected(listOfCategories[index])
                        navToNewEntry()
                    }
                )
            }
        }
    }
}
