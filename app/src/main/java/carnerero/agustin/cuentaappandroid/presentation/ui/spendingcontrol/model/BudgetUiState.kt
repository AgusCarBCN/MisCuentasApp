package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.model

import java.math.BigDecimal

data class BudgetUiState(
    val expenses: BigDecimal = BigDecimal.ZERO,
    val spendingPercentage: Float = 0f,
    val spendingPercent: Int = 0
)