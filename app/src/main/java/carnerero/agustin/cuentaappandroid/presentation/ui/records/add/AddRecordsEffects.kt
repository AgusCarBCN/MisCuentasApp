package carnerero.agustin.cuentaappandroid.presentation.ui.records.add


sealed class AddRecordsEffects{
    object Idle: AddRecordsEffects()
    object NewIncomeMessage: AddRecordsEffects()
    object NewExpenseMessage: AddRecordsEffects()
    object AccountsNotFoundMessage: AddRecordsEffects()
    object AmountOverBalanceMessage: AddRecordsEffects()

}
