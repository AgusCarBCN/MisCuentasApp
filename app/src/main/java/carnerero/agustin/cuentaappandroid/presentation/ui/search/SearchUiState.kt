package carnerero.agustin.cuentaappandroid.presentation.ui.search

import carnerero.agustin.cuentaappandroid.R

data class SearchFilterUiState(
    val options: List<Int> = listOf(R.string.incomeoption, R.string.expenseoption, R.string.alloption),
    val selectedOptionIndex: Int = 0,        // índice seleccionado por defecto
    val showDatePickerFrom: Boolean = false, // controla el date picker "from"
    val showDatePickerTo: Boolean = false,   // controla el date picker "to"
    val selectedFromDate: String = "",       // fecha desde
    val selectedToDate: String = "",         // fecha hasta
    val entryDescription: String = "",       // descripción
    val fromAmount: String = "",             // monto desde
    val toAmount: String = "",               // monto hasta
    val enableSearchButton: Boolean = false,  // controla si el botón de buscar está habilitado

)
