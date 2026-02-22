package carnerero.agustin.cuentaappandroid.utils.mapper

import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Records

class EntryMapper {

    fun entryDtoToEntry(dto: RecordDTO): Records {
        return Records(
            id=dto.id,
            description = dto.description,
            amount = dto.amount,
            date = dto.date,
            categoryId = dto.iconResource,
            accountId =dto.accountId
        )
    }

}