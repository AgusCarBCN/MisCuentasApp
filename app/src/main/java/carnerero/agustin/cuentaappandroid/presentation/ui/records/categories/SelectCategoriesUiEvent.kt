package carnerero.agustin.cuentaappandroid.presentation.ui.records.categories

import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType

sealed class SelectCategoriesUiEvent {
    data class OnShowCategoriesChange(val type: CategoryType) : SelectCategoriesUiEvent()
    data class OnCategorySelected(val category: Category): SelectCategoriesUiEvent()
}