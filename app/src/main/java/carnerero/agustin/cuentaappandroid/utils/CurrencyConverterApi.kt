package carnerero.agustin.cuentaappandroid.utils

import carnerero.agustin.cuentaappandroid.AppConst
import carnerero.agustin.cuentaappandroid.model.Currency
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