package carnerero.agustin.cuentaappandroid.domain.database.categoryusecase

import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.data.repository.CategoryRepository
import javax.inject.Inject


class GetAllCategoriesByType @Inject constructor(private val repository: CategoryRepository){

    suspend operator fun invoke(type: CategoryType): List<Category> {
        return repository.getAllCategories(type)
    }
}