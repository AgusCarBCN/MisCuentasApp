package carnerero.agustin.cuentaappandroid.di

import android.content.Context
import carnerero.agustin.cuentaappandroid.presentation.ui.calculator.ParserCalculator
import carnerero.agustin.cuentaappandroid.data.repository.UserDataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/*Cuando Hilt necesita una instancia de UserDataStoreRepository, busca
 en este módulo (AppModule) y usa el método provideUserPreferencesRepository para
 crearla, utilizando el Context de la aplicación que se inyecta automáticamente.
 Como está anotada con @Singleton, esa instancia será la misma durante
 todo el ciclo de vida de la aplicación*/

@InstallIn(SingletonComponent::class)
@Module

object AppModule {


    @Provides
    @Singleton
    fun provideUserPreferencesRepository(@ApplicationContext context: Context): UserDataStoreRepository {
        return UserDataStoreRepository(context)
    }
    @Singleton
    @Provides
    fun provideParserCalculator(): ParserCalculator {
        return ParserCalculator()
    }

}