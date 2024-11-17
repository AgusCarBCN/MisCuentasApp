package carnerero.agustin.cuentaappandroid.createaccounts.view


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.R

import carnerero.agustin.cuentaappandroid.createaccounts.model.Currency
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Account
import carnerero.agustin.cuentaappandroid.main.domain.apidata.ConvertCurrencyUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase.DeleteAccountByIdUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase.GetAllAccountsCheckedUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase.InsertAccountUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase.TransferUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase.UpdateAccountBalanceUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase.UpdateAccountDateFromUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase.UpdateAccountDateToUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase.UpdateAccountNameUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase.UpdateCheckedAccountUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.accountusecase.UpdateSpendingLimitAccountUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.GetSumTotalExpensesByDateUseCase
import carnerero.agustin.cuentaappandroid.main.domain.datastore.GetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.main.domain.datastore.SetCurrencyCodeUseCase
import carnerero.agustin.cuentaappandroid.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val getCurrencyCode: GetCurrencyCodeUseCase,
    private val setCurrencyCode: SetCurrencyCodeUseCase,
    private val addAccount: InsertAccountUseCase,
    private val getAccounts: GetAllAccountsUseCase,
    private val getAccountsChecked: GetAllAccountsCheckedUseCase,
    private val transferAmount: TransferUseCase,
    private val deleteAccount: DeleteAccountByIdUseCase,
    private val updateName: UpdateAccountNameUseCase,
    private val updateBalance: UpdateAccountBalanceUseCase,
    private val converterCurrency: ConvertCurrencyUseCase,
    private val getSumExpensesByAccount: GetSumTotalExpensesByDateUseCase,
    private val updateChecked: UpdateCheckedAccountUseCase,
    private val updateSpendingLimit: UpdateSpendingLimitAccountUseCase,
    private val updateFromDate: UpdateAccountDateFromUseCase,
    private val updateToDate: UpdateAccountDateToUseCase

) : ViewModel() {
    private val currencies=
        listOf(
            // Lista completa de todas las divisas del mundo, ordenadas alfabéticamente por código:

            Currency("AED", "UAE Dirham", R.drawable.sa),
            Currency("AFN", "Afghan Afghani", R.drawable.af),
            Currency("ALL", "Albanian Lek", R.drawable.al),
            Currency("AMD", "Armenian Dram", R.drawable.am),
            Currency("AOA", "Angolan Kwanza", R.drawable.ao),
            Currency("ARS", "Peso Argentino", R.drawable.ar),
            Currency("AUD", "Australian Dollar", R.drawable.au),
            Currency("AWG", "Aruban Florin", R.drawable.aw),
            Currency("AZN", "Azerbaijani Manat", R.drawable.az),
            Currency("BAM", "Bosnia-Herzegovina Convertible Mark", R.drawable.ba),
            Currency("BBD", "Barbadian Dollar", R.drawable.bb),
            Currency("BDT", "Bangladeshi Taka", R.drawable.bd),
            Currency("BGN", "Bulgarian Lev", R.drawable.bg),
            Currency("BHD", "Bahraini Dinar", R.drawable.bh),
            Currency("BIF", "Burundian Franc", R.drawable.bi),
            Currency("BMD", "Bermudian Dollar", R.drawable.bm),
            Currency("BND", "Brunei Dollar", R.drawable.bn),
            Currency("BOB", "Bolivian Boliviano", R.drawable.bo),
            Currency("BRL", "Real brasileño", R.drawable.br),
            Currency("BSD", "Bahamian Dollar", R.drawable.bs),
            Currency("BTN", "Bhutanese Ngultrum", R.drawable.bt),
            Currency("BWP", "Botswana Pula", R.drawable.bw),
            Currency("BYN", "Belarusian Ruble", R.drawable.by),
            Currency("BZD", "Belize Dollar", R.drawable.bz),
            Currency("CAD", "Canadian Dollar", R.drawable.ca),
            Currency("CDF", "Congolese Franc", R.drawable.cd),
            Currency("CHF", "Swiss Franc", R.drawable.ch),
            Currency("CLP", "Peso chileno", R.drawable.cl),
            Currency("CNY", "Yuan chino", R.drawable.cn),
            Currency("COP", "Peso colombiano", R.drawable.co),
            Currency("CRC", "Costa Rican Colón", R.drawable.cr),
            Currency("CUP", "Cuban Peso", R.drawable.cu),
            Currency("CVE", "Cape Verdean Escudo", R.drawable.cv),
            Currency("CZK", "Czech Koruna", R.drawable.cz),
            Currency("DJF", "Djiboutian Franc", R.drawable.dj),
            Currency("DKK", "Danish Krone", R.drawable.dk),
            Currency("DOP", "Dominican Peso", R.drawable.dom),
            Currency("DZD", "Algerian Dinar", R.drawable.dz),
            Currency("EGP", "Egyptian Pound", R.drawable.eg),
            Currency("ERN", "Eritrean Nakfa", R.drawable.er),
            Currency("ETB", "Ethiopian Birr", R.drawable.et),
            Currency("EUR", "Euro", R.drawable.eu),
            Currency("FJD", "Fijian Dollar", R.drawable.fj),
            Currency("FKP", "Falkland Islands Pound", R.drawable.fk),
            Currency("FOK", "Faroese Króna", R.drawable.fo),
            Currency("GBP", "British Pound", R.drawable.gb),
            Currency("GEL", "Georgian Lari", R.drawable.ge),
            Currency("GHS", "Ghanaian Cedi", R.drawable.gh),
            Currency("GIP", "Gibraltar Pound", R.drawable.gi),
            Currency("GMD", "Gambian Dalasi", R.drawable.gm),
            Currency("GNF", "Guinean Franc", R.drawable.gn),
            Currency("GTQ", "Guatemalan Quetzal", R.drawable.gt),
            Currency("GYD", "Guyanese Dollar", R.drawable.gy),
            Currency("HKD", "Hong Kong Dollar", R.drawable.hk),
            Currency("HNL", "Lempira de Honduras", R.drawable.hn),
            Currency("HRK", "Croatian Kuna", R.drawable.hr),
            Currency("HTG", "Haitian Gourde", R.drawable.ht),
            Currency("HUF", "Hungarian Forint", R.drawable.hu),
            Currency("IDR", "Indonesian Rupiah", R.drawable.id),
            Currency("ILS", "Israeli Shekel", R.drawable.il),
            Currency("INR", "Indian Rupee", R.drawable.`in`),
            Currency("IQD", "Iraqi Dinar", R.drawable.iq),
            Currency("IRR", "Iranian Rial", R.drawable.ir),
            Currency("ISK", "Icelandic Krona", R.drawable.`is`),
            Currency("JMD", "Jamaican Dollar", R.drawable.jm),
            Currency("JOD", "Jordanian Dinar", R.drawable.jo),
            Currency("JPY", "Yen japonés", R.drawable.jp),
            Currency("KES", "Kenyan Shilling", R.drawable.ke),
            Currency("KGS", "Kyrgyzstani Som", R.drawable.kg),
            Currency("KHR", "Cambodian Riel", R.drawable.kh),
            Currency("KMF", "Comorian Franc", R.drawable.km),
            Currency("KRW", "South Korean Won", R.drawable.kr),
            Currency("KWD", "Kuwaiti Dinar", R.drawable.kw),
            Currency("KYD", "Cayman Islands Dollar", R.drawable.ky),
            Currency("KZT", "Kazakhstani Tenge", R.drawable.kz),
            Currency("LAK", "Lao Kip", R.drawable.la),
            Currency("LBP", "Lebanese Pound", R.drawable.lb),
            Currency("LKR", "Sri Lankan Rupee", R.drawable.lk),
            Currency("LRD", "Liberian Dollar", R.drawable.lr),
            Currency("LSL", "Lesotho Loti", R.drawable.ls),
            Currency("LYD", "Libyan Dinar", R.drawable.ly),
            Currency("MAD", "Moroccan Dirham", R.drawable.ma),
            Currency("MDL", "Moldovan Leu", R.drawable.md),
            Currency("MGA", "Malagasy Ariary", R.drawable.mg),
            Currency("MKD", "Macedonian Denar", R.drawable.mk),
            Currency("MMK", "Burmese Kyat", R.drawable.mm),
            Currency("MNT", "Mongolian Tögrög", R.drawable.mn),
            Currency("MOP", "Macanese Pataca", R.drawable.mo),
            Currency("MRU", "Mauritanian Ouguiya", R.drawable.mr),
            Currency("MUR", "Mauritian Rupee", R.drawable.mu),
            Currency("MVR", "Maldivian Rufiyaa", R.drawable.mv),
            Currency("MWK", "Malawian Kwacha", R.drawable.mw),
            Currency("MXN", "Peso mexicano", R.drawable.mx),
            Currency("MYR", "Malaysian Ringgit", R.drawable.my),
            Currency("MZN", "Mozambican Metical", R.drawable.mz),
            Currency("NAD", "Namibian Dollar", R.drawable.na),
            Currency("NGN", "Nigerian Naira", R.drawable.ng),
            Currency("NIO", "Nicaraguan Córdoba", R.drawable.ni),
            Currency("NOK", "Norwegian Krone", R.drawable.no),
            Currency("NPR", "Nepalese Rupee", R.drawable.np),
            Currency("NZD", "New Zealand Dollar", R.drawable.nz),
            Currency("OMR", "Omani Rial", R.drawable.om),
            Currency("PAB", "Panamanian Balboa", R.drawable.pa),
            Currency("PEN", "Sol peruano", R.drawable.pe),
            Currency("PGK", "Papua New Guinean Kina", R.drawable.pg),
            Currency("PHP", "Peso filipino", R.drawable.ph),
            Currency("PKR", "Pakistani Rupee", R.drawable.pk),
            Currency("PLN", "Polish Zloty", R.drawable.pl),
            Currency("PYG", "Paraguayan Guarani", R.drawable.py),
            Currency("QAR", "Qatari Riyal", R.drawable.qa),
            Currency("RON", "Romanian Leu", R.drawable.ro),
            Currency("RSD", "Serbian Dinar", R.drawable.rs),
            Currency("RUB", "Russian Ruble", R.drawable.ru),
            Currency("RWF", "Rwandan Franc", R.drawable.rw),
            Currency("SAR", "Saudi Riyal", R.drawable.sa),
            Currency("SBD", "Solomon Islands Dollar", R.drawable.sb),
            Currency("SCR", "Seychellois Rupee", R.drawable.sc),
            Currency("SDG", "Sudanese Pound", R.drawable.sd),
            Currency("SEK", "Swedish Krona", R.drawable.se),
            Currency("SGD", "Singapore Dollar", R.drawable.sg),
            Currency("SHP", "Saint Helena Pound", R.drawable.sh),
            Currency("SLL", "Sierra Leonean Leone", R.drawable.sl),
            Currency("SOS", "Somali Shilling", R.drawable.so),
            Currency("SRD", "Surinamese Dollar", R.drawable.sr),
            Currency("SSP", "South Sudanese Pound", R.drawable.ss),
            Currency("STN", "São Tomé and Príncipe Dobra", R.drawable.st),
            Currency("SYP", "Syrian Pound", R.drawable.sy),
            Currency("SZL", "Eswatini Lilangeni", R.drawable.sz),
            Currency("THB", "Thai Baht", R.drawable.th),
            Currency("TJS", "Tajikistani Somoni", R.drawable.tj),
            Currency("TMT", "Turkmenistani Manat", R.drawable.tm),
            Currency("TND", "Tunisian Dinar", R.drawable.tn),
            Currency("TOP", "Tongan Paʻanga", R.drawable.to),
            Currency("TRY", "Turkish Lira", R.drawable.tr),
            Currency("TTD", "Trinidad and Tobago Dollar", R.drawable.tt),
            Currency("TWD", "New Taiwan Dollar", R.drawable.tw),
            Currency("TZS", "Tanzanian Shilling", R.drawable.tz),
            Currency("UAH", "Ukrainian Hryvnia", R.drawable.ua),
            Currency("UGX", "Ugandan Shilling", R.drawable.ug),
            Currency("USD", "US Dollar", R.drawable.us),
            Currency("UYU", "Uruguayan Peso", R.drawable.uy),
            Currency("UZS", "Uzbekistani Som", R.drawable.uz),
            Currency("VES", "Venezuelan Bolívar", R.drawable.ve),
            Currency("VND", "Vietnamese Dong", R.drawable.vn),
            Currency("VUV", "Vanuatu Vatu", R.drawable.vu),
            Currency("WST", "Samoan Tala", R.drawable.ws),
            Currency("XAF", "Central African CFA Franc", R.drawable.cf),
            Currency("XOF", "West African CFA Franc", R.drawable.wf),
            Currency("XPF", "CFP Franc", R.drawable.pf),
            Currency("YER", "Yemeni Rial", R.drawable.ye),
            Currency("ZAR", "South African Rand", R.drawable.za),
            Currency("ZMW", "Zambian Kwacha", R.drawable.zm),
            Currency("ZWL", "Zimbabwean Dollar", R.drawable.zw)
        )

    // Variable privada que representa un estado observable y modificable. Solo el ViewModel puede cambiar este valor,
    // y será utilizado para habilitar o deshabilitar el botón de cambiar nombre.
    private val _isEnableButton = MutableLiveData<Boolean>()
    // Variable pública expuesta como LiveData, de solo lectura para otros componentes. La UI observará este valor
    // para reaccionar a los cambios (por ejemplo, habilitar o deshabilitar el botón).
    val isEnableButton: LiveData<Boolean> = _isEnableButton

    // Flow que emite los gastos actuales y el límite para cada cuenta
    private val _expensePercentageFlow = MutableStateFlow<Map<Account, Float>>(emptyMap())
    val expensePercentageFlow: StateFlow<Map<Account, Float>> = _expensePercentageFlow.asStateFlow()


    private val _isEnableChangeNameButton = MutableLiveData<Boolean>()
    val isEnableChangeNameButton: LiveData<Boolean> = _isEnableChangeNameButton

    private val _isEnableChangeBalanceButton = MutableLiveData<Boolean>()
    val isEnableChangeBalanceButton: LiveData<Boolean> = _isEnableChangeBalanceButton

    private val _isConfirmTransfer = MutableLiveData<Boolean>()
    val isConfirmTransfer: LiveData<Boolean> = _isConfirmTransfer

    private val _isCurrencyExpanded = MutableLiveData<Boolean>()
    val isCurrencyExpanded: LiveData<Boolean> = _isCurrencyExpanded

    private val _enableCurrencySelector = MutableLiveData<Boolean>()
    val enableCurrencySelector: LiveData<Boolean> = _enableCurrencySelector

    private val _currencyCodeShowed = MutableLiveData<String>()
    val currencyCodeShowed: LiveData<String> = _currencyCodeShowed

    private val _currencyCodeSelected = MutableLiveData<String>()
    val currencyCodeSelected: LiveData<String> = _currencyCodeSelected

    // LiveData para los campos de texto
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _amount = MutableLiveData<String>()
    val amount: LiveData<String> = _amount

    // LiveData para los campos de cuentas modificadas
    private val _newName = MutableLiveData<String>()
    val newName: LiveData<String> = _newName

    private val _newAmount = MutableLiveData<String>()
    val newAmount: LiveData<String> = _newAmount

    //LiveData para cuenta seleccionada

    private val _accountSelected = MutableLiveData<Account?>()
    val accountSelected: MutableLiveData<Account?> = _accountSelected

    //LiveDatas de transferencia entre cuentas

    private val _destinationAccount = MutableLiveData<Account?>()
    val destinationAccount: MutableLiveData<Account?> = _destinationAccount

    private val _listOfAccounts = MutableLiveData<List<Account>>()
    val listOfAccounts: LiveData<List<Account>> = _listOfAccounts

    private val _listOfAccountsChecked = MutableLiveData<List<Account>>()
    val listOfAccountsChecked: LiveData<List<Account>> = _listOfAccountsChecked

    private val _currencyCodeList = MutableLiveData<List<Currency>>(currencies)
    val currencyCodeList: LiveData<List<Currency>> = _currencyCodeList

    //LiveData para textfield de categoria seleccionada para control de gasto

    private val _spendingLimit=MutableLiveData<String>()
    val spendingLimit: LiveData<String> = _spendingLimit

    private val _conversionCurrencyRate = MutableLiveData<Double>()
    val conversionCurrencyRate: LiveData<Double> = _conversionCurrencyRate

    private val _enableDialog=MutableLiveData<Boolean>()
    val enableDialog: LiveData<Boolean> = _enableDialog


    // Cargar datos iniciales que no dependen de la UI ni de la composición.
    init {
        viewModelScope.launch {
            _currencyCodeShowed.value =getCurrencyCode.invoke()
            getCurrencyCode()
            _isCurrencyExpanded.value = false
            onAccountUpdated()
           getListOfCurrencyCode()
            updateExpensePercentage()

        }
    }


    fun addAccount(account: Account) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    addAccount.invoke(account)  // Llama a la función para agregar la cuenta en IO
                }
                Log.d("Account", "Account created successfully")
                onAccountUpdated()
            } catch (e: Exception) {
                Log.e("Account", "Error creating account: ${e.message}", e)
                // Aquí puedes notificar a la UI del error usando un LiveData o StateFlow
            }
        }
    }

    fun transferAmount(accountFromId: Int, accountToId: Int, amount: Double) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    transferAmount.invoke(accountFromId, accountToId, amount)
                }
                onAccountUpdated()
            } catch (e: Exception) {
                Log.e("Transaction", "Error transferring amount: ${e.message}", e)
            }
        }
    }

    fun updateEntry(accountId: Int, amount: Double, isTransferDestination: Boolean) {
        viewModelScope.launch {
            try {
                val account = _accountSelected.value
                val accountDestination = _destinationAccount.value
                val newBalance = if (!isTransferDestination) {
                    (account?.balance ?: 0.0) + amount
                } else {
                    (accountDestination?.balance ?: 0.0) + amount
                }
                updateAccountBalance(accountId, newBalance)
            } catch (e: Exception) {
                Log.e("Accounts", "Error updating entry: ${e.message}", e)
            }
        }
    }

    private suspend fun updateAccountBalance(accountId: Int, newBalance: Double) {
        try {
            withContext(Dispatchers.IO) {
                updateBalance.invoke(accountId, newBalance)
            }
        } catch (e: Exception) {
            Log.e("Accounts", "Error updating account balance: ${e.message}", e)
        }
    }

    fun getAllAccounts() {
        viewModelScope.launch {
            try {
                val accounts = withContext(Dispatchers.IO) { getAccounts.invoke() }
                _listOfAccounts.postValue(accounts)
                Log.d("Accounts", "Fetched all accounts successfully")
            } catch (e: Exception) {
                Log.e("Accounts", "Error fetching all accounts: ${e.message}", e)
            }
        }
    }

    fun getAllAccountsChecked(){
        viewModelScope.launch {
            try {
                val accountsChecked = withContext(Dispatchers.IO) { getAccountsChecked.invoke() }
                _listOfAccountsChecked.postValue(accountsChecked)
            } catch (e: Exception) {
                Log.e("Accounts", "Error fetching all accounts checked: ${e.message}", e)
            }
        }
    }

    fun onDisableCurrencySelector(){
        _enableCurrencySelector.postValue(false)

    }

    fun resetFields() {
        _name.postValue("") // Vaciar el nombre de la cuenta
        _amount.postValue("") // Vaciar el balance de la cuenta
        _accountSelected.postValue(null)
        _destinationAccount.postValue(null)
        _isConfirmTransfer.postValue(false)
        _isEnableButton.postValue(false)

    }
    fun onEnableDialogChange(newValue:Boolean){
        _enableDialog.value = newValue
    }

    fun onCurrencyShowedChange(currencyShowed: String) {
        _currencyCodeShowed.value = currencyShowed


    }
    fun getCurrencyCode(){
        viewModelScope.launch(Dispatchers.IO) {
            _currencyCodeSelected.postValue(getCurrencyCode.invoke())
        }
    }

    fun onAccountSelected(accountSelected: Account) {
        _accountSelected.value = accountSelected
        val nameAccountSelected=accountSelected.name
        val amountAccountSelected=accountSelected.balance
        _newName.postValue(nameAccountSelected)
        _newAmount.postValue(amountAccountSelected.toString())
    }


    fun onDestinationAccountSelected(accountSelected: Account) {
        _destinationAccount.value = accountSelected
    }


    fun setCurrencyCode(currencyCode: String) {
        viewModelScope.launch {
            _currencyCodeSelected.value =currencyCode
            setCurrencyCode.invoke(currencyCode)

        }
    }


    fun getListOfCurrencyCode(): List<Currency> {
        // Sort the list by currency description, defaulting to an empty list if `currencies` is null
        val sortedCurrencies = currencies.sortedBy { it.currencyDescription }

        // Update the LiveData with the sorted list, if needed
        _currencyCodeList.value = sortedCurrencies

        return sortedCurrencies
    }


    fun onExpandedChange(newValue: Boolean) {
        _isCurrencyExpanded.value = newValue
    }



    fun onTextFieldsChanged(newName:String,newBalance: String) {
        if (Utils.isValidDecimal(newBalance)) {
            _amount.value = newBalance
        }
        _name.value = newName
        _isEnableButton.value=enableButton(newName,newBalance)
    }

    fun onTextNameChanged(newName:String){
        _newName.value = newName
        _isEnableChangeNameButton.value=true

    }

    fun onTextBalanceChanged(newBalance:String){
        if (Utils.isValidDecimal(newBalance)) {
            _newAmount.value = newBalance
        }

        _isEnableChangeBalanceButton.value=true
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    deleteAccount.invoke(account.id)
                }
                Log.d("Account", "Account deleted")
                onAccountUpdated()
            } catch (e: Exception) {
                Log.e("Account", "Error deleting account: ${e.message}", e)
            }
        }
    }
    fun onChangeSpendingLimit(newSpendingLimit:String){
        if (Utils.isValidDecimal(newSpendingLimit)) {
            _spendingLimit.value = newSpendingLimit
        }
        _spendingLimit.value = newSpendingLimit
    }

    fun upDateAccountName(idAccount:Int,newName:String){

        viewModelScope.launch(Dispatchers.IO) {
            updateName.invoke(idAccount,newName)
            Log.d("Account", "Name updated")
            _isEnableChangeNameButton.postValue(false)
            onAccountUpdated()
        }
    }
    fun upDateAccountBalance(idAccount:Int,newBalance:Double){
        viewModelScope.launch(Dispatchers.IO) {
             updateBalance.invoke(idAccount, newBalance)
            _isEnableChangeNameButton.postValue(false)
           onAccountUpdated()
        }
    }
    fun upDateAccountsDates(accountId:Int,fromDate:String,toDate:String){
        viewModelScope.launch(Dispatchers.IO) {
            updateFromDate.invoke(accountId, fromDate)
            updateToDate.invoke(accountId, toDate)

        }
    }

    fun updateCheckedAccount(accountId: Int, isChecked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            updateChecked.invoke(accountId, isChecked)
            getAllAccounts()
        }
    }
    fun upDateSpendingLimitAccount(accountId: Int, newAmount: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            updateSpendingLimit.invoke(accountId, newAmount)

        }
    }

    suspend fun conversionCurrencyRate(fromCurrency: String, toCurrency: String): Double? {

        return try {
            withContext(Dispatchers.IO) {
                val response = converterCurrency.invoke(fromCurrency, toCurrency)
                response.body()?.conversion_rate
            }
        }catch(e: IOException) {
            null
        }
    }

    suspend fun sumOfExpensesByAccount(accountId:Int,
                                        fromDate:String,
                                        toDate:String): Double? {

        return try {
            withContext(Dispatchers.IO) {
                val result=getSumExpensesByAccount.invoke(accountId,fromDate,toDate)
                result
            }
        }catch(e: IOException) {
            null
        }
    }

    // Función que actualiza el flujo con el porcentaje de gasto para cada categoría
    private suspend fun updateExpensePercentage() {
        getAllAccountsChecked()
        val categories = _listOfAccountsChecked.value // Método que obtiene todas las categorías
        // Verifica que las categorías no sean nulas
        if (categories.isNullOrEmpty()) {
            // Si las categorías son nulas o vacías, no se hace nada
            _expensePercentageFlow.value = emptyMap() // Asigna un mapa vacío
            return
        }

        val expensePercentageMap = categories.associateWith { account ->
            val expenses = sumOfExpensesByAccount(account.id,account.fromDate,account.toDate) ?: 0.0
            val percentage = (abs(expenses) / abs(account.spendingLimit)).toFloat().coerceIn(0.0f, 1.0f)
            percentage
        }
        _expensePercentageFlow.value = expensePercentageMap
    }
    // Función para refrescar el porcentaje de gasto (opcional)
    fun UpdateExpensePercentage() {
        viewModelScope.launch {
            updateExpensePercentage()
        }
    }

    private fun onAccountUpdated() {
        resetFields()
        getAllAccounts()
    }


    fun isValidExpense(amount: Double): Boolean = (_accountSelected.value?.balance ?: 0.0) >= amount

    private fun enableButton(description: String, amount: String): Boolean =
        description.isNotBlank() && amount.isNotEmpty() && description.isNotBlank() && amount.isNotBlank()

}



