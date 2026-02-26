package carnerero.agustin.cuentaappandroid.presentation.ui.spendingcontrol.selectcategories.componets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.data.db.entities.Category
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors

@Composable
fun CategoryWithCheckbox(
    category: Category,
    onCheckBoxChange: (Boolean) -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardColors(
            containerColor = colors.drawerColor,
            contentColor = colors.incomeColor,
            disabledContainerColor = colors.drawerColor,
            disabledContentColor = colors.incomeColor
        ),
        modifier = Modifier.size(width = 360.dp, height = 80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    painter = painterResource(id = category.iconResource),
                    contentDescription = null,
                    tint=colors.textColor,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = stringResource(id = category.nameResource),
                        style = MaterialTheme.typography.bodyMedium,
                        color=colors.textColor
                    )

                    Text(
                        text = if (category.isChecked)
                            stringResource(R.string.categorychecked)
                        else
                            stringResource(R.string.categoryunchecked),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textColor
                    )
                }
            }

            Checkbox(
                checked = category.isChecked,
                onCheckedChange = onCheckBoxChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = colors.drawerColor,
                    uncheckedColor = colors.textColor,
                    checkmarkColor = colors.incomeColor
                )
            )
        }
    }
}
/*
@Composable
fun CategoryWithCheckbox(
    category: Category,
    dialogState: DialogUiState,
    onCheckBoxChange:(Boolean)->Unit,
    onEventUserDialog:(SelectCategoriesUiEvent)->Unit
)
{

    val categoryName= stringResource(category.nameResource)
    val checked=stringResource(R.string.ischecked)
    val unchecked = stringResource(R.string.isunchecked)
    val item= stringResource(id = R.string.item)
    val iconItem= stringResource(id = R.string.itemicon)

    ElevatedCard(

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardColors(
            containerColor = colors.drawerColor,
            contentColor = colors.incomeColor,
            disabledContainerColor = colors.drawerColor,
            disabledContentColor = colors.incomeColor
        ),   modifier = Modifier
            .size(width = 360.dp, height = 80.dp)

    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 10.dp, start = 10.dp)
                .semantics {
                    contentDescription = "$item $categoryName"
                },
            horizontalArrangement = Arrangement.SpaceBetween, // Cambia a SpaceBetween
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Contenedor para el icono y el texto para que se agrupen en un extremo
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = category.iconResource),
                    contentDescription = "$iconItem $categoryName",
                    modifier = Modifier
                        .size(iconSize.extraLarge)
                        .padding(end = dimens.medium),
                    tint = colors.textHeadColor
                )
                Column {
                    Text(
                        text = stringResource(id = category.nameResource),
                        modifier = Modifier.padding(bottom = dimens.small),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textColor
                    )
                    Text(
                        text = if (category.isChecked) stringResource(id = R.string.categorychecked)

                        else stringResource(id = R.string.categoryunchecked),
                        modifier = Modifier.semantics {

                            contentDescription = if(category.isChecked) "$categoryName $checked"
                            else "$categoryName $unchecked"
                        },
                        color = colors.textColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Checkbox al extremo opuesto
            Checkbox(modifier = Modifier.semantics {
                // Descripción única del Checkbox
                contentDescription = "${category.nameResource}: ${if (category.isChecked) checked else unchecked}"
            },
                checked = category.isChecked,
                onCheckedChange = {onCheckBoxChange(it)},
                colors = CheckboxDefaults.colors(
                    checkedColor = colors.drawerColor,
                    uncheckedColor = colors.textColor,
                    checkmarkColor = colors.incomeColor
                )
            )
        }

        if (category.isChecked) {
            DialogSpendingControl(stringResource(category.nameResource),
                dialogState,
                category.id,
                onEventUserDialog
                )

        }
    }
}

*/
