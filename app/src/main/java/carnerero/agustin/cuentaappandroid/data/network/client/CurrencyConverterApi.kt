package carnerero.agustin.cuentaappandroid.data.network.client



import carnerero.agustin.cuentaappandroid.data.network.model.AppConst
import carnerero.agustin.cuentaappandroid.data.network.model.Currency
import com.github.mikephil.charting.BuildConfig
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