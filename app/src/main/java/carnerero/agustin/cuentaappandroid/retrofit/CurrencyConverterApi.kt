package carnerero.agustin.cuentaappandroid.retrofit



import carnerero.agustin.cuentaappandroid.retrofit.AppConst
import carnerero.agustin.cuentaappandroid.retrofit.Currency
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface CurrencyConverterApi {
    @GET("v6/${AppConst.KEY}/pair/{fromCurrency}/{toCurrency}")
    suspend fun convertCurrency(
        @Path("fromCurrency") fromCurrency: String,
        @Path("toCurrency") toCurrency: String
    ): Response<Currency>
}