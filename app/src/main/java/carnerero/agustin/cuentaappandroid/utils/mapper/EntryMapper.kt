package carnerero.agustin.cuentaappandroid.utils.mapper

import carnerero.agustin.cuentaappandroid.data.db.dto.EntryDTO
import carnerero.agustin.cuentaappandroid.data.db.entities.Entry

class EntryMapper {

    public fun entryDtoToEntry(dto: EntryDTO): Entry {
        return Entry(
            id=dto.id,
            description = dto.description,
            amount = dto.amount,
            date = dto.date,
            categoryId = dto.iconResource,
            accountId =dto.accountId
        )
    }

}