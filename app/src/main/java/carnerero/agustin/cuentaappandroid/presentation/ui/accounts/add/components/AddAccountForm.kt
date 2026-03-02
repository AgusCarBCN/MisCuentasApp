package carnerero.agustin.cuentaappandroid.presentation.ui.accounts.add.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.BoardType
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.IconAnimated
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.ModelButton
import carnerero.agustin.cuentaappandroid.presentation.common.sharedcomponents.TextFieldComponent
import carnerero.agustin.cuentaappandroid.presentation.theme.AppTheme.colors

@Composable
fun AddAccountForm(
    accountName:String,
    balance:String,
    onAccountNameChange:(String)->Unit,
    onAccountBalanceChange:(String)->Unit,
    addAccount:()->Unit,
    isEnabledButton:Boolean
) {

    val fieldModifier = Modifier
        .fillMaxWidth(0.85f)
        .heightIn(min = 48.dp)

    Text(
        text = stringResource(R.string.createAccount),
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        color = colors.textColor
    )

    IconAnimated(
            iconResource = R.drawable.configaccountoption,
            sizeIcon = 120,
            colors.imageTutorialInit,
            colors.imageTutorialTarget
     )

    TextFieldComponent(
        modifier = fieldModifier,
        stringResource(R.string.amountName),
        accountName,
        onTextChange =onAccountNameChange,
        BoardType.TEXT,
        false
    )

    TextFieldComponent(
        modifier = fieldModifier,
        stringResource(R.string.enteramount),
        balance,
        onTextChange =  onAccountBalanceChange,
        BoardType.DECIMAL,
        false
    )

    ModelButton(
        text = stringResource(R.string.addAccount),
        MaterialTheme.typography.labelLarge,
        modifier = fieldModifier,
        isEnabledButton,
        onClickButton = {
           addAccount()
        }
    )
}
