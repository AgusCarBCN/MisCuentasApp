package carnerero.agustin.cuentaappandroid.entries

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.main.data.database.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Entry
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.DeleteEntryUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.GeAllEntriesUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.GetAllEntriesByAccountUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.GetAllEntriesDatabaseUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.GetAllExpensesUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.GetAllIncomesUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.GetFilteredEntriesUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.GetSumTotalExpensesByDateUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.GetSumTotalExpensesUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.GetSumTotalIncomesByDate
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.GetSumTotalIncomesUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.InsertEntryUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.UpdateAmountUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.UpdateEntriesAmountByExchangeRateUseCase
import carnerero.agustin.cuentaappandroid.main.domain.database.entriesusecase.UpdateEntryUseCase
import carnerero.agustin.cuentaappandroid.utils.Utils
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EntriesViewModel @Inject constructor(
    private val addEntry: InsertEntryUseCase,
    private val getTotalIncomes: GetSumTotalIncomesUseCase,
    private val getTotalExpenses: GetSumTotalExpensesUseCase,
    private val getAllEntriesDB: GetAllEntriesDatabaseUseCase,
    private val getAllIncomes: GetAllIncomesUseCase,
    private val getAllExpenses: GetAllExpensesUseCase,
    private val getFilteredEntries: GetFilteredEntriesUseCase,
    private val getAllEntriesByAccount: GetAllEntriesByAccountUseCase,
    private val getTotalIncomesByDate: GetSumTotalIncomesByDate,
    private val getTotalExpensesByDate: GetSumTotalExpensesByDateUseCase,
    private val updateAmountEntry: UpdateAmountUseCase,
    private val getAllEntriesDTO: GeAllEntriesUseCase,
    private val updateEntriesAmountByExchangeRate: UpdateEntriesAmountByExchangeRateUseCase,
    private val deleteEntry: DeleteEntryUseCase,
    private val updateEntry: UpdateEntryUseCase

    ) : ViewModel() {


    private val _totalIncomes = MutableLiveData<Double>()
    val totalIncomes: LiveData<Double> = _totalIncomes

    private val _totalExpenses = MutableLiveData<Double>()
    val totalExpenses: LiveData<Double> = _totalExpenses

    //LiveData para la habilitación del boton
    private val _enableConfirmButton = MutableLiveData<Boolean>()
    val enableConfirmButton: LiveData<Boolean> = _enableConfirmButton

    //LiveData para la habilitación del boton
    private val _enableConfirmTransferButton = MutableLiveData<Boolean>()
    val enableConfirmTransferButton: LiveData<Boolean> = _enableConfirmTransferButton

    //LiveData para la habilitación del boton
    private val _enableOptionList = MutableLiveData<Boolean>()
    val enableOptionList: LiveData<Boolean> = _enableOptionList

    // LiveData para los campos de texto
    private val _entryName = MutableLiveData<String>()
    val entryName: LiveData<String> = _entryName

    private val _entryAmount = MutableLiveData<String>()
    val entryAmount: LiveData<String> = _entryAmount


    // MutableStateFlow para la lista de entradas
    private val _listOfEntriesDTO = MutableStateFlow<List<EntryDTO>>(emptyList())
    val listOfEntriesDTO: StateFlow<List<EntryDTO>> = _listOfEntriesDTO.asStateFlow()

    // MutableStateFlow para la lista de entradas
    private val _listOfEntriesDB = MutableStateFlow<List<Entry>>(emptyList())
    val listOfEntriesDB: StateFlow<List<Entry>> = _listOfEntriesDB.asStateFlow()

    //MutableStateFlow de entrada seleccionada a modificar
    private val _entryDTOSelected = MutableLiveData<EntryDTO?>()
    val entryDTOSelected: LiveData<EntryDTO?> = _entryDTOSelected


    init{
        getTotal()
        getAllEntriesDTO()
    }

    fun getAllEntriesDTO(){
        viewModelScope.launch(Dispatchers.IO) {
            flow {
                val entries = getAllEntriesDTO.invoke()
                emit(entries)
            }
                .catch { e ->
                    // Manejo de errores
                    _listOfEntriesDTO.value = emptyList() // O alguna acción que maneje el error
                    Log.e("ViewModel", "Error getting incomes: ${e.message}")
                }
                .collect { entries ->
                    _listOfEntriesDTO.value = entries
                }
        }
    }
    fun getFilteredEntries(accountId: Int,
                           description:String,
                           dateFrom: String = Date().dateFormat(),
                           dateTo: String = Date().dateFormat(),
                           amountMin: Double = 0.0,
                           amountMax: Double = Double.MAX_VALUE,
                           selectedOptions: Int = 0)

    {
        viewModelScope.launch(Dispatchers.IO) {
            flow {
                val entries = getFilteredEntries.invoke(accountId,
                    description,
                    dateFrom,
                    dateTo,
                    amountMin,
                    amountMax,
                    selectedOptions)
                emit(entries)
            }
                .catch { e ->
                    // Manejo de errores
                    _listOfEntriesDB.value = emptyList() // O alguna acción que maneje el error
                    Log.e("ViewModel", "Error getting incomes: ${e.message}")
                }
                .collect { entries ->
                    _listOfEntriesDTO.value = entries
                }
        }
    }

   fun getAllEntriesDataBase(){

        viewModelScope.launch(Dispatchers.IO) {
            flow {
                val entries = getAllEntriesDB.invoke()
                emit(entries)
            }
                .catch { e ->
                    // Manejo de errores
                    _listOfEntriesDB.value = emptyList() // O alguna acción que maneje el error
                    Log.e("ViewModel", "Error getting incomes: ${e.message}")
                }
                .collect { entries ->
                    _listOfEntriesDB.value = entries
                }
        }
    }
    // Método para obtener todos los ingresos
    fun getAllIncomes() {
        viewModelScope.launch(Dispatchers.IO) {
            flow {
                val entries = getAllIncomes.invoke()
                emit(entries)
            }
                .catch { e ->
                    // Manejo de errores
                    _listOfEntriesDTO.value = emptyList() // O alguna acción que maneje el error
                    Log.e("ViewModel", "Error getting incomes: ${e.message}")
                }
                .collect {/* entries ->
                    _listOfEntries.value = entries*/
                    _listOfEntriesDTO.value = it
                }

        }

    }

    // Método para obtener todos los gastos
    fun getAllExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            flow {
                val entries = getAllExpenses.invoke()
                emit(entries)
            }
                .catch { e ->
                    _listOfEntriesDTO.value = emptyList()
                    Log.e("ViewModel", "Error getting expenses: ${e.message}")
                }
                .collect { entries ->
                    _listOfEntriesDTO.value = entries
                }
        }
    }

    // Método para obtener todas las entradas por cuenta
    fun getAllEntriesByAccount(accountId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            flow {
                val entries = getAllEntriesByAccount.invoke(accountId)
                emit(entries)
            }
                .catch { e ->
                    _listOfEntriesDTO.value = emptyList()
                    Log.e("ViewModel", "Error getting entries by account $accountId: ${e.message}")
                }
                .collect { entries ->
                    _listOfEntriesDTO.value = entries
                }
        }
    }


    fun addEntry(entry: Entry) {
        viewModelScope.launch(Dispatchers.IO) {

                addEntry.invoke(entry)
                if (entry.amount >= 0.0) {
                    getTotalIncomes()
                } else {
                    getTotalExpenses()
                }
            resetFields()
            getTotal()
        }
    }
    fun deleteEntry(entry:EntryDTO) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteEntry.invoke(entry)
            getTotal()
        }
    }
    fun updateEntry(id: Long, description: String, amount: Double, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateEntry.invoke(id,description,amount,date)

            } catch (e: Exception) {
                Log.e("EntryDTO", "Error updating entry: ${e.message}")
            }
        }
    }
    fun onEntryDTOSelected(entryDTO:EntryDTO)
    {
        _entryDTOSelected.value = entryDTO
    }

    fun updateEntriesAmountByExchangeRate(rate:Double){
        viewModelScope.launch(Dispatchers.IO) {
            updateEntriesAmountByExchangeRate.invoke(rate)
        }
    }
    fun onTextFieldsChanged(newDescription:String,newAmount:String){
        // Validar y actualizar el valor de amount
        if (Utils.isValidDecimal(newAmount)) {
            _entryAmount.value = newAmount
        }
        _entryName.value=newDescription
        _enableConfirmButton.value = enableButton(newDescription,newAmount)
    }

    fun onAmountChanged(idAccountFrom:Int,idAccountTo:Int,newAmount: String) {
        // Validar y actualizar el valor de amount
        if (Utils.isValidDecimal(newAmount)) {
            _entryAmount.value = newAmount
        }
        _enableConfirmTransferButton.value = enableButtonTransfer(idAccountFrom,idAccountTo,newAmount)

    }
    fun onEnableByDate(newValue:Boolean){
        _enableOptionList.postValue(newValue)
    }


    fun onChangeTransferButton(newValue:Boolean){
        _enableConfirmTransferButton.postValue(newValue)
    }
    fun upDateAmountEntry(entryId:Long,newAmount:Double){
        viewModelScope.launch(Dispatchers.IO){
            updateAmountEntry.invoke(entryId,newAmount)
        }
    }


    fun getTotal() {
        viewModelScope.launch(Dispatchers.IO) {
            // Ejecutar ambas funciones en paralelo
            val totalIncomesDeferred = async { getTotalIncomes.invoke() }
            val totalExpensesDeferred = async { getTotalExpenses.invoke() }

            // Esperar los resultados
            val totalIncomes = totalIncomesDeferred.await()
            val totalExpenses = totalExpensesDeferred.await()

            // Publicar los resultados en LiveData
            _totalIncomes.postValue(totalIncomes)
            _totalExpenses.postValue(totalExpenses)
        }
    }

    fun getTotalByDate(accountId:Int,fromDate:String,toDate:String){
        viewModelScope.launch(Dispatchers.IO) {
            // Ejecutar ambas funciones en paralelo
            val totalIncomesDeferred = async { getTotalIncomesByDate.invoke(accountId,fromDate,toDate) }
            val totalExpensesDeferred = async { getTotalExpensesByDate.invoke(accountId,fromDate,toDate) }

            // Esperar los resultados
            val totalIncomes = totalIncomesDeferred.await()
            val totalExpenses = totalExpensesDeferred.await()

            // Publicar los resultados en LiveData
            _totalIncomes.postValue(totalIncomes)
            _totalExpenses.postValue(totalExpenses)
        }
    }


    private fun resetFields() {
        _entryName.postValue("") // Vaciar el nombre de la cuenta
        _entryAmount.postValue("") // Vaciar el balance de la cuenta
        _enableConfirmButton.postValue(false) //deshabilitar boton de confirmar
    }


    private fun enableButton(description: String, amount: String): Boolean =
        description.isNotBlank() && amount.isNotEmpty() && description.isNotBlank() && amount.isNotBlank()

    private fun enableButtonTransfer( idAccountFrom:Int,idAccountTo:Int,amount: String): Boolean =
        amount.isNotEmpty() && amount.isNotBlank() && idAccountFrom!=idAccountTo


}
