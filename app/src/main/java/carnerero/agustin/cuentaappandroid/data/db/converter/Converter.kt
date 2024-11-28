package carnerero.agustin.cuentaappandroid.data.db.converter

import androidx.room.TypeConverter
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType

class Converters {
    @TypeConverter
    fun fromCategoryType(categoryType: CategoryType): String = categoryType.name

    @TypeConverter
    fun toCategoryType(value: String): CategoryType = CategoryType.valueOf(value)
}
