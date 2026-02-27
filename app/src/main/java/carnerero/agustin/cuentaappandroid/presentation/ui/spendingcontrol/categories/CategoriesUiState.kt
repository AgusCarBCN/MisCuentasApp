package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.categories

import carnerero.agustin.cuentaappandroid.data.db.entities.Category


data class CategoriesUiState(
    val categories:List<Category> =emptyList(),
    val currencyCode:String="EUR",
    val categoryBudgetMap: Map<Int, CategoryBudgetUiState> = emptyMap()
)
