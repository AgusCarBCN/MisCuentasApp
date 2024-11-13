package carnerero.agustin.cuentaappandroid.newamount.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.components.CategoryEntries
import carnerero.agustin.cuentaappandroid.components.HeadSetting
import carnerero.agustin.cuentaappandroid.createaccounts.view.CategoriesViewModel
import carnerero.agustin.cuentaappandroid.main.data.database.entities.CategoryType
import carnerero.agustin.cuentaappandroid.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.main.view.MainViewModel


@Composable

fun CategorySelector(mainViewModel: MainViewModel, categoriesViewModel: CategoriesViewModel, type: CategoryType) {


    val listOfCategories by categoriesViewModel.listOfCategories.observeAsState(listOf())

    LaunchedEffect(type) {
        categoriesViewModel.getAllCategoriesByType(type)
    }


        HeadSetting(
            title = (if (type == CategoryType.INCOME) stringResource(id = R.string.chooseincome) else stringResource(
                id = R.string.chooseexpense
            )), androidx.compose.material3.MaterialTheme.typography.headlineSmall

        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            items(listOfCategories.size) { index ->
                CategoryEntries(listOfCategories[index],
                    modifier = Modifier
                        .padding(10.dp),
                    onClickItem = {
                        mainViewModel.selectScreen(IconOptions.NEW_AMOUNT)
                        categoriesViewModel.onCategorySelected(listOfCategories[index])
                    })
            }
        }
    }


