package carnerero.agustin.cuentaappandroid.presentation.ui.records.modify

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.UpdateBalanceEditRecordUseCase
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.UpdateEntryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordDetailViewModel @Inject constructor(
    private val updateRecord: UpdateEntryUseCase,
    private val updateBalance: UpdateBalanceEditRecordUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow(RecordDetailUiState())
    val uiState: StateFlow<RecordDetailUiState> = _uiState

    private val _effect = MutableSharedFlow<RecordDetailEffects>()
    val effect = _effect.asSharedFlow()

    fun onUserEvent(event: RecordDetailUiEvent){
        when(event){
            is RecordDetailUiEvent.Modify -> modifyRecord(event.record)
            is RecordDetailUiEvent.OnAmountChange -> onAmountChange(event.newAmount)
            is RecordDetailUiEvent.OnDateChange -> onDateChange(event.newDate)
            is RecordDetailUiEvent.OnDescriptionChange -> onDescriptionChange(event.newDescription)
            is RecordDetailUiEvent.OnShowDatePicker -> onShowDatePicker(event.visible)
        }
    }
    fun onAmountChange(newValue: String){
        _uiState.update { current->
            current.copy(
                amount=newValue
            )
        }
    }
    fun onShowDatePicker(newValue: Boolean){
        _uiState.update { current->
            current.copy(
                showDatePicker = newValue
            )
        }
    }
    fun onDateChange(newDate: String){
        _uiState.update { current->
            current.copy(
                date = newDate
            )
        }
    }
    fun onDescriptionChange(newDescription: String){
        _uiState.update { current->
            current.copy(
                description = newDescription
            )
        }
    }

    fun modifyRecord(record: RecordDTO) {
        viewModelScope.launch {
            val state = _uiState.value
            val inputAmount = state.amount.toBigDecimal()
            // Convertimos a valor real según tipo
            val newAmount = if (record.categoryType == CategoryType.INCOME) {
                inputAmount
            } else {
                inputAmount.negate()
            }
            val amountBefore = record.amount
            // Si el valor no cambia, no hacemos nada
            if (newAmount.compareTo(amountBefore) != 0) {
                val diffAmount = newAmount.subtract(amountBefore)
                Log.d("MODIFY", "diff:$diffAmount")
                // Actualizamos registro
                updateRecord.invoke(
                    record.id,
                    state.description,
                    newAmount,
                    state.date
                )
                // Actualizamos balance solo con la diferencia real
                updateBalance.invoke(record.accountId, diffAmount)

                // Emitimos efecto de éxito
                _effect.emit(RecordDetailEffects.MessageUpdateRecord)
            }
        }
    }
    fun getInitValues(record: RecordDTO){
        viewModelScope.launch {
            val amount=record.amount.abs().toString()
            _uiState.update { current->
                current.copy(
                    amount = amount,
                    date=record.date,
                    description = record.description
                )
            }
        }
    }

}