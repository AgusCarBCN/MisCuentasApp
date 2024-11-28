package carnerero.agustin.cuentaappandroid.presentation.ui.stadistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.HeadSetting
import carnerero.agustin.cuentaappandroid.presentation.ui.setting.components.RowComponent
import carnerero.agustin.cuentaappandroid.presentation.ui.main.model.IconOptions
import carnerero.agustin.cuentaappandroid.presentation.ui.main.view.MainViewModel

@Composable
fun StadisticsScreen(mainViewModel: MainViewModel)
{

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 25.dp)

            )

    {
        HeadSetting(title = stringResource(id = R.string.stadistics), MaterialTheme.typography.headlineSmall)

        RowComponent(title = stringResource(id = R.string.barchart),
            description = stringResource(id = R.string.barchartdes),
            iconResource = R.drawable.barchartoption,
            onClick = {
                mainViewModel.selectScreen(IconOptions.BARCHART)
            })

        RowComponent(title = stringResource(id = R.string.piechart),
            description = stringResource(id = R.string.piechartdes),
            iconResource = R.drawable.ic_piechart,
            onClick = {
                mainViewModel.selectScreen(IconOptions.PIE_CHART)
            })

    }
}





















