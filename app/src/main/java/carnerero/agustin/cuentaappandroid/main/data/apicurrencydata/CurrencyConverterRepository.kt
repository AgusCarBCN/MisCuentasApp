package carnerero.agustin.cuentaappandroid.main.data.apicurrencydata


import carnerero.agustin.cuentaappandroid.retrofit.Currency
import carnerero.agustin.cuentaappandroid.retrofit.CurrencyConverterApi
import retrofit2.Response
import javax.inject.Inject

class CurrencyConverterRepository @Inject constructor(
    private val api: CurrencyConverterApi
) {
    suspend fun convertCurrency(fromCurrency: String, toCurrency: String): Response<Currency> {
        return api.convertCurrency(fromCurrency, toCurrency)
    }
}