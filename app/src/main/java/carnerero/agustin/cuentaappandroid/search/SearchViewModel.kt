package carnerero.agustin.cuentaappandroid.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.utils.Utils
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.util.Date
import javax.inject.Inject

class SearchViewModel @Inject constructor() : ViewModel() {

    // Lista de opciones como identificadores de recursos
    val options = listOf(R.string.incomeoption, R.string.expenseoption, R.string.alloption)

    // Almacena el índice de la opción seleccionada
    private val _selectedOptionIndex =
        MutableLiveData(0) // Por defecto, selecciona la primera opción
    val selectedOptionIndex: LiveData<Int> = _selectedOptionIndex

    private val _showDatePickerFrom = MutableLiveData<Boolean>()
    val showDatePickerFrom: LiveData<Boolean> = _showDatePickerFrom

    private val _showDatePickerTo = MutableLiveData<Boolean>()
    val showDatePickerTo: LiveData<Boolean> = _showDatePickerTo

    private val _selectedFromDate = MutableLiveData<String>()
    val selectedFromDate: LiveData<String> = _selectedFromDate

    private val _selectedToDate = MutableLiveData<String>()
    val selectedToDate: LiveData<String> = _selectedToDate

    private val _entryDescription = MutableLiveData<String>()
    val entryDescription: LiveData<String> = _entryDescription

    private val _fromAmount = MutableLiveData<String>()
    val fromAmount: LiveData<String> = _fromAmount

    private val _toAmount = MutableLiveData<String>()
    val toAmount: LiveData<String> = _toAmount

    private val _enableSearchButton = MutableLiveData<Boolean>()
    val enableSearchButton: LiveData<Boolean> = _enableSearchButton



    init {
        resetFields()
    }

    // Método para actualizar la opción seleccionada
    fun onOptionSelected(index: Int) {
        _selectedOptionIndex.value = index
    }

    fun onShowDatePicker(newValue: Boolean, isDateFrom: Boolean) {
        if (isDateFrom) {
            _showDatePickerFrom.value = newValue
        } else {
            _showDatePickerTo.value = newValue
        }
    }


    fun onSelectedDate(date: String, isDateFrom: Boolean) {

        if (isDateFrom) {
            _selectedFromDate.value = date
        } else {
            _selectedToDate.value = date
        }
    }



    fun onAmountsFieldsChange(fromAmount: String, toAmount: String) {
        // Validar y actualizar el valor de amount
        if (Utils.isValidDecimal(fromAmount)) {
            _fromAmount.value = fromAmount
        }
        if (Utils.isValidDecimal(toAmount)) {
            _toAmount.value = toAmount
        }
        if (validateAmounts(fromAmount, toAmount)) {
            _enableSearchButton.value = true
        } else {
            _enableSearchButton.value = false
        }
    }


    fun onEntryDescriptionChanged(newDescription: String) {
        _entryDescription.value = newDescription
    }

    // Método para actualizar la opción seleccionada
    /* fun onOptionSelected(option: Int) {
         _selectedOption.value = option
     }*/

    fun onEnableSearchButton() {
        val fromDate = _selectedFromDate.value ?: Date().dateFormat()
        val toDate = _selectedToDate.value ?: Date().dateFormat()
        val fromAmount = _fromAmount.value ?: "0.0"
        val toAmount = _toAmount.value ?: "0.0"
        val enable = validateDates() && validateAmounts(fromAmount, toAmount)
        _enableSearchButton.value = enable

    }


    fun validateDates(): Boolean {
        val fromDateString = _selectedFromDate.value ?: Date().dateFormat()
        val toDateString = _selectedToDate.value ?: Date().dateFormat()
        val fromDate = Utils.convertStringToLocalDate(fromDateString)
        val toDate = Utils.convertStringToLocalDate(toDateString)
        return fromDate.isBefore(toDate) || fromDate.isEqual(toDate)
    }

    fun validateAmounts(fromAmount: String, toAmount: String): Boolean {
        val from = _fromAmount.value?.toDoubleOrNull() ?: 0.0
        val to = _toAmount.value?.toDoubleOrNull() ?: 0.0
        return to >= from
    }

    fun resetFields() {
        _selectedOptionIndex.value = 0
        _showDatePickerFrom.value = false
        _showDatePickerTo.value = false
        _selectedFromDate.value = Date().dateFormat()
        _selectedToDate.value = Date().dateFormat()
        _entryDescription.value = ""
        _fromAmount.value = ""
        _toAmount.value = ""
        _enableSearchButton.value = false
    }



}
