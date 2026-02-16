package carnerero.agustin.cuentaappandroid.presentation.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class RecordsFilter(val routeName: String) {
    object Expenses : RecordsFilter("Expenses")
    object Incomes : RecordsFilter("Incomes")
    data class RecordsByAccount(val accountId: Int) : RecordsFilter("RecordsByAccount")
}
