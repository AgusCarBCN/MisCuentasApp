package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectcategories.model

import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.model.DialogUiState

data class SelectCategoriesUiState(
    val dialogUiState: DialogUiState= DialogUiState(),
    val categories: List<Category> = emptyList()

)
