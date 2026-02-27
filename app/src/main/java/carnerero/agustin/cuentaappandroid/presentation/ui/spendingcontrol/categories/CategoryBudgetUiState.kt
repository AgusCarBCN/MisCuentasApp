package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.categories

import java.math.BigDecimal


data class CategoryBudgetUiState(
    val expenses: BigDecimal = BigDecimal.ZERO,
    val spendingPercentage: Float = 0f,
    val spendingPercent: Int = 0
)
