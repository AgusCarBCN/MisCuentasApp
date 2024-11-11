package carnerero.agustin.cuentaappandroid.di

import carnerero.agustin.cuentaappandroid.retrofit.AppConst
import carnerero.agustin.cuentaappandroid.retrofit.CurrencyConverterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

object NetWorkModule {

    @Module
    @InstallIn(SingletonComponent::class)
    object NetworkModule {

        @Singleton
        @Provides
        fun provideOkHttpClient(): OkHttpClient {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }

        @Singleton
        @Provides
        fun provideRetrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(AppConst.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        @Singleton
        @Provides
        fun provideCurrencyConverterApi(retrofit: Retrofit): CurrencyConverterApi {
            return retrofit.create(CurrencyConverterApi::class.java)
        }
    }
}