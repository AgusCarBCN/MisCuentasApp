package carnerero.agustin.cuentaappandroid.di

import android.content.Context
import androidx.room.Room
import carnerero.agustin.cuentaappandroid.data.db.converter.Converters
import carnerero.agustin.cuentaappandroid.data.db.dao.AccountDao
import carnerero.agustin.cuentaappandroid.data.db.dao.CategoryDao
import carnerero.agustin.cuentaappandroid.data.db.dao.EntryDao
import carnerero.agustin.cuentaappandroid.data.db.database.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



    @Module
    @InstallIn(SingletonComponent::class)
    object RoomModule {

        @Provides
        @Singleton
        fun provideRoomDatabase(@ApplicationContext context: Context): AppDataBase {
            return Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "my_database"
            )

                .build()
        }

        @Provides
        fun provideEntryDao(database: AppDataBase): EntryDao = database.getEntryDao()

        @Provides
        fun provideAccountDao(database: AppDataBase): AccountDao = database.getAccountDao()

        @Provides
        fun provideCategoryDao(database: AppDataBase): CategoryDao = database.getCategoryDao()
    }


