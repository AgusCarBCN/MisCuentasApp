package carnerero.agustin.cuentaappandroid.presentation.ui.records.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.domain.database.categoryusecase.GetAllCategoriesByType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCategoriesViewModel @Inject constructor(
    private val getAllCategoriesByType: GetAllCategoriesByType
): ViewModel() {

    private val _uiState = MutableStateFlow(SelectCategoriesUiState())
    val uiState: StateFlow<SelectCategoriesUiState> = _uiState

    private val _effect = MutableSharedFlow<SelectCategoriesEffects>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val effect = _effect.asSharedFlow()

    fun onUserEvent(event: SelectCategoriesUiEvent){
        when(event){
            is SelectCategoriesUiEvent.OnShowCategoriesChange -> {
                showCategories(event.type)
            }
            is SelectCategoriesUiEvent.OnCategorySelected -> {
                onSelectCategory(event.category)
            }
        }
    }

    fun onSelectCategory(category: Category){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    categorySelected = category
                )
            }
            _effect.emit(SelectCategoriesEffects.OnNavToAddRecordsScreen)

        }
    }
    fun showCategories(type: CategoryType) {
        viewModelScope.launch {
            getAllCategoriesByType.invoke(type)
                .collect { categories ->
                    _uiState.update { current ->
                        current.copy(
                            categories = categories
                        )
                    }
                }
        }
    }
}