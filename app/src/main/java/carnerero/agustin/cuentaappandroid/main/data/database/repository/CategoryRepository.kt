package carnerero.agustin.cuentaappandroid.main.data.database.repository

import carnerero.agustin.cuentaappandroid.main.data.database.dao.CategoryDao
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Category
import carnerero.agustin.cuentaappandroid.main.data.database.entities.CategoryType
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
    suspend fun updateSpendingLimitCategory(categoryId:Int,newAmount:Double) {
        categoryDao.updateSpendingLimitCategory(categoryId,newAmount)
    }

    // 6. Update limitMax category
    suspend fun updateLimitMaxCategory(categoryId:Int,newLimitMax:Float) {
        categoryDao.updateLimitMaxCategory(categoryId,newLimitMax)
    }

    // 7. Update from Date category
    suspend fun updateFromDateCategory(categoryId:Int,newDate:String) {
        categoryDao.updateFromDateCategory(categoryId,newDate)
    }

    // 8. Update to Date category
    suspend fun updateToDateCategory(categoryId:Int,newDate:String) {
        categoryDao.updateToDateCategory(categoryId,newDate)
    }
}
