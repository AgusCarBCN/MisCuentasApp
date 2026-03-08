package carnerero.agustin.cuentaappandroid.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.math.BigDecimal
import java.util.Date

@Entity(
    tableName = "EntryEntity",
    foreignKeys = [
        ForeignKey(
            entity = Account::class, // Relación uno a muchos con Account
            parentColumns = ["id"], // La columna id de Account
            childColumns = ["accountId"], // Referencia a accountId en Entry
            onDelete = ForeignKey.CASCADE // Si se elimina una cuenta, se eliminan las entradas relacionadas
        ),
        ForeignKey(
            entity = Category::class, // Relación uno a muchos con Account
            parentColumns = ["id"], // La columna id de Account
            childColumns = ["categoryId"], // Referencia a accountId en Entry
            onDelete = ForeignKey.CASCADE // Si se elimina una cuenta, se eliminan las entradas relacionadas
        )
    ],
    indices = [
        Index(value = ["accountId"]),
        Index(value = ["categoryId"]),
        Index(value = ["date"])
    ]
)
data class Records(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "amount") var amount: BigDecimal= BigDecimal.ZERO,
    @ColumnInfo(name = "date") val date: String = Date().dateFormat(),
    @ColumnInfo(name = "categoryId") val categoryId: Int, // Relación uno a muchos con Account
    @ColumnInfo(name = "accountId") val accountId: Int // Relación uno a muchos con Account
)