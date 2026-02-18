package carnerero.agustin.cuentaappandroid.presentation.ui.search.model

import java.math.BigDecimal

data class SearchFilter(
    val accountId: Int,
    val description: String? = null,
    val dateFrom: String,
    val dateTo: String,
    val amountMin: BigDecimal = BigDecimal.ZERO,
    val amountMax: BigDecimal = BigDecimal("1E10"),
    val selectedOption: TransactionType = TransactionType.ALL
)
