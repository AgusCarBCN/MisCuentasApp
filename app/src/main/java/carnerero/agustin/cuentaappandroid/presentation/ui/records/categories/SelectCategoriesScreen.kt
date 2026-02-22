package carnerero.agustin.cuentaappandroid.presentation.ui.records.categories

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.CategoryEntries
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.data.db.entities.CategoryType

@Composable
fun SelectCategoriesScreen(
    selectCategoriesViewModel: SelectCategoriesViewModel,
    type: CategoryType,
    navToAddRecords: () -> Unit
) {
    // 🔹 Estado de la UI
    val state by selectCategoriesViewModel.uiState.collectAsStateWithLifecycle()
    val categories = state.categories

    // 🔹 LaunchedEffect para cargar categorías según el tipo
    LaunchedEffect(type) {
        // Esto se ejecuta **una sola vez cada vez que cambia 'type'**
        // Por ejemplo: Income -> Expense
        selectCategoriesViewModel.showCategories(type)
    }
    // ✅ Explicación:
    // 1. LaunchedEffect con 'type' como key garantiza que la carga de categorías
    //    se haga solo cuando realmente cambia el tipo.
    // 2. Evita llamar showCategories repetidamente en cada recomposición.
    // 3. El contenido del Composable se renderiza mientras la carga ocurre.

    // 🔹 LaunchedEffect para efectos (eventos de un solo uso)
    LaunchedEffect(Unit) {
        // LaunchedEffect(Unit) significa que este bloque se lanza **una sola vez** al crear el Composable
        // y **no depende de ninguna variable**.
        selectCategoriesViewModel.effect.collect { effect ->
            // Aquí estamos **recogiendo todos los eventos que el ViewModel emita**
            when (effect) {
                SelectCategoriesEffects.OnNavToAddRecordsScreen -> {
                    // Navegación inmediata: al primer click
                    navToAddRecords()
                }
                else -> Unit
            }
        }
    }
    // ✅ Explicación:
    // - Nunca usamos collectAsStateWithLifecycle para eventos, porque eso convierte eventos
    //   en estado y depende de recomposiciones.
    // - collect dentro de LaunchedEffect garantiza que **no se pierde ningún evento**
    //   y que se maneja exactamente cuando llega.
    // - Unit como key asegura que este collector se instale solo **una vez** y no se reinicie
    //   con cada recomposición del Composable.

    Column(modifier = Modifier.fillMaxSize()) {
        // Título según tipo
        HeadSetting(
            title = if (type == CategoryType.INCOME)
                stringResource(id = R.string.chooseincome)
            else
                stringResource(id = R.string.chooseexpense),
            MaterialTheme.typography.headlineSmall
        )

        // Grid de categorías
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            items(
                items = categories,
                key = { it.id } // Estabilidad ante recomposiciones
            ) { category ->
                CategoryEntries(
                    category = category,
                    modifier = Modifier.padding(10.dp),
                    onClickItem = {
                        // 🔹 Emitir evento directamente al ViewModel
                        // No dependemos del estado, no hay doble click
                        selectCategoriesViewModel.onUserEvent(
                            SelectCategoriesUiEvent.OnCategorySelected(category)
                        )
                    }
                )
            }
        }
    }
}