package carnerero.agustin.cuentaappandroid.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import carnerero.agustin.cuentaappandroid.data.db.converter.Converters
import carnerero.agustin.cuentaappandroid.data.db.dao.AccountDao
import carnerero.agustin.cuentaappandroid.data.db.dao.CategoryDao
import carnerero.agustin.cuentaappandroid.data.db.dao.EntryDao
import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.data.db.entities.Entry

@Database(entities=[Account::class, Entry::class, Category::class], version = 1)
@TypeConverters(Converters::class) // Registrar los conversores
abstract class AppDataBase:RoomDatabase(){

abstract fun getAccountDao(): AccountDao
abstract fun getEntryDao(): EntryDao
abstract fun getCategoryDao(): CategoryDao
}
