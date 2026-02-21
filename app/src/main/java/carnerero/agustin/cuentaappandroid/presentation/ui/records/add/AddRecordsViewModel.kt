package carnerero.agustin.cuentaappandroid.presentation.ui.records.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carnerero.agustin.cuentaappandroid.domain.database.accountusecase.GetAllAccountsUseCase
import carnerero.agustin.cuentaappandroid.domain.database.categoryusecase.GetAllCategoriesByType
import carnerero.agustin.cuentaappandroid.domain.database.entriesusecase.InsertRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecordsViewModel @Inject constructor(
    private val getAllCategoriesByType: GetAllCategoriesByType,
    private val getAccounts: GetAllAccountsUseCase,
    private val addRecord: InsertRecordUseCase

    ): ViewModel(){

    private val _uiState = MutableStateFlow(AddRecordsUiState())
    val uiState: StateFlow<AddRecordsUiState> = _uiState

    private val _effect = MutableSharedFlow<AddRecordsEffects>()
    val effect = _effect.asSharedFlow()



}