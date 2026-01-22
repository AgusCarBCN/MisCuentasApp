package carnerero.agustin.cuentaappandroid.domain.database.categoryusecase

import carnerero.agustin.cuentaappandroid.data.repository.CategoryRepository
import java.math.BigDecimal
import javax.inject.Inject


class UpdateSpendingLimitCategoryUseCase @Inject constructor(private val repository: CategoryRepository){

    suspend operator fun invoke(categoryId:Int,newAmount: BigDecimal) {
        repository.updateSpendingLimitCategory(categoryId, newAmount)
    }
}