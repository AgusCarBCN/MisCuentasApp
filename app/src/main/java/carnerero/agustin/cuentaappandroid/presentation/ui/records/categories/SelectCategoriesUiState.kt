package carnerero.agustin.cuentaappandroid.presentation.ui.records.categories

import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType

data class SelectCategoriesUiState(
    val categories: List<Category> =emptyList(),
    val categorySelected: Category = Category(
        type = CategoryType.EMPTY,
        iconResource = 0,
        nameResource = 0
    )
)
