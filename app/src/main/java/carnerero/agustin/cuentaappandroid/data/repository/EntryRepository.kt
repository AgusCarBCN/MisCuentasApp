package carnerero.agustin.cuentaappandroid.data.repository


import carnerero.agustin.cuentaappandroid.data.db.dao.EntryDao
import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Entry
import carnerero.agustin.cuentaappandroid.presentation.ui.search.model.SearchFilter
import carnerero.agustin.cuentaappandroid.presentation.ui.search.model.TransactionType
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import carnerero.agustin.cuentaappandroid.utils.mapper.EntryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.util.Date
import javax.inject.Inject

class EntryRepository @Inject constructor(
    private val entryDao: EntryDao,
    private val entryMapper: EntryMapper
) {

        // 1️⃣ Insert entry
        suspend fun insertEntry(entry: Entry) {
            entryDao.insertEntry(entry)
        }

        suspend fun insertEntryDTO(entryDTO: EntryDTO) {
            val entry = entryMapper.entryDtoToEntry(entryDTO)
            entryDao.insertEntry(entry)
        }

        // 2️⃣ Get sum of incomes and expenses
         fun getSumIncomes(): Flow<BigDecimal> =
            entryDao.getSumOfIncomeEntries()
                .map { it ?: BigDecimal.ZERO }

         fun getSumExpenses(): Flow<BigDecimal> =
        entryDao.getSumOfExpenseEntries()
            .map { it ?: BigDecimal.ZERO }

        suspend fun getSumOfExpensesEntriesByCategory(categoryId: Int): BigDecimal =
            entryDao.getSumOfExpenseByCategory(categoryId) ?: BigDecimal.ZERO

        suspend fun getSumOfExpensesByCategoryAndDate(categoryId: Int, fromDate: String, toDate: String): BigDecimal =
            entryDao.getSumOfExpensesByCategoryAndDate(categoryId, fromDate, toDate) ?: BigDecimal.ZERO

        suspend fun getSumIncomesByDate(accountId: Int, fromDate: String = Date().dateFormat(), toDate: String = Date().dateFormat()): BigDecimal =
            entryDao.getSumOfIncomeEntriesByDate(accountId, fromDate, toDate) ?: BigDecimal.ZERO

        suspend fun getSumExpensesByDate(accountId: Int, fromDate: String = Date().dateFormat(), toDate: String = Date().dateFormat()): BigDecimal =
            entryDao.getSumOfExpensesEntriesByDate(accountId, fromDate, toDate) ?: BigDecimal.ZERO

        suspend fun getSumOfIncomeEntriesForMonth(accountId: Int, month: String, year: String): BigDecimal =
            entryDao.getSumOfIncomeEntriesForMonth(accountId, month, year) ?: BigDecimal.ZERO

        suspend fun getSumOfExpensesEntriesForMonth(accountId: Int, month: String, year: String): BigDecimal =
            entryDao.getSumOfExpenseEntriesForMonth(accountId, month, year) ?: BigDecimal.ZERO

        fun getAllEntriesDTO(): Flow<List<EntryDTO>> = entryDao.getAllEntriesDTO()
        fun getAllIncomesDTO(): Flow<List<EntryDTO>> = entryDao.getAllIncomesDTO()
        fun getAllExpensesDTO(): Flow<List<EntryDTO>> = entryDao.getAllExpensesDTO()
        fun getAllEntriesByAccount(accountId: Int): Flow<List<EntryDTO>> =
            entryDao.getAllEntriesByAccountDTO(accountId)

        // 4️⃣ Filtrado de entradas
        fun getEntriesFiltered(filter: SearchFilter): Flow<List<EntryDTO>> {

            val searchPattern = filter.description
                ?.takeIf { it.isNotBlank() }
                ?.let { "%$it%" }

            return entryDao
                .getEntriesBasic(
                    accountId = filter.accountId,
                    descriptionAmount = searchPattern,
                    dateFrom = filter.dateFrom,
                    dateTo = filter.dateTo
                )
                .map { entries ->

                    entries.filter { entry ->

                        val amount = entry.amount
                        val absAmount = amount.abs()

                        val amountOk =
                            absAmount >= filter.amountMin &&
                                    absAmount <= filter.amountMax

                        val typeOk = when (filter.selectedOption) {
                            TransactionType.INCOME -> amount >= BigDecimal.ZERO
                            TransactionType.EXPENSE -> amount < BigDecimal.ZERO
                            TransactionType.ALL -> true
                        }

                        amountOk && typeOk
                    }
                }
        }


    suspend fun getEntriesByDate(accountId:Int,
                                 fromDate: String=Date().dateFormat(),
                                 toDate: String=Date().dateFormat()
    ):List<EntryDTO> =entryDao.getAllEntriesByDateDTO(accountId,
        fromDate,
        toDate)

        // 5️⃣ Actualizar montos
        suspend fun updateEntriesAmountByExchangeRate(rate: BigDecimal) {
            entryDao.updateEntriesAmountByExchangeRate(rate)
        }

        suspend fun updateEntry(id: Long, description: String,newAmount: BigDecimal,date: String) {
            entryDao.updateEntryFields(id,description,newAmount,date)
        }

          suspend fun updateAmountEntry(idAccount:Long,newAmount: BigDecimal){
            entryDao.updateAmountEntry(idAccount,newAmount)
         }

        // 6️⃣ Borrar entradas
        suspend fun deleteEntry(entryDTO: EntryDTO) {
            val entry = entryMapper.entryDtoToEntry(entryDTO)
            entryDao.deleteEntry(entry)
        }

    }



