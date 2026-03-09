package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectcategories


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.components.DialogSpendingControl
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectcategories.componets.CategoryWithCheckbox
import carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectcategories.model.SelectCategoriesUiEvent

import carnerero.agustin.cuentaappandroid.utils.Utils
@Composable
fun SelectCategoriesScreen(
    viewModel: SelectCategoriesSpendingControlViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val sortedCategories = remember(state.categories) {
        Utils.getSortedCategories(state.categories, context)
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {

        val maxWidthDp = maxWidth * 0.85f

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeadSetting(
                title = stringResource(id = R.string.selectcategories),
                MaterialTheme.typography.titleLarge
            )

            LazyColumn(
                modifier = Modifier
                    .width(maxWidthDp)
                    .weight(1f)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(16.dp)
            ) {
                items(sortedCategories) { (category, _) ->

                    CategoryWithCheckbox(
                        category = category,
                        onCheckBoxChange = { checked ->
                            viewModel.onUserEvent(
                                SelectCategoriesUiEvent.OnCheckedChange(
                                    category.id,
                                    checked
                                )
                            )
                            if (checked) {
                                viewModel.onUserEvent(
                                    SelectCategoriesUiEvent.OnOpenDialog(category)
                                )
                            }
                        }
                    )
                }
            }
        }

        // 🔥 Dialog fuera del LazyColumn
        if (state.dialogUiState.showDialog) {

            val selectedCategory = state.categories
                .firstOrNull { it.id == state.dialogUiState.id }

            selectedCategory?.let { category ->
                DialogSpendingControl(
                    name = stringResource(R.string.name),
                    dialogState = state.dialogUiState,
                    onUserEvent = viewModel::onUserEvent,
                    onConfirm = { SelectCategoriesUiEvent.OnConfirm },
                    onClose = { SelectCategoriesUiEvent.OnCloseDialog },
                    onSpendLimitChange = { SelectCategoriesUiEvent.OnSpendLimitChange(it) },
                    onSelectDate = { field, date -> SelectCategoriesUiEvent.OnSelectDate(field, date) },
                    onShowDatePicker = { field, visible -> SelectCategoriesUiEvent.OnShowDatePicker(field, visible) }
                )
            }
        }
    }
}