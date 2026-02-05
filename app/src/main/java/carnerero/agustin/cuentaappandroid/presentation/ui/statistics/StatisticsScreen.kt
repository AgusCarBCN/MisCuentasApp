package carnerero.agustin.cuentaappandroid.presentation.ui.stadistics

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.RowComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel

@Composable
fun StatisticsScreen(
                     navToBarChart:()->Unit,
                     navToPieChart:()->Unit)
{
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxWidthDp = maxWidth
        val maxHeightDp = maxHeight
        val fieldModifier = Modifier
            .width(maxWidthDp * 0.85f) // mismo ancho para TODOS
            .heightIn(min = 48.dp)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        )

        {
            HeadSetting(
                title = stringResource(id = R.string.stadistics),
                MaterialTheme.typography.headlineSmall
            )

            RowComponent(
                fieldModifier,
                title = stringResource(id = R.string.barchart),
                description = stringResource(id = R.string.barchartdes),
                iconResource = R.drawable.barchartoption,
                onClick = {
                    navToBarChart()
                })

            RowComponent(
                fieldModifier,
                title = stringResource(id = R.string.piechart),
                description = stringResource(id = R.string.piechartdes),
                iconResource = R.drawable.ic_piechart,
                onClick = {
                    navToPieChart()
                })

        }
    }
}





















