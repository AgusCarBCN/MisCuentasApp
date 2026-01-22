package carnerero.agustin.cuentaappandroid.data.network.client




import carnerero.agustin.cuentaappandroid.data.network.model.Currency
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CurrencyConverterApi {



    // La clave la pas
    // amos como query
    @GET("v6/pair/{fromCurrency}/{toCurrency}")
    suspend fun convertCurrency(
        @Path("fromCurrency") fromCurrency: String,
        @Path("toCurrency") toCurrency: String,
        @Query("apiKey") apiKey: String = carnerero.agustin.cuentaappandroid.BuildConfig.API_KEY// tu key aquí
    ): Response<Currency>
}