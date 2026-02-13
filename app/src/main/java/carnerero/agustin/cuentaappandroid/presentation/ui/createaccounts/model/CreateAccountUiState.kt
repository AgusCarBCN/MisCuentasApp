package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model

data class CreateAccountUiState(
    val accountName: String = "",
    val balance: String = "",
    val currencies: List<Currency> = emptyList(),
    val selectedCurrency: String = "EUR",
    val isDropdownExpanded: Boolean = false) {
    val isFormValid: Boolean
        get() = accountName.isNotBlank()
                && balance.toBigDecimalOrNull() != null


}