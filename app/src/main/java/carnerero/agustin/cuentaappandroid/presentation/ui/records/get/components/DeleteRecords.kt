package carnerero.agustin.cuentaappandroid.presentation.ui.records.get.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.dto.RecordDTO
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import carnerero.agustin.cuentaappandroid.presentation.ui.records.get.model.RecordDataCheckBox
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting

@Composable
fun DeleteRecords(
   records:List<RecordDTO>,
   currencyCode:String,
   deleteRecord:(RecordDTO)->Unit
) {

    // Sincronizar listOfEntriesWithCheckBox con listOfEntries:
    // remember(listOfEntries):
    // - Al observar listOfEntries, remember reconstruye listOfEntriesWithCheckBox cada vez que listOfEntries cambia.
    // - Esto asegura que los datos estén siempre sincronizados con la fuente original (listOfEntries).
    // map y toMutableStateList:
    // - map crea una nueva lista de EntryWithCheckBox, donde cada elemento de listOfEntries se asocia a un checkbox inicializado en false.
    // - toMutableStateList convierte esa lista en un estado observable para que las actualizaciones dinámicas funcionen en la interfaz de manera reactiva.
    val listOfEntriesWithCheckBox = remember(records) {
        records.map { RecordDataCheckBox(it, false) }.toMutableStateList()
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (records.isNotEmpty()) {
            HeadSetting(
                title = stringResource(id = R.string.selectentriesToDelete),
                MaterialTheme.typography.titleLarge
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.recordsnotfound),
                    color = colors.textColor,
                    textAlign = TextAlign.Center,
                    fontSize = with(LocalDensity.current) {
                        dimensionResource(id = R.dimen.text_body_extra_large).toSp()
                    }
                )
            }

        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(colors.backgroundPrimary)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), // Espacio entre elementos
            contentPadding = PaddingValues(16.dp) // Padding alrededor del contenido
        ) {
            items(listOfEntriesWithCheckBox) { entry ->
                RecordWithCheckBox(
                    entry.entry,
                    currencyCode,
                    entry.checkbox,
                    onSelectionChange = {
                        val index = listOfEntriesWithCheckBox.indexOf(entry)
                        if (index != -1) {
                            listOfEntriesWithCheckBox[index] =
                                entry.copy(checkbox = !entry.checkbox)
                        }

                    }
                )
            }
        }
        if (records.isNotEmpty()) {
            ModelButton(
                text = stringResource(
                    id = R.string.deleteButton
                ),
                MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(320.dp),
                true,
                onClickButton = {
                    val entriesToRemove =
                        listOfEntriesWithCheckBox.filter { it.checkbox } // Filtra los elementos a eliminar
                    if (entriesToRemove.isNotEmpty()) {
                         entriesToRemove.forEach { entryWithCheckBox ->
                            deleteRecord(entryWithCheckBox.entry)
                           // listOfEntriesWithCheckBox.remove(entryWithCheckBox) // Modifica la lista original

                        }
                    }
                }
            )
        }
    }
}
