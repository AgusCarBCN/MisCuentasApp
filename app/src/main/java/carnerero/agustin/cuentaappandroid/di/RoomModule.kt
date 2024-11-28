package carnerero.agustin.cuentaappandroid.di

import android.content.Context
import androidx.room.Room
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

    private const val APP_DATABASE="app_database"

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context,
        AppDataBase::class.java, APP_DATABASE
    ).build()

    @Provides
    @Singleton
    fun provideAccountDao(database: AppDataBase) = database.getAccountDao()


    @Provides
    @Singleton
    fun provideEntryDao(database: AppDataBase) = database.getEntryDao()


    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDataBase) = database.getCategoryDao()
}