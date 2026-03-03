package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.modify

sealed class ModifyAccountsEffects {
    object MessageNameChange: ModifyAccountsEffects()
    object MessageBalanceChange:ModifyAccountsEffects()
}