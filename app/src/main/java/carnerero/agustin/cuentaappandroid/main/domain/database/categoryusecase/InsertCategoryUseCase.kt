package carnerero.agustin.cuentaappandroid.main.domain.database.categoryusecase

import carnerero.agustin.cuentaappandroid.main.data.database.entities.Category
import carnerero.agustin.cuentaappandroid.main.data.database.repository.CategoryRepository
import javax.inject.Inject

class InsertCategoryUseCase  @Inject constructor(private val repository: CategoryRepository){

    suspend operator fun invoke(newCategory: Category) {
        repository.insertCategory(newCategory)
    }
}