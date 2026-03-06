package carnerero.agustin.cuentaappandroid.notification

import carnerero.agustin.cuentaappandroid.data.db.entities.Account
import carnerero.agustin.cuentaappandroid.data.db.entities.Category

data class NotificationUiState(
    val accountsChecked: List<Account> = emptyList(),
    val categoriesChecked: List<Category> = emptyList(),
    val expensePercentageByCategory: Map<Category, Float> = emptyMap(),
    val expensePercentageByAccount: Map<Account, Float> = emptyMap(),
    val currencyCode:String ="EUR"
)
