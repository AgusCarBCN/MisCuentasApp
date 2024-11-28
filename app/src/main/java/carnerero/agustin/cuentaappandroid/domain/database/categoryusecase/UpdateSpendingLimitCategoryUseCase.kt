package carnerero.agustin.cuentaappandroid.domain.database.categoryusecase

import carnerero.agustin.cuentaappandroid.data.repository.CategoryRepository
import javax.inject.Inject


class UpdateSpendingLimitCategoryUseCase @Inject constructor(private val repository: CategoryRepository){

    suspend operator fun invoke(categoryId:Int,newAmount:Double) {
        repository.updateSpendingLimitCategory(categoryId, newAmount)
    }
}