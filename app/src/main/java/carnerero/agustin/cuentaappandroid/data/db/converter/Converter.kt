package carnerero.agustin.cuentaappandroid.data.db.converter

import androidx.room.TypeConverter
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import java.math.BigDecimal

class Converters {
    @TypeConverter
    fun fromCategoryType(categoryType: CategoryType): String = categoryType.name

    @TypeConverter
    fun toCategoryType(value: String): CategoryType = CategoryType.valueOf(value)

    // Para BigDecimal
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? = value?.let { BigDecimal(it) }
}
