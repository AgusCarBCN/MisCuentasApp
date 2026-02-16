package carnerero.agustin.cuentaappandroid.presentation.ui.home

sealed class HomeUiEvents {

    object Idle: HomeUiEvents()
    object ShowIncomes: HomeUiEvents()
    object ShowExpenses: HomeUiEvents()
    data class ShowAllRecordsByAccount(val accountId:Int): HomeUiEvents()

}