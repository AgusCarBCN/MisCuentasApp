package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectcategories.model

import androidx.webkit.internal.ApiFeature
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField
import java.time.LocalDate

sealed class SelectCategoriesUiEvent {

    object OnConfirm : SelectCategoriesUiEvent()

    data class OnSpendLimitChange(val newSpendLimit:String) : SelectCategoriesUiEvent()

    data class OnCheckedChange(val categoryId:Int,val newValue:Boolean) : SelectCategoriesUiEvent()

    data class OnOpenDialog(val category: Category) : SelectCategoriesUiEvent()

    object OnCloseDialog : SelectCategoriesUiEvent()

    data class OnShowDatePicker(
        val field: DateField,
        val visible: Boolean
    ) : SelectCategoriesUiEvent()

    data class OnSelectDate(
        val field: DateField,
        val date: String
    ) : SelectCategoriesUiEvent()
}