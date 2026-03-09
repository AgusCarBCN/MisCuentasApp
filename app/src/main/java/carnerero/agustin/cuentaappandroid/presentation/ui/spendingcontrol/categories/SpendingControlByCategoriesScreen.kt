package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.categories


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.SpacerApp

@Composable
fun SpendingControlByCategoriesScreen(
    categoriesViewModel: CategoriesSpendingControlViewModel
) {
    val state by categoriesViewModel.uiState.collectAsStateWithLifecycle()
    val categoriesChecked = state.categories.filter { it.isChecked }

    // Solo observar cuando cambian las categorías seleccionadas
    LaunchedEffect(categoriesChecked.map { it.id }) {
        categoriesChecked.forEach { category ->
            categoriesViewModel.observeCategorySpending(category)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 16.dp)
        ) {
            items(categoriesChecked, key = { it.id }) { category ->

                val categoryBudget = state.categoryBudgetMap[category.id]

                CategoryBudgetItemControl(
                    category = category,
                    currencyCode = state.currencyCode,
                    uiState = categoryBudget
                )

                SpacerApp()
            }
        }
    }
}

