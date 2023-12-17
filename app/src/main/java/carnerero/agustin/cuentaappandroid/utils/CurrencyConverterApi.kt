package carnerero.agustin.cuentaappandroid.utils

import carnerero.agustin.cuentaappandroid.model.ConversionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface CurrencyConverterApi {
    @GET("v1/convert")
    fun convertCurrency(
        @Query("access_key") apiKey: String?,
        @Query("from") fromCurrency: String?,
        @Query("to") toCurrency: String?,
        @Query("amount") amount: Double
    ): Call<ConversionResponse?>?
}