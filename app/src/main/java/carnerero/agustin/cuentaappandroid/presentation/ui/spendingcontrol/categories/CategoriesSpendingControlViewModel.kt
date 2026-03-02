package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.domain.database.categoryusecase.GetAllCategoriesByType
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetSumOfExpensesByCategoryAndDateUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.model.BudgetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.roundToInt


@HiltViewModel
class CategoriesSpendingControlViewModel @Inject constructor(
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val getAllCategoriesByType: GetAllCategoriesByType,
    private val getSumExpensesByCategory: GetSumOfExpensesByCategoryAndDateUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState


    init {
        observeInitialData()
    }
    private fun observeInitialData() {
        viewModelScope.launch {
            combine(
                getAllCategoriesByType(CategoryType.EXPENSE),
                getCurrencyCode()
            ) { categories, currencyCode ->
                _uiState.value.copy(
                    categories = categories,
                    currencyCode = currencyCode
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun observeCategorySpending(category: Category) {
        viewModelScope.launch {
            getSumExpensesByCategory
                .invoke(category.id,category.fromDate,category.toDate)
                .collect { expense ->

                    val safeExpense = expense?: BigDecimal.ZERO
                    val limit = category.spendingLimit

                    val percentage = if (limit > BigDecimal.ZERO) {
                        abs(safeExpense.toFloat() / limit.toFloat())
                            .coerceIn(0f, 1f)
                    } else {
                        0f
                    }

                    val percent = (percentage * 100).roundToInt()

                    // 1️⃣ Crear un mapa mutable temporal
                    val updatedMap = _uiState.value.categoryBudgetMap.toMutableMap()

                    // 2️⃣ Añadir/actualizar la categoría
                    updatedMap[category.id] = BudgetUiState(
                        expenses = safeExpense,
                        spendingPercentage = percentage,
                        spendingPercent = percent
                    )

                    // 3️⃣ Actualizar el StateFlow
                    _uiState.update { current ->
                        current.copy(categoryBudgetMap = updatedMap)
                    }
                }
        }
    }
}