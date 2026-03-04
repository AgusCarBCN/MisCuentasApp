package carnerero.agustin.cuentaappandroid.presentation.ui.records.get.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors


@Composable
fun ModifyRecords(
    records:List<RecordDTO>,
    currecyCode:String

) {


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.backgroundPrimary)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp), // Espacio entre elementos
        contentPadding = PaddingValues(16.dp) // Padding alrededor del contenido
    ) {
        items(records) { record ->
            RecordWithIcon(record,
                currecyCode)
        }
    }


}


