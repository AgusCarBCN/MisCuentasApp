package carnerero.agustin.cuentaappandroid.data.repository


import carnerero.agustin.cuentaappandroid.data.db.dao.EntryDao
import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Entry
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import carnerero.agustin.cuentaappandroid.utils.mapper.EntryMapper
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
        suspend fun getSumIncomes(): BigDecimal =
            entryDao.getSumOfIncomeEntries() ?: BigDecimal.ZERO

        suspend fun getSumExpenses(): BigDecimal =
            entryDao.getSumOfExpenseEntries() ?: BigDecimal.ZERO

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

        // 3️⃣ Listar entradas
        suspend fun getAllEntries(): List<Entry> = entryDao.getAllEntries()
        suspend fun getAllEntriesDTO(): List<EntryDTO> = entryDao.getAllEntriesDTO()
        suspend fun getAllIncomesDTO(): List<EntryDTO> = entryDao.getAllIncomesDTO()
        suspend fun getAllExpensesDTO(): List<EntryDTO> = entryDao.getAllExpensesDTO()
        suspend fun getAllEntriesByAccount(accountId: Int): List<EntryDTO> =
            entryDao.getAllEntriesByAccountDTO(accountId)

        // 4️⃣ Filtrado de entradas
        suspend fun getEntriesFiltered(
            accountId: Int,
            descriptionAmount: String,
            fromDate: String = Date().dateFormat(),
            toDate: String = Date().dateFormat(),
            amountMin: BigDecimal = BigDecimal.ZERO,
            amountMax: BigDecimal = BigDecimal("1E10"), // máximo arbitrario
            selectedOptions: Int = 0
        ): List<EntryDTO> = entryDao.getFilteredEntriesDTO(
            accountId,
            descriptionAmount,
            fromDate,
            toDate,
            amountMin,
            amountMax,
            selectedOptions
        )
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

        suspend fun updateEntry(id: Long, description: String,newAmount: BigDecimal,date: String,) {
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



