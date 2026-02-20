package carnerero.agustin.cuentaappandroid.presentation.ui.home

import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.data.db.entities.Entry
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordsFilter
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal


data class HomeUiState(
    val accounts: List<Account> = emptyList(),
    val totalIncomes: BigDecimal = BigDecimal.ZERO,
    val totalExpenses: BigDecimal = BigDecimal.ZERO,
    val filter: RecordsFilter= RecordsFilter.Expenses,
    val route:String="",
    val currencyCode: String = "EUR"
)

