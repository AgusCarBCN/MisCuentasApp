package carnerero.agustin.cuentaappandroid.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Entry
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Date

@Dao
interface EntryDao {

    // 1. Add a new entry
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: Entry)

    // 2. Delete entry
    @Delete
    suspend fun deleteEntry(entry: Entry)

    // 3. Modify entries
    @Update
    suspend fun updateEntry(entry: Entry)

    // 4. Get all entries
    @Query("SELECT * FROM EntryEntity ORDER BY date DESC")
    suspend fun getAllEntries(): List<Entry>

    // 5. Get all incomes
    @Query("SELECT * FROM EntryEntity WHERE amount>=0")
    suspend fun getAllIncomes(): List<Entry>

    // 6. Get all expenses
    @Query("SELECT * FROM EntryEntity WHERE amount<0")
    suspend fun getAllExpenses(): List<Entry>

    // 7. Get entry by id
    @Query("SELECT * FROM EntryEntity WHERE id = :entryId")
    suspend fun getEntryById(entryId: Long): Entry?

    // 8. Update All entries by exchange rate
    @Transaction
    suspend fun updateEntriesAmountByExchangeRate(rate: BigDecimal) {
        val entries = getAllEntries()
        entries.forEach { entry ->
            val newAmount = entry.amount.multiply(rate)
                .setScale(8, RoundingMode.HALF_UP)
            updateEntryFields(entry.id, entry.description, newAmount, entry.date)
        }
    }
    // 9. Get all entries by date
    @Query("SELECT * FROM EntryEntity WHERE date = :specificDate ORDER BY date DESC")
    suspend fun getEntriesByDate(specificDate: String): List<Entry>

    // 10. Get entries by amount
    @Query("SELECT * FROM EntryEntity WHERE amount >= :minAmount ORDER BY date DESC")
    suspend fun getEntriesByAmount(minAmount: BigDecimal): List<Entry>

    // 11. Get all entries by category
    @Transaction
    @Query("SELECT * FROM EntryEntity WHERE categoryId = :categoryId ORDER BY date DESC")
    suspend fun getEntriesByCategory(categoryId: Int): List<Entry>

    // 12. Get sum of all incomes
    @Transaction
    @Query("SELECT SUM(amount) FROM EntryEntity WHERE amount >= 0")
    suspend fun getSumOfIncomeEntries(): BigDecimal?

    // 13. Get sum of all expenses
    @Transaction
    @Query("SELECT SUM(amount) FROM EntryEntity WHERE amount < 0")
    suspend fun getSumOfExpenseEntries(): BigDecimal?

    // 14. Get sum of all expenses by category
    @Transaction
    @Query("SELECT SUM(amount) FROM EntryEntity WHERE amount >= 0 AND categoryId = :id")
    suspend fun getSumOfIncomesByCategory(id: Int): BigDecimal?

    // 15. Get sum of all expenses by category
    @Transaction
    @Query("SELECT SUM(amount) FROM EntryEntity WHERE amount < 0 AND categoryId = :id")
    suspend fun getSumOfExpenseByCategory(id: Int): BigDecimal?

    // 16. Get sum of incomes by date
    @Transaction
    @Query(
        """SELECT SUM(amount) FROM EntryEntity WHERE amount >= 0
        AND accountId = :accountId
        AND date >= :dateFrom  
        AND date <= :dateTo"""
    )
    suspend fun getSumOfIncomeEntriesByDate(
        accountId: Int,
        dateFrom: String = Date().dateFormat(),
        dateTo: String = Date().dateFormat()
    ): BigDecimal?

    // 17. Get sum of expenses by date
    @Transaction
    @Query(
        """SELECT SUM(amount) FROM EntryEntity WHERE amount < 0
        AND accountId = :accountId
        AND date >= :dateFrom  
        AND date <= :dateTo"""
    )
    suspend fun getSumOfExpensesEntriesByDate(
        accountId: Int,
        dateFrom: String = Date().dateFormat(),
        dateTo: String = Date().dateFormat()
    ): BigDecimal?

    // 18. Get sum of incomes by category
    @Transaction
    @Query(
        """SELECT SUM(amount) FROM EntryEntity WHERE amount >= 0
        AND categoryId = :categoryId
        AND date >= :dateFrom  
        AND date <= :dateTo"""
    )
    suspend fun getSumOfIncomesByCategoryAndDate(
        categoryId: Int,
        dateFrom: String = Date().dateFormat(),
        dateTo: String = Date().dateFormat()
    ): BigDecimal?



    // 19. Get sum of expenses by category
    @Transaction
    @Query(
        """SELECT SUM(amount) FROM EntryEntity WHERE amount < 0
        AND categoryId = :categoryId
        AND date >= :dateFrom  
        AND date <= :dateTo"""
    )
    suspend fun getSumOfExpensesByCategoryAndDate(
        categoryId: Int,
        dateFrom: String = Date().dateFormat(),
        dateTo: String = Date().dateFormat()
    ): BigDecimal?

    // 20. Get sum of incomes entries by month
    @Transaction
    @Query(
        """
    SELECT SUM(amount) FROM EntryEntity 
    WHERE amount >= 0
    AND accountId = :accountId
    AND SUBSTR(date, 6, 2) = :month  -- Extrae el mes
    AND SUBSTR(date, 1, 4) = :year   -- Extrae el año
    """
    )
    suspend fun getSumOfIncomeEntriesForMonth(
        accountId: Int,
        month: String,  // '01' a '12'
        year: String    // 'YYYY'
    ): BigDecimal?

    // 21. Get sum of expenses entries by month
    @Transaction
    @Query(
        """
    SELECT SUM(amount) FROM EntryEntity 
    WHERE amount < 0
    AND accountId = :accountId
    AND SUBSTR(date, 6, 2) = :month  -- Extrae el mes
    AND SUBSTR(date, 1, 4) = :year   -- Extrae el año
    """
    )
    suspend fun getSumOfExpenseEntriesForMonth(
        accountId: Int,
        month: String,  // '01' a '12'
        year: String    // 'YYYY'
    ): BigDecimal?

    // 22. Get all expenses
    @Query(
        """
    SELECT e.id,
           e.description,
           e.amount,
           e.date,
           c.iconResource,
           c.nameResource,
           e.accountId,
           a.name,
           e.categoryId,
           c.categoryType
    FROM EntryEntity e
    INNER JOIN AccountEntity a ON e.accountId = a.id
    INNER JOIN CategoryEntity c ON e.categoryId = c.id
    WHERE e.amount >= 0
    ORDER BY date DESC, e.id DESC
"""
    )
    suspend fun getAllIncomesDTO(): List<EntryDTO>

    // 23. Get all expenses
    @Query(
        """
    SELECT e.id,
           e.description,
           e.amount,
           e.date,
           c.nameResource,
           c.iconResource,
           e.accountId,
           a.name,
           e.categoryId,
           c.categoryType
    FROM EntryEntity e
    INNER JOIN AccountEntity a ON e.accountId = a.id
    INNER JOIN CategoryEntity c ON e.categoryId = c.id
    WHERE e.amount < 0 
    ORDER BY date DESC, e.id DESC
"""
    )
    suspend fun getAllExpensesDTO(): List<EntryDTO>

    // 24. Get all entries
    @Query(
        """
    SELECT e.id,
           e.description,
           e.amount,
           e.date,
           c.nameResource,
           c.iconResource,
           e.accountId,
           a.name,
            e.categoryId,
            c.categoryType
    FROM EntryEntity e
    INNER JOIN AccountEntity a ON e.accountId = a.id
    INNER JOIN CategoryEntity c ON e.categoryId = c.id
    ORDER BY date DESC, e.id DESC
"""
    )
    suspend fun getAllEntriesDTO(): List<EntryDTO>

    // 25. Get all entries by account
    @Query(
        """
    SELECT e.id,
           e.description,
           e.amount,
           e.date,
           c.nameResource,
           c.iconResource,
           e.accountId,
           a.name ,
           e.categoryId,
           c.categoryType
    FROM EntryEntity e
    INNER JOIN AccountEntity a ON e.accountId = a.id
     INNER JOIN CategoryEntity c ON e.categoryId = c.id
   WHERE accountId = :accountId
    ORDER BY date DESC, e.id DESC
"""
    )
    suspend fun getAllEntriesByAccountDTO(accountId: Int): List<EntryDTO>

    @Query(
        """
    SELECT e.id,
           e.description,
           e.amount,
           e.date,
           c.nameResource,
           c.iconResource,
           e.accountId,
           a.name ,
           e.categoryId,
           c.categoryType
    FROM EntryEntity e
    INNER JOIN AccountEntity a ON e.accountId = a.id
     INNER JOIN CategoryEntity c ON e.categoryId = c.id
        WHERE accountId = :accountId
            AND e.date >= :dateFrom 
            AND e.date <= :dateTo   
ORDER BY date DESC, e.id DESC
"""
    )
    suspend fun getAllEntriesByDateDTO(accountId: Int,
                                       dateFrom: String = Date().dateFormat(),
                                       dateTo: String = Date().dateFormat() ): List<EntryDTO>



    // 26. Get all entries filtered
    @Query(
        """
    SELECT 
       e.id, 
       e.description,
       e.amount,
       e.date,
       c.nameResource,
       c.iconResource,
       e.accountId,
       a.name ,
       e.categoryId,
       c.categoryType
       
FROM EntryEntity e
INNER JOIN AccountEntity a ON e.accountId = a.id
INNER JOIN CategoryEntity c ON e.categoryId = c.id
 WHERE e.accountId = :accountId
           
        AND e.date >= :dateFrom 
        AND e.date <= :dateTo      
        AND ABS(e.amount) >= :amountMin 
        AND ABS(e.amount) <= :amountMax
        AND (
            (:selectedOptions = 2) 
            OR (:selectedOptions = 0 AND e.amount >= 0.0)
            OR (:selectedOptions = 1 AND e.amount < 0.0)                  
        )
         AND (:descriptionAmount LIKE "" OR e.description LIKE :descriptionAmount)
         ORDER BY date DESC, e.id DESC

"""
    )
    suspend fun getFilteredEntriesDTO(
        accountId: Int,
        descriptionAmount: String,
        dateFrom: String = Date().dateFormat(),
        dateTo: String = Date().dateFormat(),
        amountMin: BigDecimal = BigDecimal.ZERO,
        amountMax: BigDecimal = BigDecimal("1E10"),
        selectedOptions: Int = 0
    ): List<EntryDTO>

    // Método para actualizar solo los campos específicos de la entrada
    @Query(
        "UPDATE EntryEntity SET description = :description, amount = :amount, date = :date WHERE id = :id"
    )
    suspend fun updateEntryFields(
        id: Long,
        description: String,
        amount: BigDecimal,
        date: String
    )

    // 27. Update amount entry
    @Query("UPDATE EntryEntity SET amount = :amount WHERE id = :accountId")
    suspend fun updateAmountEntry(accountId: Long, amount: BigDecimal)


}
