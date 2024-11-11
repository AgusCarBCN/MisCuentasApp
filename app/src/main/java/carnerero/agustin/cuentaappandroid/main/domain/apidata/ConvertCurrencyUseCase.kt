package carnerero.agustin.cuentaappandroid.main.domain.apidata

import carnerero.agustin.cuentaappandroid.main.data.apicurrencydata.CurrencyConverterRepository
import carnerero.agustin.cuentaappandroid.retrofit.Currency
import retrofit2.Response
import javax.inject.Inject

class ConvertCurrencyUseCase @Inject constructor(
    private val repository: CurrencyConverterRepository
) {
    suspend operator fun invoke(fromCurrency: String, toCurrency: String): Response<Currency> {
        return repository.convertCurrency(fromCurrency, toCurrency)
    }
}