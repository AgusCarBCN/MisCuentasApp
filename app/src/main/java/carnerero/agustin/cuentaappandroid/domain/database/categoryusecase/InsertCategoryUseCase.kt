package carnerero.agustin.cuentaappandroid.domain.database.categoryusecase

import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.data.repository.CategoryRepository
import javax.inject.Inject

class InsertCategoryUseCase  @Inject constructor(private val repository: CategoryRepository){

    suspend operator fun invoke(newCategory: Category) {
        repository.insertCategory(newCategory)
    }
}