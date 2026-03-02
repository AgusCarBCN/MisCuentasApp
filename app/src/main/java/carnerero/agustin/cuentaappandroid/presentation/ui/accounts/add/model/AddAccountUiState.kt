package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.model

data class AddAccountUiState(
    val accountName: String = "",
    val balance: String = "",
    val currencies: List<Currency> = emptyList(),
    val selectedCurrency: String = "EUR",
    val isDropdownExpanded: Boolean = false) {
    val isFormValid: Boolean
        get() = accountName.isNotBlank()
                && balance.toBigDecimalOrNull() != null


}