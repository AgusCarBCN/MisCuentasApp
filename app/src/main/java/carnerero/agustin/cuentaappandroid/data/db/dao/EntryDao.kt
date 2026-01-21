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
import java.math.BigDecimal

@Dao
interface EntryDao {

        // 1️⃣ Insertar nueva entrada
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertEntry(entry: Entry)

        // 2️⃣ Borrar entrada
        @Delete
        suspend fun deleteEntry(entry: Entry)

        // 3️⃣ Actualizar toda la entrada
        @Update
        suspend fun updateEntry(entry: Entry)

        // 4️⃣ Actualizar campos específicos
        @Query("UPDATE EntryEntity SET description = :description, amount = :amount, date = :date WHERE id = :id")
        suspend fun updateEntryFields(id: Long, description: String, amount: BigDecimal, date: String)

        // 5️⃣ Listar todas las entradas
        @Query("SELECT * FROM EntryEntity ORDER BY date DESC")
        suspend fun getAllEntries(): List<Entry>

        // 6️⃣ Listar todos los ingresos
        @Query("SELECT * FROM EntryEntity WHERE amount >= 0")
        suspend fun getAllIncomes(): List<Entry>

        // 7️⃣ Listar todos los gastos
        @Query("SELECT * FROM EntryEntity WHERE amount < 0")
        suspend fun getAllExpenses(): List<Entry>

        // 8️⃣ Obtener entrada por id
        @Query("SELECT * FROM EntryEntity WHERE id = :entryId")
        suspend fun getEntryById(entryId: Long): Entry?

        // 9️⃣ Sumar ingresos en Kotlin
        suspend fun getSumOfIncomeEntries(): BigDecimal =
            getAllIncomes().fold(BigDecimal.ZERO) { acc, entry -> acc + entry.amount }

        // 🔟 Sumar gastos en Kotlin
        suspend fun getSumOfExpenseEntries(): BigDecimal =
            getAllExpenses().fold(BigDecimal.ZERO) { acc, entry -> acc + entry.amount }

        // 1️⃣1️⃣ Sumar gastos por categoría
        suspend fun getSumOfExpenseByCategory(categoryId: Int): BigDecimal =
            getAllExpenses().filter { it.categoryId == categoryId }
                .fold(BigDecimal.ZERO) { acc, entry -> acc + entry.amount }

        // 1️⃣2️⃣ Sumar ingresos por fecha y cuenta
        suspend fun getSumOfIncomeEntriesByDate(
            accountId: Int,
            dateFrom: String,
            dateTo: String
        ): BigDecimal = getAllIncomes()
            .filter { it.accountId == accountId && it.date >= dateFrom && it.date <= dateTo }
            .fold(BigDecimal.ZERO) { acc, entry -> acc + entry.amount }

        // 1️⃣3️⃣ Sumar gastos por fecha y cuenta
        suspend fun getSumOfExpenseEntriesByDate(
            accountId: Int,
            dateFrom: String,
            dateTo: String
        ): BigDecimal = getAllExpenses()
            .filter { it.accountId == accountId && it.date >= dateFrom && it.date <= dateTo }
            .fold(BigDecimal.ZERO) { acc, entry -> acc + entry.amount }

        // 1️⃣4️⃣ Actualizar montos por tipo de cambio en Kotlin
        @Transaction
        suspend fun updateEntriesAmountByExchangeRate(rate: BigDecimal) {
            val entries = getAllEntries()
            entries.forEach { entry ->
                val newAmount = entry.amount.multiply(rate)
                updateEntryFields(entry.id, entry.description, newAmount, entry.date)
            }
        }

        // 1️⃣5️⃣ Obtener entradas por categoría
        @Transaction
        @Query("SELECT * FROM EntryEntity WHERE categoryId = :categoryId ORDER BY date DESC")
        suspend fun getEntriesByCategory(categoryId: Int): List<Entry>

        // 1️⃣6️⃣ DTOs para UI (ingresos, gastos, todos)
        @Query("""
        SELECT e.id, e.description, e.amount, e.date,
               c.nameResource, c.iconResource,
               e.accountId, a.name, e.categoryId, c.categoryType
        FROM EntryEntity e
        INNER JOIN AccountEntity a ON e.accountId = a.id
        INNER JOIN CategoryEntity c ON e.categoryId = c.id
        ORDER BY date DESC, e.id DESC
    """)
        suspend fun getAllEntriesDTO(): List<EntryDTO>

        @Query("""
        SELECT e.id, e.description, e.amount, e.date,
               c.nameResource, c.iconResource,
               e.accountId, a.name, e.categoryId, c.categoryType
        FROM EntryEntity e
        INNER JOIN AccountEntity a ON e.accountId = a.id
        INNER JOIN CategoryEntity c ON e.categoryId = c.id
        WHERE e.amount >= 0
        ORDER BY date DESC, e.id DESC
    """)
        suspend fun getAllIncomesDTO(): List<EntryDTO>

        @Query("""
        SELECT e.id, e.description, e.amount, e.date,
               c.nameResource, c.iconResource,
               e.accountId, a.name, e.categoryId, c.categoryType
        FROM EntryEntity e
        INNER JOIN AccountEntity a ON e.accountId = a.id
        INNER JOIN CategoryEntity c ON e.categoryId = c.id
        WHERE e.amount < 0
        ORDER BY date DESC, e.id DESC
    """)
        suspend fun getAllExpensesDTO(): List<EntryDTO>
    }



