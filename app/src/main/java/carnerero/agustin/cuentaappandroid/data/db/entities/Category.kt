package carnerero.agustin.cuentaappandroid.data.db.entities

import androidx.room.*
import carnerero.agustin.cuentaappandroid.utils.dateFormat
import java.util.Date

@Entity(tableName = "CategoryEntity")
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "categoryType") val type: CategoryType, // Cambiamos a enum
    @ColumnInfo(name = "nameResource") val nameResource: Int, // Identificador del recurso de string para el nombre
    @ColumnInfo(name = "iconResource") val iconResource: Int, // Recurso de icono para la categoría
    @ColumnInfo(name = "isChecked") var isChecked: Boolean = false, // Indica si la categoría está seleccionada
    @ColumnInfo(name = "periodSpendingLimit") var spendingLimit: Double = 0.0, // Cantidad asociada a la categoría
    @ColumnInfo(name = "fromDate") val fromDate: String = Date().dateFormat(),
    @ColumnInfo(name = "toDate") val toDate: String = Date().dateFormat()

)
