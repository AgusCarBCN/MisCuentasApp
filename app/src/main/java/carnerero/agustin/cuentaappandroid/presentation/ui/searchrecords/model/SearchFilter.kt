package carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model

import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.math.BigDecimal
import java.util.Date

data class SearchFilter(
    val accountId: Int,
    val description: String? = null,
    val dateFrom: String= Date().dateFormat(),
    val dateTo: String=Date().dateFormat(),
    val amountMin: BigDecimal = BigDecimal.ZERO,
    val amountMax: BigDecimal = BigDecimal("1E10"),
    val selectedOption: TransactionType = TransactionType.ALL
)
