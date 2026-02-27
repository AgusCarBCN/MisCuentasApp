package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.domain.database.categoryusecase.GetAllCategoriesByType
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.GetSumOfExpensesByCategoryUseCase
import carnerero.agustin.cuentaappandroid.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.categories.CategoriesUiState
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.categories.CategoryBudgetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.roundToInt


@HiltViewModel
class CategoriesSpendingControlViewModel @Inject constructor(
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val getAllCategoriesByType: GetAllCategoriesByType,
    private val getSumExpensesByCategory: GetSumOfExpensesByCategoryUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState


    init {
        observeInitialData()
    }

    private fun observeInitialData() {
        viewModelScope.launch {
            val currencyCode=getCurrencyCode.invoke()
            getAllCategoriesByType.invoke(CategoryType.EXPENSE)
                .collect { categories ->
                    _uiState.update { current ->
                        current.copy(
                            categories= categories,
                            currencyCode=currencyCode,
                        )
                    }
                }
        }
    }
    fun observeCategorySpending(category: Category) {
        viewModelScope.launch {
            getSumExpensesByCategory
                .invoke(category.id)
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
                    updatedMap[category.id] = CategoryBudgetUiState(
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