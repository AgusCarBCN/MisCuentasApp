package carnerero.agustin.cuentaappandroid.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import java.math.BigDecimal
import java.math.RoundingMode

/*Permiten que una función pueda ser pausada y reanudada en un contexto de corutinas,
 lo que facilita la ejecución de operaciones que podrían bloquear el hilo principal*/

@Dao
interface AccountDao {

    // 1️⃣ Insertar cuenta
    @Insert
    suspend fun insertAccount(account: Account)

    // 2️⃣ Listar todas las cuentas
    @Query("SELECT * FROM AccountEntity")
     fun getAllAccounts(): Flow<List<Account>>

    // 3️⃣ Borrar cuenta
    @Delete
    suspend fun deleteAccount(account: Account)

    // 4️⃣ Borrar todas las cuentas
    @Query("DELETE FROM AccountEntity")
    suspend fun deleteAllAccounts()

    // 5️⃣ Obtener cuenta por id
    @Query("SELECT * FROM AccountEntity WHERE id = :accountId LIMIT 1")
    suspend fun getAccountById(accountId: Int): Account?

    // 6️⃣ Actualizar nombre de cuenta
    @Query("UPDATE AccountEntity SET name = :newName WHERE id = :accountId")
    suspend fun updateAccountName(accountId: Int, newName: String)

    // 7️⃣ Actualizar balance de cuenta
    @Query("UPDATE AccountEntity SET balance = :newBalance WHERE id = :accountId")
    suspend fun updateAccountBalance(accountId: Int, newBalance: BigDecimal)

    @Transaction
    suspend fun deposit(accountId: Int, amount: BigDecimal) {
        val account = getAccountById(accountId) ?: throw Exception("Cuenta no encontrada")
        require(amount > BigDecimal.ZERO) { "El monto debe ser positivo" }

        val newBalance = account.balance.add(amount)
        updateAccountBalance(accountId,newBalance)
    }

    @Transaction
    suspend fun withdraw(accountId: Int, amount: BigDecimal) {
        val account = getAccountById(accountId) ?: throw Exception("Cuenta no encontrada")
        require(amount > BigDecimal.ZERO) { "El monto debe ser positivo" }

        val newBalance = account.balance.subtract(amount)
        if (newBalance < BigDecimal.ZERO) {
            throw Exception("Saldo insuficiente")
        }

        updateAccountBalance(accountId,newBalance)
    }

    // 8️⃣ Transferencia de fondos entre cuentas
    @Transaction
    suspend fun transferFunds(fromAccountId: Int, toAccountId: Int, amount: BigDecimal) {
        val fromAccount = getAccountById(fromAccountId)
            ?: throw IllegalArgumentException("Cuenta origen no encontrada")
        val toAccount = getAccountById(toAccountId)
            ?: throw IllegalArgumentException("Cuenta destino no encontrada")

        val updatedFromBalance = fromAccount.balance.subtract(amount)
        if (updatedFromBalance < BigDecimal.ZERO) {
            throw IllegalArgumentException("Saldo insuficiente en la cuenta origen")
        }
        val updatedToBalance = toAccount.balance.add(amount)

        updateAccountBalance(fromAccountId, updatedFromBalance)
        updateAccountBalance(toAccountId, updatedToBalance)
    }

    // 9️⃣ Actualizar estado isChecked
    @Query("UPDATE AccountEntity SET isChecked = :newValueChecked WHERE id = :accountId")
    suspend fun updateCheckedAccount(accountId: Int, newValueChecked: Boolean)

    // 🔟 Actualizar spendingLimit
    @Query("UPDATE AccountEntity SET periodSpendingLimit = :newAmount WHERE id = :accountId")
    suspend fun updateSpendingLimitAccount(accountId: Int, newAmount: BigDecimal)

    // 1️⃣1️⃣ Actualizar fecha fromDate
    @Query("UPDATE AccountEntity SET fromDate = :newDate WHERE id = :accountId")
    suspend fun updateFromDateAccount(accountId: Int, newDate: String)

    // 1️⃣2️⃣ Actualizar fecha toDate
    @Query("UPDATE AccountEntity SET toDate = :newDate WHERE id = :accountId")
    suspend fun updateToDateAccount(accountId: Int, newDate: String)

    // 1️⃣3️⃣ Listar todas las cuentas con isChecked = true
    @Query("SELECT * FROM AccountEntity WHERE isChecked=1")
    suspend fun getAllAccountsChecked(): List<Account>

    // 1️⃣4️⃣ Actualizar balance de todas las cuentas según tipo de cambio
    // NO se puede hacer directamente con BigDecimal en SQL, hacemos en Kotlin
   /* @Transaction
    suspend fun updateAllBalancesByExchangeRate(rate: BigDecimal) {
        val accounts = getAllAccounts()
        accounts.forEach { account ->
            val newBalance = account.balance.multiply(rate)
                .setScale(8, RoundingMode.HALF_UP)
            updateAccountBalance(account.id, newBalance)
        }
    }*/
    @Transaction
    suspend fun updateAllBalancesByExchangeRate(rate: BigDecimal) {
        // Obtenemos la lista actual de cuentas de manera inmediata
        val accounts = getAllAccounts().first() // first() obtiene el valor actual del Flow
        accounts.forEach { account ->
            val newBalance = account.balance.multiply(rate)
                .setScale(8, RoundingMode.HALF_UP)
            updateAccountBalance(account.id, newBalance)
        }
    }

}
