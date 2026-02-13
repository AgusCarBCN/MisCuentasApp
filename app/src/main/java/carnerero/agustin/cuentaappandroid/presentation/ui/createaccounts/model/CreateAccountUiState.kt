package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model

data class CreateAccountUiState(
    val accountName: String = "",
    val balance: String = "",
    val currencies: List<Currency> = emptyList(),
    val selectedCurrency: Currency? = null
) {
    val isFormValid: Boolean
        get() = accountName.isNotBlank()
                && balance.toBigDecimalOrNull() != null
                && selectedCurrency != null

    var isExpanded: Boolean = false

}