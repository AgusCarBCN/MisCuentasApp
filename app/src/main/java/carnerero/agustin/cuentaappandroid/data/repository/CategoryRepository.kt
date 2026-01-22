package carnerero.agustin.cuentaappandroid.data.repository

import carnerero.agustin.cuentaappandroid.data.db.dao.CategoryDao
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import java.math.BigDecimal
import javax.inject.Inject

class CategoryRepository  @Inject constructor(private val categoryDao: CategoryDao) {

    // 1. Insert category
    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)

    }

    // 2. List all categories by type
    suspend fun getAllCategories(type: CategoryType): List<Category> {
        return categoryDao.getAllCategories(type)
    }

    // 3. List all categories checked
    suspend fun getAllCategoriesChecked(type: CategoryType): List<Category> {
        return categoryDao.getAllCategoriesChecked(type)
    }

    // 4. Update checked category
    suspend fun updateCheckedCategory(categoryId:Int, newValue:Boolean) {
        categoryDao.updateCheckedCategory(categoryId,newValue)
    }

    // 5. Update amount category
    suspend fun updateSpendingLimitCategory(categoryId:Int,newAmount: BigDecimal) {
        categoryDao.updateSpendingLimitCategory(categoryId,newAmount)
    }


    // 6. Update from Date category
    suspend fun updateFromDateCategory(categoryId:Int,newDate:String) {
        categoryDao.updateFromDateCategory(categoryId,newDate)
    }

    // 7. Update to Date category
    suspend fun updateToDateCategory(categoryId:Int,newDate:String) {
        categoryDao.updateToDateCategory(categoryId,newDate)
    }
}
