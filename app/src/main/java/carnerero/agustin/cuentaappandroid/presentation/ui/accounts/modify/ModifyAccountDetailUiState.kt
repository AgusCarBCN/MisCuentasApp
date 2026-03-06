package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.modify



data class ModifyAccountDetailUiState(
    val name:String="",
    val balance: String="",

){
    val enableChangeButton=name.isNotBlank()
    val enableChangeBalance=balance.isNotBlank()
}
