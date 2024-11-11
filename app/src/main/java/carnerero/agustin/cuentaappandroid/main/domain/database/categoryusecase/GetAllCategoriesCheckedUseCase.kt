package carnerero.agustin.cuentaappandroid.main.domain.database.categoryusecase

import carnerero.agustin.cuentaappandroid.main.data.database.entities.Category
import carnerero.agustin.cuentaappandroid.main.data.database.entities.CategoryType
import carnerero.agustin.cuentaappandroid.main.data.database.repository.CategoryRepository
import javax.inject.Inject

class GetAllCategoriesCheckedUseCase  @Inject constructor(private val repository: CategoryRepository){

    suspend operator fun invoke(type: CategoryType): List<Category> {
        return repository.getAllCategoriesChecked(type)
    }
}