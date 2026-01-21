package carnerero.agustin.cuentaappandroid.data.repository


import carnerero.agustin.cuentaappandroid.data.db.dao.EntryDao
import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Entry
import java.math.BigDecimal
import javax.inject.Inject

class EntryRepository @Inject constructor(private val entryDao: EntryDao) {

    class EntryRepository(
        private val entryDao: EntryDao
    ) {
        /* =====================
           CRUD
           ===================== */
        suspend fun insertEntry(entry: Entry) =
            entryDao.insertEntry(entry)

        suspend fun deleteEntry(entry: Entry) =
            entryDao.deleteEntry(entry)

        suspend fun updateEntry(entry: Entry) =
            entryDao.updateEntry(entry)

        suspend fun updateEntryFields(
            id: Long,
            description: String,
            amount: BigDecimal,
            date: String
        ) = entryDao.updateEntryFields(id, description, amount, date)

        suspend fun getEntryById(entryId: Long): Entry? =
            entryDao.getEntryById(entryId)

        suspend fun getAllEntries(): List<Entry> =
            entryDao.getAllEntries()

        /* =====================
           CONSULTAS SIMPLES
           ===================== */

        suspend fun getAllIncomes(): List<Entry> =
            entryDao.getAllIncomes()

        suspend fun getAllExpenses(): List<Entry> =
            entryDao.getAllExpenses()

        suspend fun getEntriesByCategory(categoryId: Int): List<Entry> =
            entryDao.getEntriesByCategory(categoryId)

        /* =====================
           LÓGICA FINANCIERA
           ===================== */

        suspend fun getTotalIncome(): BigDecimal =
            entryDao.getAllIncomes()
                .fold(BigDecimal.ZERO) { acc, entry -> acc + entry.amount }

        suspend fun getTotalExpense(): BigDecimal =
            entryDao.getAllExpenses()
                .fold(BigDecimal.ZERO) { acc, entry -> acc + entry.amount }

        suspend fun getTotalExpenseByCategory(categoryId: Int): BigDecimal =
            entryDao.getAllExpenses()
                .filter { it.categoryId == categoryId }
                .fold(BigDecimal.ZERO) { acc, entry -> acc + entry.amount }

        suspend fun getIncomeByDateRange(
            accountId: Int,
            from: String,
            to: String
        ): BigDecimal =
            entryDao.getAllIncomes()
                .filter {
                    it.accountId == accountId &&
                            it.date >= from &&
                            it.date <= to
                }
                .fold(BigDecimal.ZERO) { acc, entry -> acc + entry.amount }

        suspend fun getExpenseByDateRange(
            accountId: Int,
            from: String,
            to: String
        ): BigDecimal =
            entryDao.getAllExpenses()
                .filter {
                    it.accountId == accountId &&
                            it.date >= from &&
                            it.date <= to
                }
                .fold(BigDecimal.ZERO) { acc, entry -> acc + entry.amount }

        /* =====================
           OPERACIONES MASIVAS
           ===================== */

        suspend fun applyExchangeRate(rate: BigDecimal) {
            val entries = entryDao.getAllEntries()
            entries.forEach { entry ->
                val newAmount = entry.amount.multiply(rate)
                entryDao.updateEntryFields(
                    entry.id,
                    entry.description,
                    newAmount,
                    entry.date
                )
            }
        }

        /* =====================
           DTOs PARA UI
           ===================== */

        suspend fun getAllEntriesDTO(): List<EntryDTO> =
            entryDao.getAllEntriesDTO()

        suspend fun getAllIncomesDTO(): List<EntryDTO> =
            entryDao.getAllIncomesDTO()

        suspend fun getAllExpensesDTO(): List<EntryDTO> =
            entryDao.getAllExpensesDTO()
    }

}
