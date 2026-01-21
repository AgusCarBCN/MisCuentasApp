package carnerero.agustin.cuentaappandroid.data.network.client




import carnerero.agustin.cuentaappandroid.data.network.model.Currency
import carnerero.agustin.cuentaappandroid.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface CurrencyConverterApi {




    @GET("v6/${BuildConfig.API_KEY}/pair/{fromCurrency}/{toCurrency}")
    suspend fun convertCurrency(
        @Path("fromCurrency") fromCurrency: String,
        @Path("toCurrency") toCurrency: String
    ): Response<Currency>
}