package carnerero.agustin.cuentaappandroid.presentation.ui.main.menu.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMyAccountsBar(scope: CoroutineScope, drawerState: DrawerState, title: Int) {

    TopAppBar(
        title = { Text(text = stringResource(id = title) ) },
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(
                    if (drawerState.isOpen) Icons.Filled.Close else Icons.Filled.Menu,
                    contentDescription = "Side menu",
                    tint = colors.topBarContent
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.barBackground,
            titleContentColor = colors.topBarContent,
            actionIconContentColor = colors.topBarContent
        )
    )
}
