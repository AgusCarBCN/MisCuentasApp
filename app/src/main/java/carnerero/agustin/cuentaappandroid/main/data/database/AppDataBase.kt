package carnerero.agustin.cuentaappandroid.main.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import carnerero.agustin.cuentaappandroid.main.data.database.converter.Converters
import carnerero.agustin.cuentaappandroid.main.data.database.dao.AccountDao
import carnerero.agustin.cuentaappandroid.main.data.database.dao.CategoryDao
import carnerero.agustin.cuentaappandroid.main.data.database.dao.EntryDao
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Account
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Category
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Entry

@Database(entities=[Account::class, Entry::class, Category::class], version = 1)
@TypeConverters(Converters::class) // Registrar los conversores
abstract class AppDataBase:RoomDatabase(){

abstract fun getAccountDao(): AccountDao
abstract fun getEntryDao(): EntryDao
abstract fun getCategoryDao(): CategoryDao
}
