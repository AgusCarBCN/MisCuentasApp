package carnerero.agustin.cuentaappandroid.presentation.common.model

data class CategoryItem(
val id: Int,
val nameKey: String,      // "food", "transport", etc.
val nameRes: Int)