package carnerero.agustin.cuentaappandroid.main.data.database.repository

import carnerero.agustin.cuentaappandroid.main.data.database.dao.AccountDao
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Account
import javax.inject.Inject

class AccountRepository @Inject constructor(private val accountDao: AccountDao) {


    // 1. Insertar una cuenta
    suspend fun insertAccount(account: Account) {
        accountDao.insertAccount(account)

    }

    // 2. Listar todas las cuentas
    suspend fun getAllAccounts(): List<Account> {
        return accountDao.getAllAccounts()
    }

    //3.Borrar una cuenta
    suspend fun deleteAccount(account: Account) {
        accountDao.deleteAccount(account)
    }

    // 4. Borrar todas las cuentas
    suspend fun deleteAllAccounts() {
        accountDao.deleteAllAccounts()
    }

    // 5. Obtener una cuenta específica por ID
    suspend fun getAccountById(accountId: Int): Account? {
        return accountDao.getAccountById(accountId)
    }

    // 6. Actualizar el nombre de una cuenta
    suspend fun updateAccountName(accountId: Int, newName: String) {
        accountDao.updateAccountName(accountId, newName)
    }

    // 7. Actualizar el balance de una cuenta
    suspend fun updateAccountBalance(accountId: Int, newBalance: Double) {
        accountDao.updateAccountBalance(accountId, newBalance)
    }
    //8.Transferencia entre cuentas
    suspend fun transferFunds(fromAccountId: Int, toAccountId: Int, amount: Double) {
        accountDao.transferFunds(fromAccountId, toAccountId, amount)
    }
    // 9. Update checked account
    suspend fun updateCheckedAccount(accountId:Int, newValue:Boolean) {
        accountDao.updateCheckedAccount(accountId,newValue)
    }

    // 10. Update amount account
    suspend fun updateSpendingLimitAccount(accountId:Int, newAmount:Double) {
        accountDao.updateSpendingLimitAccount(accountId,newAmount)
    }

   // 11. Update from Date account
    suspend fun updateFromDateAccount(accountId:Int,newDate:String) {
        accountDao.updateFromDateAccount(accountId,newDate)
    }

    // 12. Update to Date account
    suspend fun updateToDateAccount(accountId:Int,newDate:String) {
        accountDao.updateToDateAccount(accountId,newDate)
    }

    // 13. Get all accounts checked

    suspend fun getAllAccountsChecked(): List<Account> {
        return accountDao.getAllAccountsChecked()
    }

}