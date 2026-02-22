package carnerero.agustin.cuentaappandroid.presentation.ui.records.categories

sealed class SelectCategoriesEffects {
   object Idle: SelectCategoriesEffects()
   object OnNavToAddRecordsScreen: SelectCategoriesEffects()
}