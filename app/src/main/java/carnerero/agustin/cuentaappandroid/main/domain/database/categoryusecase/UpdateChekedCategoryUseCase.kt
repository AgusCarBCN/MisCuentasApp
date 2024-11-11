package carnerero.agustin.cuentaappandroid.main.domain.database.categoryusecase

import carnerero.agustin.cuentaappandroid.main.data.database.repository.CategoryRepository
import javax.inject.Inject

class UpdateCheckedCategoryUseCase  @Inject constructor(private val repository: CategoryRepository){

    suspend operator fun invoke(categoryId:Int,newValue:Boolean) {
        repository.updateCheckedCategory(categoryId, newValue)
    }
}