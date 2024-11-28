package carnerero.agustin.cuentaappandroid.data.repository


import carnerero.agustin.cuentaappandroid.data.network.model.Currency
import carnerero.agustin.cuentaappandroid.data.network.client.CurrencyConverterApi
import retrofit2.Response
import javax.inject.Inject

class CurrencyConverterRepository @Inject constructor(
    private val api: CurrencyConverterApi
) {
    suspend fun convertCurrency(fromCurrency: String, toCurrency: String): Response<Currency> {
        return api.convertCurrency(fromCurrency, toCurrency)
    }
}