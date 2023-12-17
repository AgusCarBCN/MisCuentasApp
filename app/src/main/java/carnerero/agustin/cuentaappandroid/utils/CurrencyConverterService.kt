package carnerero.agustin.cuentaappandroid.utils

import carnerero.agustin.cuentaappandroid.AppConst
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {

    private var retrofit: Retrofit? = null
    private const val BASE_URL = AppConst.BASEURL

    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }

    fun createApiService(): CurrencyConverterApi {
        return getRetrofitInstance().create(CurrencyConverterApi::class.java)
    }
}
