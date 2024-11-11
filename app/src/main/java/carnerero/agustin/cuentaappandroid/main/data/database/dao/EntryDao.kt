package carnerero.agustin.cuentaappandroid.main.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import carnerero.agustin.cuentaappandroid.main.data.database.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Entry
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.util.Date

@Dao
interface EntryDao {

    // 1. Add a new entry
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: Entry)

    /* // 2. Borrar una entrada por ID
     @Delete
     suspend fun deleteEntry(entry: Entry)

     // 3. Borrar todas las entradas
     @Query("DELETE FROM EntryEntity")
     suspend fun deleteAllEntries()*/

    // 2. Get all entries
    @Query("SELECT * FROM EntryEntity ORDER BY date DESC")
    suspend fun getAllEntries(): List<Entry>

    // 3. Get all incomes
    @Query("SELECT * FROM EntryEntity WHERE amount>=0")
    suspend fun getAllIncomes(): List<Entry>

    // 4. Get all expenses

    @Query("SELECT * FROM EntryEntity WHERE amount<0")
    suspend fun getAllExpenses(): List<Entry>

    // 5. Get entry by id
    @Query("SELECT * FROM EntryEntity WHERE id = :entryId")
    suspend fun getEntryById(entryId: Long): Entry?



    // 7. Actualizar el montante de una entrada
    @Query("UPDATE EntryEntity SET amount = :amount WHERE id = :accountId")
    suspend fun updateAmountEntry(accountId: Long, amount: Double)


    // 7. Obtener entradas por fecha específica (ordenadas por fecha descendiente)
    @Query("SELECT * FROM EntryEntity WHERE date = :specificDate ORDER BY date DESC")
    suspend fun getEntriesByDate(specificDate: String): List<Entry>

    // 8. Obtener entradas con saldo mayor o igual a un cierto monto (ordenadas por fecha descendiente)
    @Query("SELECT * FROM EntryEntity WHERE amount >= :minAmount ORDER BY date DESC")
    suspend fun getEntriesByAmount(minAmount: Double): List<Entry>


    //6. Get all entries by category
    @Transaction
    @Query("SELECT * FROM EntryEntity WHERE categoryId = :categoryId ORDER BY date DESC")
    suspend fun getEntriesByCategory(categoryId: Int): List<Entry>

    //7.Get sum of all incomes
    @Transaction
    @Query("SELECT SUM(amount) FROM EntryEntity WHERE amount >= 0")
    suspend fun getSumOfIncomeEntries(): Double?

    //7.Get sum of all expenses
    @Transaction
    @Query("SELECT SUM(amount) FROM EntryEntity WHERE amount < 0")
    suspend fun getSumOfExpenseEntries(): Double?

    @Transaction
    @Query("SELECT SUM(amount) FROM EntryEntity WHERE amount < 0 AND categoryId= :id")
    suspend fun getSumOfExpenseByCategory(id:Int): Double?

    //8. Get sum of all incomes by date
    @Transaction
    @Query(
        """SELECT SUM(amount) FROM EntryEntity WHERE amount >= 0
            AND accountId = :accountId
            AND date >= :dateFrom  
            AND date <= :dateTo  """
    )
    suspend fun getSumOfIncomeEntriesByDate(
        accountId: Int,
        dateFrom: String = Date().dateFormat(),
        dateTo: String = Date().dateFormat()
    ): Double?

    //9. Get sum of all expenses by date
    @Transaction
    @Query(
        """SELECT SUM(amount) FROM EntryEntity WHERE amount < 0
            AND accountId = :accountId
            AND date >= :dateFrom  
            AND date <= :dateTo"""
    )
    suspend fun getSumOfExpenseEntriesByDate(
        accountId: Int,
        dateFrom: String = Date().dateFormat(),
        dateTo: String = Date().dateFormat()
    ): Double?

    //9. Get sum of all expenses by date and category
    @Transaction
    @Query(
        """SELECT SUM(amount) FROM EntryEntity WHERE amount < 0
            AND categoryId = :categoryId
            AND date >= :dateFrom  
            AND date <= :dateTo"""
    )
    suspend fun getSumOfExpenseByCategoryAndDate(
        categoryId: Int,
        dateFrom: String = Date().dateFormat(),
        dateTo: String = Date().dateFormat()
    ): Double?


    //10.Get sum of incomes entries by month
    @Transaction
    @Query(
        """
    SELECT SUM(amount) FROM EntryEntity 
        WHERE amount >= 0
         AND accountId = :accountId
         AND SUBSTR(date, 4, 2) = :month 
         AND SUBSTR(date, 7, 4) = :year
"""
    )
    suspend fun getSumOfIncomeEntriesForMonth(
        accountId: Int,
        month: String,  // Espera un valor en formato 'MM' (ejemplo: '01' para enero)
        year: String    // Espera un valor en formato 'YYYY' (ejemplo: '2024')
    ): Double?


    //11.Get sum of expenses entries by month
    @Transaction
    @Query(
        """           
           
SELECT SUM(amount) FROM EntryEntity 
WHERE amount < 0
  AND accountId = :accountId
   AND SUBSTR(date, 4, 2) = :month 
   AND SUBSTR(date, 7, 4) = :year
"""
    )
    suspend fun getSumOfExpensesEntriesForMonth(
        accountId: Int,
        month: String,
        year: String
    ): Double?



    @Query(
        """
    SELECT e.description,
           e.amount,
           e.date,
           c.iconResource,
           c.nameResource,
           e.accountId,
           a.name,
           e.categoryId
    FROM EntryEntity e
    INNER JOIN AccountEntity a ON e.accountId = a.id
    INNER JOIN CategoryEntity c ON e.categoryId = c.id
    WHERE e.amount >= 0
"""
    )
    suspend fun getAllIncomesDTO(): List<EntryDTO>

    @Query(
        """
    SELECT e.description,
           e.amount,
           e.date,
           c.nameResource,
           c.iconResource,
           e.accountId,
           a.name,
           e.categoryId
    FROM EntryEntity e
    INNER JOIN AccountEntity a ON e.accountId = a.id
    INNER JOIN CategoryEntity c ON e.categoryId = c.id
    WHERE e.amount < 0
"""
    )
    suspend fun getAllExpensesDTO(): List<EntryDTO>

    @Query(
        """
    SELECT e.description,
           e.amount,
           e.date,
           c.nameResource,
           c.iconResource,
           e.accountId,
           a.name,
            e.categoryId
    FROM EntryEntity e
    INNER JOIN AccountEntity a ON e.accountId = a.id
    INNER JOIN CategoryEntity c ON e.categoryId = c.id
   
"""
    )
    suspend fun getAllEntriesDTO(): List<EntryDTO>

    @Query(
        """
    SELECT e.description,
           e.amount,
           e.date,
           c.nameResource,
           c.iconResource,
           e.accountId,
           a.name ,
           e.categoryId
    FROM EntryEntity e
    INNER JOIN AccountEntity a ON e.accountId = a.id
     INNER JOIN CategoryEntity c ON e.categoryId = c.id
   WHERE accountId = :accountId
"""
    )
    suspend fun getAllEntriesByAccountDTO(accountId: Int): List<EntryDTO>

    @Query(
        """
    SELECT e.description,
       e.amount,
       e.date,
       c.nameResource,
       c.iconResource,
       e.accountId,
       a.name ,
       e.categoryId
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


"""
    )
    suspend fun getFilteredEntriesDTO(
        accountId: Int,
        descriptionAmount: String,
        dateFrom: String = Date().dateFormat(),
        dateTo: String = Date().dateFormat(),
        amountMin: Double = 0.0,
        amountMax: Double = 0.0,
        selectedOptions: Int = 0
    ): List<EntryDTO>

}

