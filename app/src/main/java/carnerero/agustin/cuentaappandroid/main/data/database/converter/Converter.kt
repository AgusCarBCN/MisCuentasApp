package carnerero.agustin.cuentaappandroid.main.data.database.converter

import androidx.room.TypeConverter
import carnerero.agustin.cuentaappandroid.main.data.database.entities.CategoryType

class Converters {
    @TypeConverter
    fun fromCategoryType(categoryType: CategoryType): String = categoryType.name

    @TypeConverter
    fun toCategoryType(value: String): CategoryType = CategoryType.valueOf(value)
}
