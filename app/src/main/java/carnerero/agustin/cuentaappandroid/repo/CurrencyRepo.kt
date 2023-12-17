package carnerero.agustin.cuentaappandroid.repo

import carnerero.agustin.cuentaappandroid.utils.RetrofitInstance

class CurrencyRepo {

    suspend fun getCurrency(currencyForm:String,currencyTo:String)=RetrofitInstance.apiCurrency.convertCurrency(currencyForm,currencyTo)
}