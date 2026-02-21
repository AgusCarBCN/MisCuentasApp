package carnerero.agustin.cuentaappandroid.presentation.ui.records.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.CategoryEntries
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType


@Composable
fun SelectCategoriesScreen(
    selectCategoriesViewModel: SelectCategoriesViewModel,
    type: CategoryType
) {
    val state by selectCategoriesViewModel.uiState.collectAsStateWithLifecycle()
    val categories=state.categories
    LaunchedEffect(type) {
        selectCategoriesViewModel.showCategories(type)
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
            items(categories.size) { index ->
                CategoryEntries(
                    category = categories[index],
                    modifier = Modifier.padding(10.dp),
                    onClickItem = {
                        selectCategoriesViewModel.onUserEvent(SelectCategoriesUiEvent.OnCategorySelected(categories[index]))
                        //categoriesViewModel.onCategorySelected(listOfCategories[index])
                        //navToNewEntry()
                    }
                )
            }
        }
    }
}
