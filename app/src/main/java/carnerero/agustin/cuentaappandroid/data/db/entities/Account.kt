package carnerero.agustin.cuentaappandroid.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.math.BigDecimal
import java.util.Date

@Entity("AccountEntity")
data class Account(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")  val id: Int = 0,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("balance")var balance: BigDecimal= BigDecimal.ZERO,
    @ColumnInfo(name = "isChecked") var isChecked: Boolean = false, // Indica si la cuenta está seleccionada para control de gastos
    @ColumnInfo(name = "periodSpendingLimit") var spendingLimit: BigDecimal = BigDecimal.ZERO, // Cantidad asociada a la cuenta de control de gastos
    @ColumnInfo(name = "fromDate") val fromDate: String = Date().dateFormat(),
    @ColumnInfo(name = "toDate") val toDate: String = Date().dateFormat()

)

