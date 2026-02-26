package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectcategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.domain.database.categoryusecase.GetAllCategoriesByType
import carnerero.agustin.cuentaappandroid.domain.database.categoryusecase.UpdateCheckedCategoryUseCase
import carnerero.agustin.cuentaappandroid.domain.database.categoryusecase.UpdateFromDateCategoryUseCase
import carnerero.agustin.cuentaappandroid.domain.database.categoryusecase.UpdateSpendingLimitCategoryUseCase
import carnerero.agustin.cuentaappandroid.domain.database.categoryusecase.UpdateToDateCategoryUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.DateField
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.model.DialogUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectcategories.model.SelectCategoriesUiEvent
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectcategories.model.SelectCategoriesUiState
import carnerero.agustin.cuentaappandroid.utils.Utils
import carnerero.agustin.cuentaappandroid.utils.dateFormatByLocale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SelectCategoriesSpendingControlViewModel @Inject constructor(
    private val getAllCategoriesByType: GetAllCategoriesByType,
    private val upDateSpendingLimit: UpdateSpendingLimitCategoryUseCase,
    private val upDateFromDate: UpdateFromDateCategoryUseCase,
    private val upDateToDate: UpdateToDateCategoryUseCase,
    private val upDateCheckedCategory: UpdateCheckedCategoryUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(SelectCategoriesUiState())
    val uiState: StateFlow<SelectCategoriesUiState> = _uiState


    init {
        observeInitialData()
    }

    private fun observeInitialData() {
        viewModelScope.launch {
            getAllCategoriesByType.invoke(CategoryType.EXPENSE)
                .collect { categories ->
                    _uiState.update { current ->
                        current.copy(
                            categories = categories
                        )
                    }
                }
        }
        }

    fun onUserEvent(event: SelectCategoriesUiEvent){
        when(event){
            is SelectCategoriesUiEvent.OnConfirm -> onConfirm()
            is SelectCategoriesUiEvent.OnSelectDate -> onSelectedDate(event.field,event.date)
            is SelectCategoriesUiEvent.OnShowDatePicker -> onShowDatePicker(event.field,event.visible)
            is SelectCategoriesUiEvent.OnSpendLimitChange -> onSpendingLimitChange(event.newSpendLimit)
            is SelectCategoriesUiEvent.OnCheckedChange -> onCheckBoxChange(event.categoryId,event.newValue)
            is SelectCategoriesUiEvent.OnOpenDialog -> openDialog(event.category)
            is SelectCategoriesUiEvent.OnCloseDialog -> closeDialog()
        }
    }

    fun onCheckBoxChange(categoryId: Int, newValue: Boolean) {
        viewModelScope.launch {
            upDateCheckedCategory(categoryId, newValue)
            if(!newValue){
                upDateSpendingLimit(categoryId, BigDecimal.ZERO)
            }
        }
    }
    fun onShowDatePicker(field: DateField, visible: Boolean) {
        _uiState.update { current ->
            val updatedDates = (current.dialogUiState).copy(
                showFromDatePicker = if (field == DateField.FROM) visible else current.dialogUiState.showFromDatePicker,
                showToDatePicker = if (field == DateField.TO) visible else current.dialogUiState.showToDatePicker
            )
            current.copy(dialogUiState = updatedDates)
        }
    }

    fun onSpendingLimitChange(newValue:String){
            _uiState.update { current ->
                current.copy(
                    dialogUiState = current.dialogUiState.copy(
                        spendLimit =newValue
                    )
                )
            }
    }


    fun onSelectedDate(field: DateField, date: String) {
            _uiState.update { current ->
                val updatedDates = (current.dialogUiState).copy(
                    fromDate = if (field == DateField.FROM) date else current.dialogUiState.fromDate,
                    toDate = if (field == DateField.TO) date else current.dialogUiState.toDate
                )
                current.copy(dialogUiState = updatedDates)
            }
    }

    fun onConfirm() {
        val dialog = _uiState.value.dialogUiState
        val categoryId = dialog.categoryId

        viewModelScope.launch {
            upDateSpendingLimit(categoryId, dialog.spendLimit.toBigDecimalOrNull() ?: BigDecimal.ZERO)
            upDateFromDate(categoryId, dialog.fromDate)
            upDateToDate(categoryId, dialog.toDate)
            //onShowDialog(categoryId, false)
            closeDialog()
        }
    }
    fun openDialog(category: Category) {
        _uiState.update { current ->
            current.copy(
                dialogUiState = current.dialogUiState.copy(
                    categoryId = category.id,
                    showDialog = true,
                    spendLimit = category.spendingLimit.toPlainString(),
                    fromDate = category.fromDate,
                    toDate = category.toDate
                )
            )
        }
    }
    fun closeDialog() {
        _uiState.update { current ->
            current.copy(
                dialogUiState = DialogUiState()
            )
        }
    }
}

