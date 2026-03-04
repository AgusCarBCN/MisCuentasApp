package carnerero.agustin.cuentaappandroid.data.repository


import carnerero.agustin.cuentaappandroid.data.db.dao.AccountDao
import carnerero.agustin.cuentaappandroid.data.db.dao.EntryDao
import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Records
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.SearchFilter
import carnerero.agustin.cuentaappandroid.presentation.ui.searchrecords.model.TransactionType
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import carnerero.agustin.cuentaappandroid.utils.mapper.EntryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.util.Date
import javax.inject.Inject

class RecordRepository @Inject constructor(
    private val entryDao: EntryDao,
    private val entryMapper: EntryMapper
) {

        // 1️⃣ Insert entry
        suspend fun insertEntry(record: Records) {
            entryDao.insertEntry(record)
        }

        suspend fun insertEntryDTO(recordDTO: RecordDTO) {
            val entry = entryMapper.entryDtoToEntry(recordDTO)
            entryDao.insertEntry(entry)
        }

        // 2️⃣ Get sum of incomes and expenses
         fun getSumIncomes(): Flow<BigDecimal> =
            entryDao.getSumOfIncomeEntries()
                .map { it ?: BigDecimal.ZERO }

         fun getSumExpenses(): Flow<BigDecimal> =
        entryDao.getSumOfExpenseEntries()
            .map { it ?: BigDecimal.ZERO }

        fun getSumOfExpensesEntriesByCategory(categoryId: Int): Flow<BigDecimal?> =
            entryDao.getSumOfExpenseByCategory(categoryId)

        fun getSumOfExpensesByCategoryAndDate(categoryId: Int, fromDate: String, toDate: String): Flow<BigDecimal?> =
            entryDao.getSumOfExpensesByCategoryAndDate(categoryId, fromDate, toDate)

        fun getSumIncomesByDate(accountId: Int, fromDate: String = Date().dateFormat(), toDate: String = Date().dateFormat()): Flow<BigDecimal?> =
            entryDao.getSumOfIncomeEntriesByDate(accountId, fromDate, toDate)

        fun getSumExpensesByDate(accountId: Int, fromDate: String = Date().dateFormat(), toDate: String = Date().dateFormat()):Flow<BigDecimal?> =
            entryDao.getSumOfExpensesByAccountAndDate(accountId, fromDate, toDate)

        fun getSumOfIncomeEntriesForMonth(accountId: Int, month: String, year: String): Flow<BigDecimal?> =
            entryDao.getSumOfIncomeEntriesForMonth(accountId, month, year)

        fun getSumOfExpensesEntriesForMonth(accountId: Int, month: String, year: String): Flow<BigDecimal?> =
            entryDao.getSumOfExpenseEntriesForMonth(accountId, month, year)

        fun getAllEntriesDTO(): Flow<List<RecordDTO>> = entryDao.getAllEntriesDTO()
        fun getAllIncomesDTO(): Flow<List<RecordDTO>> = entryDao.getAllIncomesDTO()
        fun getAllExpensesDTO(): Flow<List<RecordDTO>> = entryDao.getAllExpensesDTO()
        fun getAllEntriesByAccount(accountId: Int): Flow<List<RecordDTO>> =
            entryDao.getAllEntriesByAccountDTO(accountId)

        // 4️⃣ Filtrado de entradas
        fun getEntriesFiltered(filter: SearchFilter): Flow<List<RecordDTO>> {

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
    ):List<RecordDTO> =entryDao.getAllEntriesByDateDTO(accountId,
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
        suspend fun deleteEntry(entryDTO: RecordDTO) {
            val entry = entryMapper.entryDtoToEntry(entryDTO)
            entryDao.deleteEntry(entry)
        }

    }



