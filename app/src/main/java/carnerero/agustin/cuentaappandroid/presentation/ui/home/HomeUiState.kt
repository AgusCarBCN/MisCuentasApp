package carnerero.agustin.cuentaappandroid.presentation.ui.home

import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.data.db.entities.Entry
import java.math.BigDecimal


data class HomeUiState(
    val accounts: List<Account> = emptyList(),
    val listOfRecords:List<EntryDTO> = emptyList(),
    val totalIncomes: BigDecimal = BigDecimal.ZERO,
    val totalExpenses: BigDecimal = BigDecimal.ZERO,
    val showEntries:Boolean=false,
    val enableByDate: Boolean=false,
    val currencyCode: String = "EUR"
)

