package carnerero.agustin.cuentaappandroid.main.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import carnerero.agustin.cuentaappandroid.main.data.database.entities.Category
import carnerero.agustin.cuentaappandroid.main.data.database.entities.CategoryType

@Dao
interface CategoryDao {


    // 1. Add category
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    // 2. Get all categories by type
    @Query("SELECT * FROM CategoryEntity WHERE categoryType= :type")
    suspend fun getAllCategories(type: CategoryType): List<Category>

    // 3. Get all categories checked
    @Query("SELECT * FROM CategoryEntity WHERE isChecked= 1 AND categoryType= :type")
    suspend fun getAllCategoriesChecked(type: CategoryType): List<Category>

    // 4. Update checked state category
    @Query("UPDATE CategoryEntity SET isChecked = :newValueChecked WHERE id = :categoryId")
    suspend fun updateCheckedCategory(categoryId: Int, newValueChecked: Boolean)

    // 5. Update periodSpendingLimit category
    @Query("UPDATE CategoryEntity SET periodSpendingLimit = :newAmount WHERE id = :categoryId")
    suspend fun updateSpendingLimitCategory(categoryId: Int, newAmount:Double)

   // 6. Update date From  category control expense
    @Query("UPDATE CategoryEntity SET fromDate = :newDate WHERE id = :categoryId")
    suspend fun updateFromDateCategory(categoryId:Int,newDate: String)

    // 7. Update date to  category control expense
    @Query("UPDATE CategoryEntity SET toDate = :newDate WHERE id = :categoryId")
    suspend fun updateToDateCategory(categoryId:Int,newDate: String)

}