package carnerero.agustin.cuentaappandroid.utils

import carnerero.agustin.cuentaappandroid.AppConst
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{
        private val retrofit by lazy{
            val logging=HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client=OkHttpClient.Builder().addInterceptor(logging).build()

            Retrofit.Builder().baseUrl(AppConst.BASEURL).addConverterFactory(GsonConverterFactory.create()).client(client).build()
        }
        val apiCurrency: CurrencyConverterApi by lazy {
            retrofit.create(CurrencyConverterApi::class.java)
        }
    }
}