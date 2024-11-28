package carnerero.agustin.cuentaappandroid.domain.apidata

import carnerero.agustin.cuentaappandroid.data.repository.CurrencyConverterRepository
import carnerero.agustin.cuentaappandroid.data.network.model.Currency
import retrofit2.Response
import javax.inject.Inject

class ConvertCurrencyUseCase @Inject constructor(
    private val repository: CurrencyConverterRepository
) {
    suspend operator fun invoke(fromCurrency: String, toCurrency: String): Response<Currency> {
        return repository.convertCurrency(fromCurrency, toCurrency)
    }
}