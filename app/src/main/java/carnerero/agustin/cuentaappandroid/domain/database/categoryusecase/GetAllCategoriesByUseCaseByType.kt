package carnerero.agustin.cuentaappandroid.domain.database.categoryusecase

import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType
import carnerero.agustin.cuentaappandroid.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllCategoriesByType @Inject constructor(private val repository: CategoryRepository){

    operator fun invoke(type: CategoryType): Flow<List<Category>> {
        return repository.getAllCategories(type)
    }
}